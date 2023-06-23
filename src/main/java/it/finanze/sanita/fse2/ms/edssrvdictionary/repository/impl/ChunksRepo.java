package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.impl;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChunksRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class ChunksRepo implements IChunksRepo {

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
    public void markToRemove(String id, @Nullable String omit) {
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

}
