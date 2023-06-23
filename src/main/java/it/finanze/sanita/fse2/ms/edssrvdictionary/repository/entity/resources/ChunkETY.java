package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.chunks.PartialChunkDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.ResChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.ResMetaETY;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceMetaDTO;

@Document(collection = "#{@chunkBean}")
@Data
public class ChunkETY {

    @Id
    private ObjectId id;
    private ResMetaETY meta;
    private ResChunkETY chunk;

    public ChunkETY(ResMetaETY meta, ResChunkETY chunk) {
        this.meta = meta;
        this.chunk = chunk;
    }

    public static ChunkETY from(ResourceMetaDTO meta, PartialChunkDTO chunk) {
        return new ChunkETY(
            ResMetaETY.from(meta),
            ResChunkETY.from(chunk)
        );
    }

}
