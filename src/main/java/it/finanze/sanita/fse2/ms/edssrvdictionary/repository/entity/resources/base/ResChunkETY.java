package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceItemDTO;

@Getter
@AllArgsConstructor
public class ResChunkETY {
    private ObjectId root;
    private long idx;
    private List<ResourceItemDTO> values;
    private long size;
}
