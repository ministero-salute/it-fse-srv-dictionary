package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceMetaDTO;

@Getter
@AllArgsConstructor
public class ChunkMetaETY {

    String oid;
    String version;
    String type;

    public static ChunkMetaETY from(ResourceMetaDTO meta) {
        return new ChunkMetaETY(meta.getOid(), meta.getVersion(), meta.getType());
    }

    public static ChunkMetaETY from(IndexMetaETY meta) {
        return new ChunkMetaETY(meta.getOid(), meta.getVersion(), meta.getType());
    }

}
