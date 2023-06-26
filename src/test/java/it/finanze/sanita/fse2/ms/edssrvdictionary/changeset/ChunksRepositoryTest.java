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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Profile.TEST;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.impl.ChunksRepo.REMOVE_AFTER_X_HOURS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(TEST)
@TestInstance(PER_CLASS)
public class ChunksRepositoryTest extends AbstractChunkResources {

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private IChunksRepo repository;

    @BeforeEach
    void init() {
        mongo.dropCollection(ChunksIndexETY.class);
        mongo.dropCollection(ChunkETY.class);
    }

    @Test
    void shouldRemoveNoIndex() {
        // Args
        TestResource[] res = new TestResource[] {
            createResource("codesystem", null),
            createResource("valueset", null)
        };
        // Generate active chunks
        insert(res);
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
        int size = insert(res);
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
        int size = insert(res);
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

    void assertResourceExists(TestResource e, boolean expected) {
        String verb = expected ? "doesn't" : "does";
        // Check index exists
        assertEquals(
            expected,
            exists(ChunksIndexETY.class, e.getIndex().getId()),
            String.format("Expected index %s exists", verb)
        );
        // Check chunks exists
        for (ChunkETY chunk : e.getChunks()) {
            assertEquals(
                expected,
                exists(ChunkETY.class, chunk.getId()),
                String.format("Expected chunk %s exists", verb)
            );
        }
    }

    boolean exists(Class<?> clazz, ObjectId id) {
        return mongo.exists(new Query(where("_id").is(id)), clazz);
    }
    int insert(TestResource ...res) {
        for (TestResource r : res) {
            mongo.insert(r.getIndex());
            mongo.insertAll(r.getChunks());
        }
        return res.length;
    }

    @AfterAll
    void teardown() {
        mongo.dropCollection(ChunksIndexETY.class);
        mongo.dropCollection(ChunkETY.class);
    }

}
