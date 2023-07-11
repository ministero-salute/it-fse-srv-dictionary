package it.finanze.sanita.fse2.ms.edssrvdictionary.changeset;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.changeset.base.ResourceTypeTest.CODESYSTEM;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Profile.TEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.edssrvdictionary.changeset.base.AbstractChangeset;
import it.finanze.sanita.fse2.ms.edssrvdictionary.client.IQueryClient;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO.HistoryDeleteDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO.HistoryInsertDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceItemDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistorySnapshotDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistorySnapshotDTO.Resources;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChunksRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.ResChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.impl.ChangeSetSRV;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(TEST)
@TestInstance(PER_CLASS)
class ChangesetTest extends AbstractChangeset {
    
    @Autowired
    private IChunksRepo repository;

    @MockBean
    private IQueryClient client;

    @Autowired
    private ChangeSetSRV service;
    
    @Test
    void getChangesetHistoryTest() throws OperationException {
        // Build resource insertion
        HistoryInsertDTO insertion = createInsertion(CODESYSTEM);
        // Build insertions
        List<HistoryInsertDTO> insertions = new ArrayList<HistoryInsertDTO>();
        insertions.add(insertion);
        // Build expected history
        HistoryDTO history = createHistory(insertions, new ArrayList<HistoryDeleteDTO>());
        // Build items
        List<ResourceItemDTO> items = createItemsList();
        // Build history resource dto
        HistoryResourceDTO resourceDTO = createHistoryResource(insertion, items);
        // Stubs response
        when(client.getHistory(any())).thenReturn(history);
        when(client.getResource(insertion.getId(), insertion.getVersion())).thenReturn(resourceDTO);
        // Call history method from service
        HistoryDTO response = service.history(null);
        assertEquals(history.getInsertions(), response.getInsertions(), "Insertions expected doesn't match with actual");
        // Call findByResourceVersione method from repository to fetch chunks index
        Optional<ChunksIndexETY> resource = repository.findByResourceVersion(resourceDTO.getResourceId(), resourceDTO.getVersionId());
        assertTrue(resource.isPresent(), "Resource is not present");
        // Get chunks from index
        ChunksIndexETY index = resource.get();
        List<ObjectId> chunks = index.getChunks();
        int sizeChunks = 0;
        for (ObjectId id : chunks) {
            Optional<ChunkETY> ety = repository.getChunk(id.toHexString());
            ResChunkETY chunk = ety.get().getChunk();
            assertTrue(ety.isPresent(), "Id doesn't match any chunks on db");
            assertEquals(index.getId().toHexString(), chunk.getRoot().toHexString());
            assertEquals(chunk.getSize(), chunk.getValues().size(), "Size mismatch");
            sizeChunks += chunk.getSize();
        }
        assertEquals(index.getSize(), sizeChunks, "Expected chunks size doesn't match current chunks size");
    }

    @Test
    void getChangesetIntegrityTest() throws OperationException {
        // Build TestResource
        TestResource resource = createResource(CODESYSTEM, null, new Date());
        // Call history method from service
        HistorySnapshotDTO expected = new HistorySnapshotDTO();
        expected.setResources(List.of(resource.asResource()));
        // Insert resources on mongodb
        insert(false, resource);
        // Stub
        when(client.getSnapshot()).thenReturn(expected);
        // Call snapshot method from service
        HistorySnapshotDTO actual = service.snapshot();
        List<Resources> expectedRes = expected.getResources();
        List<Resources> actualRes = actual.getResources();
        assertEquals(expectedRes.size(), actualRes.size(), "Size of actual resources doesn't match expected");
        assertEquals(expectedRes, actualRes, "Actual resources don't match expected");
        // Check if actual resources size matches with expected resources size
        assertEquals(expectedRes.get(0).getSize(), actualRes.get(0).getSize(), "Size of resource "+actualRes.get(0).getId()+" doesn't match");
    }

    @Test
    void getChangesetIntegrityNullSizeTest() {
        HistorySnapshotDTO expected = new HistorySnapshotDTO();
        Resources resource = new Resources(generateResourceId(), CODESYSTEM.getValue(), generateVersion(), null);
        expected.setResources(List.of(resource));
        // Stub
        when(client.getSnapshot()).thenReturn(expected);
        // Call snapshot method from service
        HistorySnapshotDTO actual = service.snapshot();
        List<Resources> expectedRes = expected.getResources();
        List<Resources> actualRes = actual.getResources();
        assertEquals(expectedRes.size(), actualRes.size(), "Size of actual resources doesn't match expected");
        assertEquals(expectedRes, actualRes, "Actual resources don't match expected");
        assertEquals(null, actualRes.get(0).getSize(), "Size of resource "+actualRes.get(0).getId()+" is not null");
    }

    @Test
    void getChangesetIntegrityMixedTest() {
        // Build TestResource with size
        TestResource resource1 = createResource(CODESYSTEM, null, new Date());
        // Build Resource without size
        Resources resource2 = new Resources(generateResourceId(), CODESYSTEM.getValue(), generateVersion(), null);
        // Call history method from service
        HistorySnapshotDTO expected = new HistorySnapshotDTO();
        expected.setResources(List.of(resource1.asResource(), resource2));
        // Insert resources on mongodb
        insert(false, resource1);
        // Stub
        when(client.getSnapshot()).thenReturn(expected);
        // Call snapshot method from service
        HistorySnapshotDTO actual = service.snapshot();
        List<Resources> expectedRes = expected.getResources();
        List<Resources> actualRes = actual.getResources();
        assertEquals(expectedRes.size(), actualRes.size(), "Size of actual resources doesn't match expected");
        assertEquals(expectedRes, actualRes, "Actual resources don't match expected");
        // Check if actual resources size matches with expected resources size
        assertEquals(expectedRes.get(0).getSize(), actualRes.get(0).getSize(), "Size of resource "+actualRes.get(0).getId()+" doesn't match");
        assertEquals(null, actualRes.get(1).getSize(), "Size of resource "+actualRes.get(1).getId()+" is not null");
    } 

}
