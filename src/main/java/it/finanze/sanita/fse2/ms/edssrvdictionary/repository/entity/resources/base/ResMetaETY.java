package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceMetaDTO;

@Getter
@AllArgsConstructor
public class ResMetaETY {
    String oid;
    String version;
    String type;

    public static ResMetaETY from(ResourceMetaDTO meta) {
        return new ResMetaETY(meta.getOid(), meta.getVersion(), meta.getType());
    }

}
