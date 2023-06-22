package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources;

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.ResMetaETY;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "#{@chunksIndexBean}")
@Data
public class ChunksIndexETY {

    @Id
    private ObjectId id;
    private String resource;
    private String version;
    private ResMetaETY meta;
    private List<ObjectId> chunks;
    private long size;
    private boolean deleted;

}
