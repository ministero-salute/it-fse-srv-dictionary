package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class HistoryResourceDTO {

    private static final String PTT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    String traceID;
    String spanID;

    String id;
    String version;
    ResourceMetaDTO meta;
    List<ResourceItemDTO> items;

    @Data
    @JsonInclude(NON_NULL)
    public static class ResourceMetaDTO {
        String resourceId;
        String versionId;
        String resourceType;
        @JsonFormat(pattern = PTT_ISO_8601)
        Date releaseDate;
    }

    @Data
    @JsonInclude(NON_NULL)
    public static class ResourceItemDTO {
        String code;
        String display;
    }

}
