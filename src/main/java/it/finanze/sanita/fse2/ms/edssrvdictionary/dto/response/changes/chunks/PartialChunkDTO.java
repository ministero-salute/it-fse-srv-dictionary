package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.chunks;

import lombok.Value;
import org.bson.types.ObjectId;

import java.util.List;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceItemDTO;

@Value
public class PartialChunkDTO {
    ObjectId root;
    int idx;
    List<ResourceItemDTO> items;
}
