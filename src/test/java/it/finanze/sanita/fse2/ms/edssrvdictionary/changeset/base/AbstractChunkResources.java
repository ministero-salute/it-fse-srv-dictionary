package it.finanze.sanita.fse2.ms.edssrvdictionary.changeset.base;

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.ChunkMetaETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.IndexMetaETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.ResChunkETY;
import lombok.Value;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.HOURS;

public abstract class AbstractChunkResources {

    private static final SecureRandom generator = new SecureRandom();

    protected TestResource createResource(String type, @Nullable Date deleted) {
        return createResource(type, deleted, new Date());
    }

    protected TestResource createResource(String type, @Nullable Date deleted, Date insertionDate) {
        List<ChunkETY> chunks = new ArrayList<>();
        // Generate fake index
        ChunksIndexETY index = generateIndex(type, deleted);
        // Generate fake size
        int size = generateNumber(5);
        // Check for zero
        if(size == 0) size = 1;
        // Iterate
        for(int i = 0; i < size; ++i) {
            // Generate fake chunk
            ChunkETY e = generateChunk(index, i, insertionDate);
            // Add id to index
            index.getChunks().add(e.getId());
            // Save chunk
            chunks.add(e);
        }
        return new TestResource(index, chunks);
    }

    private ChunksIndexETY generateIndex(String type, @Nullable Date deleted) {
        return new ChunksIndexETY(
            new ObjectId(),
            String.valueOf(generateNumber(9999)),
            String.valueOf(generateNumber(1000)),
            new IndexMetaETY(
                generateOid(),
                generateVersion(),
                type,
                false
            ),
            new ArrayList<>(),
            0,
            deleted
        );
    }

    private ChunkETY generateChunk(ChunksIndexETY index, int idx, Date insertedAt) {
        ChunkETY chunk = new ChunkETY(
            ChunkMetaETY.from(index.getMeta()),
            new ResChunkETY(
                index.getId(),
                idx,
                new ArrayList<>(),
                0
            )
        );
        chunk.setId(new ObjectId());
        chunk.setInsertedAt(insertedAt);
        return chunk;
    }

    private String generateOid() {
        return generateId(5);
    }

    private String generateVersion() {
        return generateId(2);
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
    }

    @Value
    public static class TestSetting {
        TestResource resource;
        boolean omitIndex;
    }

}
