package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
public class HistoryDTO {

    private static final String PTT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    String traceID;
    String spanID;

    @JsonFormat(pattern = PTT_ISO_8601)
    Date timestamp;

    @JsonFormat(pattern = PTT_ISO_8601)
    Date lastUpdate;

    List<HistoryInsertDTO> insertions;
    List<HistoryDeleteDTO> deletions;

    @Data
    public static class HistoryInsertDTO {
        String id;
        String version;
        String type;
    }

    @Data
    @JsonInclude(NON_NULL)
    public static class HistoryDeleteDTO {
        String id;
        String type;
        String omit;
    }
}
