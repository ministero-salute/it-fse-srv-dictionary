package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources;

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.ResChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.ResMetaETY;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "#{@chunksIndexBean}")
@Data
public class ChunkETY {

    @Id
    private ObjectId id;
    private ResMetaETY meta;
    private ResChunkETY chunk;

}
