package it.finanze.sanita.fse2.ms.edssrvdictionary.service.impl;

import com.google.common.collect.Lists;
import it.finanze.sanita.fse2.ms.edssrvdictionary.client.IQueryClient;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.chunks.PartialChunkDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceItemDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChunksRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.IChangeSetSRV;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO.HistoryDeleteDTO;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO.HistoryInsertDTO;

@Service
@Slf4j
public class ChangeSetSRV implements IChangeSetSRV {

    private static final int CHUNK_SIZE = 1;

    @Autowired
    private IQueryClient client;

    @Autowired
    private IChunksRepo repository;

    @Override
    public HistoryDTO history(Date lastUpdate) {
        log.debug("Retrieving history at {}", lastUpdate);
        // Retrieve history
        HistoryDTO history = client.getHistory(lastUpdate);
        // Sync mongo with history
        syncAt(history);
        // Return object
        return history;
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

}
