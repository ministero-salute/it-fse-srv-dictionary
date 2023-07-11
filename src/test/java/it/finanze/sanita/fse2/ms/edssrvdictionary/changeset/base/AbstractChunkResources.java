package it.finanze.sanita.fse2.ms.edssrvdictionary.changeset.base;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.service.impl.ChangeSetSRV.CHUNK_SIZE;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.google.common.collect.Lists;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceItemDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistorySnapshotDTO.Resources;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.ChunkMetaETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.IndexMetaETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.ResChunkETY;
import lombok.Value;

public abstract class AbstractChunkResources {

    @Autowired
    protected MongoTemplate mongo;

    private static final SecureRandom generator = new SecureRandom();

    protected TestResource createResource(ResourceTypeTest type, @Nullable Date deleted, Date insertion, int size) {
        List<ChunkETY> chunks = new ArrayList<>();
        // Check for zero
        if(size == 0) size = 1;
        // Generate fake index
        ChunksIndexETY index = generateIndex(type, deleted, size);
        // Create items
        List<ResourceItemDTO> items = generateItems(size);
        // Split by chunk
        List<List<ResourceItemDTO>> parts = Lists.partition(items, CHUNK_SIZE);
        // Iterate
        for(int i = 0; i < parts.size(); ++i) {
            // Generate fake chunk
            ChunkETY e = generateChunk(index, i, insertion, parts.get(i));
            // Add id to index
            index.getChunks().add(e.getId());
            // Save chunk
            chunks.add(e);
        }
        return new TestResource(index, chunks);
    }

    protected TestResource createResource(ResourceTypeTest type, @Nullable Date deleted) {
        return createResource(type, deleted, new Date(), generateNumber(CHUNK_SIZE * 4));
    }

    protected TestResource createResource(ResourceTypeTest type, @Nullable Date deleted, Date insertion) {
        return createResource(type, deleted, insertion, 0);
    }

    private ChunksIndexETY generateIndex(ResourceTypeTest type, @Nullable Date deleted, int size) {
        return new ChunksIndexETY(
            new ObjectId(),
            String.valueOf(generateNumber(9999)),
            String.valueOf(generateNumber(1000)),
            new IndexMetaETY(
                generateOid(),
                generateVersion(),
                type.getValue(),
                new Date(),
                false
            ),
            new ArrayList<>(),
            size,
            deleted
        );
    }

    private ChunkETY generateChunk(ChunksIndexETY index, int idx, Date insertedAt, List<ResourceItemDTO> items) {
        ChunkETY chunk = new ChunkETY(
            ChunkMetaETY.from(index.getMeta()),
            new ResChunkETY(
                index.getId(),
                idx,
                items,
                items.size()
            )
        );
        chunk.setId(new ObjectId());
        chunk.setInsertedAt(insertedAt);
        return chunk;
    }

    private List<ResourceItemDTO> generateItems(int size) {
        List<ResourceItemDTO> items = new ArrayList<>();
        for(int i = 0; i < size; ++i) {
            items.add(new ResourceItemDTO("code#" + i, "display#" + i));
        }
        return items;
    }

    protected String generateOid() {
        return generateId(5);
    }

    protected String generateVersion() {
        return generateId(2);
    }

    protected String generateResourceId() {
        return generateId(1);
    }

    private String generateId(int depth) {
        StringBuilder oid = new StringBuilder();
        for(int i = 0; i < depth; ++i) {
            oid.append(generateNumber(999)).append('.');
        }
        oid.deleteCharAt(oid.length() - 1);
        return oid.toString();
    }

    private int generateNumber(int bound) {
        return Math.abs(generator.nextInt(bound));
    }

    protected Date getDateBeforeHours(int hours) {
        return Date.from(Instant.now().minus(hours, HOURS));
    }

    @Value
    public static class TestResource {
        ChunksIndexETY index;
        List<ChunkETY> chunks;

        public Resources asResource() {
            return new Resources();
        }
    }

    @Value
    public static class TestSetting {
        TestResource resource;
        boolean omitIndex;
    }

    protected void assertResourceExists(TestResource e, boolean expected) {
        assertResourceExists(e, expected, false);
    }

    protected void assertResourceExists(TestResource e, boolean expected, boolean omitIndexCheck) {
        String verb = expected ? "doesn't" : "does";
        if(!omitIndexCheck) {
            // Check index exists
            assertEquals(
                expected,
                exists(ChunksIndexETY.class, e.getIndex().getId()),
                String.format("Expected index %s exists", verb)
            );
        }
        // Check chunks exists
        for (ChunkETY chunk : e.getChunks()) {
            assertEquals(
                expected,
                exists(ChunkETY.class, chunk.getId()),
                String.format("Expected chunk %s exists", verb)
            );
        }
    }

    protected boolean exists(Class<?> clazz, ObjectId id) {
        return mongo.exists(new Query(where("_id").is(id)), clazz);
    }

    protected int insert(boolean omitIndex, TestResource res) {
        if (!omitIndex) mongo.insert(res.getIndex());
        mongo.insertAll(res.getChunks());
        return 1;
    }

    protected int insert(TestSetting ...res) {
        int size = 0;
        for (TestSetting r : res) {
            size += insert(r.isOmitIndex(), r.getResource());
        }
        return size;
    }

}
