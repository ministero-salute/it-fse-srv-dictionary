package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.chunks.PartialChunkDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.ChunkMetaETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.ResChunkETY;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceMetaDTO;

@Document(collection = "#{@chunkBean}")
@Data
public class ChunkETY {

    public static final String FIELD_CK_INSERTED_AT = "inserted_at";

    @Id
    private ObjectId id;
    private ChunkMetaETY meta;
    private ResChunkETY chunk;
    @Field(FIELD_CK_INSERTED_AT)
    private Date insertedAt;

    public ChunkETY(ChunkMetaETY meta, ResChunkETY chunk) {
        this.meta = meta;
        this.chunk = chunk;
        this.insertedAt = new Date();
    }

    public static ChunkETY from(ResourceMetaDTO meta, PartialChunkDTO chunk) {
        return new ChunkETY(
            ChunkMetaETY.from(meta),
            ResChunkETY.from(chunk)
        );
    }

    public static ChunkETY empty() {
        return new ChunkETY(
            null,
            new ResChunkETY(null, 0, new ArrayList<>(), 1)
        );
    }

}
