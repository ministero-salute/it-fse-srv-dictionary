package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceMetaDTO;

@Getter
@AllArgsConstructor
public class IndexMetaETY {

    String oid;
    String version;
    String type;

    boolean whitelist;

    public static IndexMetaETY from(ResourceMetaDTO meta) {
        return new IndexMetaETY(
            meta.getOid(),
            meta.getVersion(),
            meta.getType(),
            meta.isWhitelist()
        );
    }

}
