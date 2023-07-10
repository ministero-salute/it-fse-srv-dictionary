package it.finanze.sanita.fse2.ms.edssrvdictionary.changeset;

import it.finanze.sanita.fse2.ms.edssrvdictionary.changeset.base.AbstractChunkResources;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChunksRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Profile.TEST;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.impl.ChunksRepo.REMOVE_AFTER_X_HOURS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(TEST)
@TestInstance(PER_CLASS)
public class ChunksRepositoryTest extends AbstractChunkResources {

    @Autowired
    private IChunksRepo repository;

    @BeforeEach
    void init() {
        mongo.dropCollection(ChunksIndexETY.class);
        mongo.dropCollection(ChunkETY.class);
    }

    // *** DATA REMOVER ***

    @Test
    void shouldRemoveNoIndex() {
        // Args
        TestResource[] res = new TestResource[] {
            createResource("codesystem", null),
            createResource("valueset", null)
        };
        // Generate active chunks
        insert(
            new TestSetting(res[0], false),
            new TestSetting(res[1], false)
        );
        // Execute
        assertDoesNotThrow(() -> {
            // Retrieve removed chunks
            List<ChunksIndexETY> indexes = repository.removeIndexes();
            // Check
            assertTrue(indexes.isEmpty());
            // Verify exists
            assertResourceExists(res[0], true);
            assertResourceExists(res[1], true);
        });
    }

    @Test
    void shouldRemoveAllIndexes() {
        // Args
        TestResource[] res = new TestResource[] {
            createResource("codesystem", getDateBeforeHours(REMOVE_AFTER_X_HOURS + 1)),
            createResource("valueset", getDateBeforeHours(REMOVE_AFTER_X_HOURS + 3))
        };
        // Generate active chunks
        int size = insert(
            new TestSetting(res[0], false),
            new TestSetting(res[1], false)
        );
        // Execute
        assertDoesNotThrow(() -> {
            // Retrieve removed chunks
            List<ChunksIndexETY> indexes = repository.removeIndexes();
            // Check
            assertEquals(size, indexes.size());
            // Verify not exists in collection
            assertResourceExists(res[0], false);
            assertResourceExists(res[1], false);
        });
    }

    @Test
    void shouldRemoveOneIndex() {
        // Args
        TestResource[] res = new TestResource[] {
            createResource("codesystem", getDateBeforeHours(REMOVE_AFTER_X_HOURS + 1)),
            createResource("valueset", getDateBeforeHours(REMOVE_AFTER_X_HOURS - 3))
        };
        // Generate active chunks
        int size = insert(
            new TestSetting(res[0], false),
            new TestSetting(res[1], false)
        );
        // Execute
        assertDoesNotThrow(() -> {
            // Retrieve removed chunks
            List<ChunksIndexETY> indexes = repository.removeIndexes();
            // Check
            assertEquals(size - 1, indexes.size());
            // Verify existence
            assertResourceExists(res[0], false);
            assertResourceExists(res[1], true);
        });
    }

    // *** ORPHAN CHUNKS REMOVER ***

    @Test
    void shouldRemoveNoChunk() {
        // Args
        TestResource[] res = new TestResource[] {
            createResource("codesystem", null),
            createResource("valueset", null)
        };
        // Generate active chunks
        insert(
            new TestSetting(res[0], false),
            new TestSetting(res[1], false)
        );
        // Execute
        assertDoesNotThrow(() -> {
            // Retrieve removed chunks
            List<ObjectId> indexes = repository.removeOrphanChunks();
            // Check
            assertTrue(indexes.isEmpty());
            // Verify exists
            assertResourceExists(res[0], true);
            assertResourceExists(res[1], true);
        });
    }

    @Test
    void shouldRemoveNoChunkDespiteMissingIndex() {
        // Args
        TestResource[] res = new TestResource[] {
            createResource("codesystem", null),
            createResource("valueset", null)
        };
        // Generate active chunks
        insert(
            new TestSetting(res[0], true),
            new TestSetting(res[1], true)
        );
        // Execute
        assertDoesNotThrow(() -> {
            // Retrieve removed chunks
            List<ObjectId> indexes = repository.removeOrphanChunks();
            // Check
            assertTrue(indexes.isEmpty());
            // Verify exists
            assertResourceExists(res[0], true, true);
            assertResourceExists(res[1], true, true);
        });
    }

    @Test
    void shouldRemoveOneChunk() {
        // Args
        TestResource[] res = new TestResource[] {
            createResource("codesystem", null),
            createResource("valueset", null, getDateBeforeHours(REMOVE_AFTER_X_HOURS + 3))
        };
        // Generate chunks
        int size = insert(
            new TestSetting(res[0], false),
            new TestSetting(res[1], true)
        );
        // Execute
        assertDoesNotThrow(() -> {
            // Retrieve removed chunks
            List<ObjectId> indexes = repository.removeOrphanChunks();
            // Check
            assertEquals(size - 1, indexes.size());
            // Verify exists
            assertResourceExists(res[0], true);
            assertResourceExists(res[1], false);
        });
    }

    @Test
    void shouldRemoveAllChunks() {
        // Args
        TestResource[] res = new TestResource[] {
            createResource("codesystem", null, getDateBeforeHours(REMOVE_AFTER_X_HOURS + 1)),
            createResource("valueset", null, getDateBeforeHours(REMOVE_AFTER_X_HOURS + 3))
        };
        // Generate chunks
        int size = insert(
            new TestSetting(res[0], true),
            new TestSetting(res[1], true)
        );
        // Execute
        assertDoesNotThrow(() -> {
            // Retrieve removed chunks
            List<ObjectId> indexes = repository.removeOrphanChunks();
            // Check
            assertEquals(size, indexes.size());
            // Verify not exists in collection
            assertResourceExists(res[0], false);
            assertResourceExists(res[1], false);
        });
    }

    @AfterAll
    void teardown() {
        mongo.dropCollection(ChunksIndexETY.class);
        mongo.dropCollection(ChunkETY.class);
    }

}
