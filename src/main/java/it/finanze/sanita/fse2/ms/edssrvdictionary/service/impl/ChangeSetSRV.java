package it.finanze.sanita.fse2.ms.edssrvdictionary.service.impl;

import com.google.common.collect.Lists;
import it.finanze.sanita.fse2.ms.edssrvdictionary.client.IQueryClient;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ResourceDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.chunks.PartialChunkDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceItemDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.EngineInitException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChunksRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.IChangeSetSRV;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.utils.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.*;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO.HistoryDeleteDTO;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO.HistoryInsertDTO;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_QP_CHUNK;

@Service
@Slf4j
public class ChangeSetSRV implements IChangeSetSRV {

    private static final int CHUNK_SIZE = 1;

    @Autowired
    private IQueryClient client;

    @Autowired
    private IChunksRepo repository;

    private volatile boolean syncing;

    @Override
    public HistoryDTO history(Date lastUpdate) {
        // Check for syncing
        if(syncing) throw new EngineInitException(ERR_SRV_INIT_ENGINE);
        log.debug("Retrieving history at {}", lastUpdate);
        // Retrieve history
        HistoryDTO history = client.getHistory(lastUpdate);
        // Sync mongo with history
        syncAt(history);
        // Return object
        return history;
    }

    @Override
    public ResourceDTO resource(String resource, String version, String ref, int chunk) throws DocumentNotFoundException, OutOfRangeException {
        SearchResult res;
        // Check for syncing
        if(syncing) throw new EngineInitException(ERR_SRV_INIT_ENGINE);
        // Get index
        if(ref != null) {
            res = retrieveByRef(ref, chunk);
        } else {
            res = retrieveBy(resource, version, chunk);
        }
        return ResourceDTO.from(res.getIdx(), res.getItems(), chunk);
    }

    private SearchResult retrieveBy(String resource, String version, int chunk) throws DocumentNotFoundException, OutOfRangeException {
        SearchResult res;
        // Get reference
        Optional<ChunksIndexETY> index = repository.findByResourceVersion(resource, version);
        // Check if exists
        if(!index.isPresent()) {
            throw new DocumentNotFoundException(ERR_SRV_DOCUMENT_NOT_EXIST);
        }
        ChunksIndexETY etx = index.get();
        // Check if whitelisted
        if(etx.getMeta().isWhitelist()) {
            res = new SearchResult(etx, ChunkETY.empty());
        } else {
            res = retrieveByRef(etx.getId().toString(), chunk);
        }
        // Return by ref
        return res;
    }

    @Async("single-thread-exec")
    @Override
    public void initHistoryStorage() {
        log.debug("History engine running on {}", Thread.currentThread().getName());
        syncing = true;
        log.info("Initialising history storage");
        syncAt(client.getHistory(null));
        log.info("Finishing setup for history storage");
        syncing = false;
    }

    @Override
    public long size() {
        return repository.getActiveItems();
    }

    @Override
    public List<ChunksIndexETY> clearIndexes() {
        return repository.removeIndexes();
    }

    @Override
    public List<ObjectId> clearOrphanChunks() {
        return repository.removeOrphanChunks();
    }

    private synchronized void syncAt(HistoryDTO history) {
        log.debug("Starting synchronization with history");
        resourcesToRemove(history.getDeletions());
        resourcesToDownload(history.getInsertions());
        log.debug("Finish synchronization");
    }

    private void resourcesToRemove(List<HistoryDeleteDTO> deletions) {
        log.debug("Checking resources to mark as removed");
        for (int i = 0; i < deletions.size(); i++) {
            // Get item
            HistoryDeleteDTO item = deletions.get(i);
            // Remove
            repository.markIndexAsRemovable(item.getId(), item.getOmit());
            // Check for omission
            if(item.getOmit() != null) {
                log.debug(
                    "[#{}] {} marked as resource to update, removing previous version but omitting {} (latest)",
                    i + 1,
                    item.getId(),
                    item.getOmit()
                );
            } else {
                log.debug("[#{}] {} marked as resource to remove completely", i + 1, item.getId());
            }
        }
    }

    private void resourcesToDownload(List<HistoryInsertDTO> insertions) {
        List<HistoryInsertDTO> res = new ArrayList<>();

        log.debug("Checking resources to download");

        for (int i = 0; i < insertions.size(); i++) {
            // Get item
            HistoryInsertDTO item = insertions.get(i);
            // Unavailable resources are going to be downloaded
            if(!repository.exists(item.getId(), item.getVersion())) {
                log.debug("[#{}] {} marked as resource to download", i + 1, item);
                res.add(item);
            }
        }

        if(res.isEmpty()) {
            log.debug("There are no resources to download, everything is available");
        } else {
            downloadResource(res);
        }
    }

    private void downloadResource(List<HistoryInsertDTO> resources) {
        for (HistoryInsertDTO res : resources) {
            log.debug("[{}] Downloading resource", res);
            // Retrieve resource
            HistoryResourceDTO resource = client.getResource(res.getId(), res.getVersion());
            // Create root
            ObjectId root = new ObjectId();
            // Get items
            List<ChunkETY> chunks = generateChunks(root, resource);
            // Add index
            repository.createChunkIndex(ChunksIndexETY.from(resource, root, chunks));
        }
    }

    private List<ChunkETY> generateChunks(ObjectId root, HistoryResourceDTO res) {
        log.debug("[{}] Starting chunks generation", res.info());
        // Hold chunks references
        List<ChunkETY> chunks = new ArrayList<>();
        // Get values
        List<ResourceItemDTO> values = res.getItems();
        // Check for empty resources
        if(!values.isEmpty()) {
            // Split
            List<List<ResourceItemDTO>> partition = Lists.partition(values, CHUNK_SIZE);
            // Iterate
            for (int idx = 0; idx < partition.size(); idx++) {
                log.debug("[{}] Generating chunk {}/{}", res.info(), idx + 1, partition.size());
                // Get chunk items
                List<ResourceItemDTO> items = partition.get(idx);
                // Create chunk
                ChunkETY instance = ChunkETY.from(
                    res.getMeta(),
                    new PartialChunkDTO(root, idx, items)
                );
                // Insert
                repository.createChunk(instance);
                // Add
                chunks.add(instance);
            }
        } else {
            log.debug("Resource {} is empty, no chunks will be generated", res.info());
        }
        log.debug("[{}] Finishing chunk generation", res.info());
        return chunks;
    }

    private SearchResult retrieveByRef(String ref, int chunk) throws DocumentNotFoundException, OutOfRangeException {
        ObjectId current;
        ChunksIndexETY idx;
        ChunkETY items;
        // Retrieve index
        Optional<ChunksIndexETY> index = repository.getChunkIndex(ref);
        // Check if exists
        if(!index.isPresent()) {
            throw new DocumentNotFoundException(ERR_SRV_DOCUMENT_NOT_EXIST);
        }
        // Verify boundaries
        idx = index.get();
        try {
            // Get object id
            current = idx.getChunks().get(chunk);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new OutOfRangeException(ERR_VAL_IDX_CHUNK_NOT_VALID, API_QP_CHUNK);
        }
        // Retrieve index
        Optional<ChunkETY> v = repository.getChunk(current.toHexString());
        // Check if exists
        if(!v.isPresent()) {
            throw new DocumentNotFoundException(ERR_SRV_DOCUMENT_CHUNK_NOT_EXIST);
        }
        // Get
        items = v.get();

        return new SearchResult(idx, items);
    }

}
