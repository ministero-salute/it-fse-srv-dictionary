package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.impl;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChunksRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY.*;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@Slf4j
public class ChunksRepo implements IChunksRepo {

    public static final int REMOVE_AFTER_X_HOURS = 12;

    @Autowired
    private MongoTemplate mongo;

    @Override
    public void createChunk(ChunkETY obj) {
        mongo.insert(obj);
    }

    @Override
    public void createChunkIndex(ChunksIndexETY obj) {
        mongo.insert(obj);
    }

    @Override
    public long getActiveItems() {
        // Temporary field
        final String counter = "total";
        // Get collection
        MongoCollection<Document> ctx = mongo.getCollection(
            mongo.getCollectionName(ChunksIndexETY.class)
        );
        // Prepare pipeline
        Bson match = match(Filters.eq(FIELD_IDX_DELETED_AT, null));
        Bson unwind = unwind("$" + FIELD_IDX_SIZE);
        Bson group = group(null, sum(counter, "$" + FIELD_IDX_SIZE));
        // Execute
        AggregateIterable<Document> aggregate = ctx.aggregate(Arrays.asList(match, unwind, group));
        // Check nullity
        Document document = Objects.requireNonNull(aggregate.first());
        // Return value
        return Objects.requireNonNull(document.getLong(counter));
    }

    @Override
    public boolean exists(String id, String version) {
        Query q = new Query(
            where(FIELD_IDX_RESOURCE).is(id)
            .and(FIELD_IDX_VERSION).is(version)
            .and(FIELD_IDX_DELETED_AT).isNull()
        );
        return mongo.exists(q, ChunksIndexETY.class);
    }

    @Override
    public void markIndexAsRemovable(String id, @Nullable String omit) {
        // Prepare query
        Query q = new Query(
            where(FIELD_IDX_RESOURCE).is(id).and(FIELD_IDX_DELETED_AT).isNull()
        );
        if(omit != null) q.addCriteria(where(FIELD_IDX_VERSION).ne(omit));
        // Set definition
        Update up = new Update();
        up.set(FIELD_IDX_DELETED_AT, new Date());
        // Execute
        mongo.updateMulti(q, up, ChunksIndexETY.class);

    }

    @Override
    public List<ChunksIndexETY> removeIndexes() {
        // Remove chunks after X hours have passed
        Query q = new Query(
            where(FIELD_IDX_DELETED_AT).not().isNullValue().
            andOperator(where(FIELD_IDX_DELETED_AT).lt(getDateOffsetForRemove()))
        );
        // Iterate each document
        List<ChunksIndexETY> documents = mongo.find(q, ChunksIndexETY.class);
        // One by one to lower consumption (cursor)
        for (ChunksIndexETY document : documents) {
            // Chunks related to this index
            List<ObjectId> chunks = document.getChunks();
            // Query to remove chunks
            Query q1 = new Query(
                where(FIELD_IDX_ID).in(chunks)
            );
            // Remove them
            DeleteResult out = mongo.remove(q1, ChunkETY.class);
            // Verify integrity
            if(out.getDeletedCount() != chunks.size()) {
                log.warn(
                    "Mismatch between expected chunks to remove ({}) and chunks removed {} for {}",
                    chunks.size(),
                    out.getDeletedCount(),
                    document.getId()
                );
            }
            // Remove index
            mongo.remove(document);
        }

        return documents;
    }

    private Date getDateOffsetForRemove() {
        return Date.from(Instant.now().minus(REMOVE_AFTER_X_HOURS, HOURS));
    }

}
