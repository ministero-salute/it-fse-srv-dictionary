package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query;

import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
public class HistoryResourceDTO {

    String traceID;
    String spanID;

    String id;
    String version;
    ResourceMetaDTO meta;
    List<ResourceItemDTO> items;

    @Value
    public static class ResourceMetaDTO {
        String resourceId;
        String versionId;
        String resourceType;
        Date releaseDate;
    }

    @Value
    public static class ResourceItemDTO {
        String code;
        String display;
    }
}
