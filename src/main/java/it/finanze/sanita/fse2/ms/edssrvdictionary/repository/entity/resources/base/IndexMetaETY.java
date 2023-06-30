package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceMetaDTO;

@Getter
@AllArgsConstructor
public class IndexMetaETY {

    private static final String PTT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    String oid;
    String version;
    String type;
    @JsonFormat(pattern = PTT_ISO_8601)
    Date released;
    boolean whitelist;

    public static IndexMetaETY from(ResourceMetaDTO meta) {
        return new IndexMetaETY(
            meta.getOid(),
            meta.getVersion(),
            meta.getType(),
            meta.getReleased(),
            meta.isWhitelist()
        );
    }

}
