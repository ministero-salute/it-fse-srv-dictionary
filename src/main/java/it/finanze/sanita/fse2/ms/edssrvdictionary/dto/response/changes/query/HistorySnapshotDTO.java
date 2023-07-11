package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(NON_NULL)
public class HistorySnapshotDTO {

    private static final String PTT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private String traceID;
    private String spanID;

    @JsonFormat(pattern = PTT_ISO_8601)
    private Date timestamp;

    private List<Resources> resources;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(NON_NULL)
    public static class Resources {
        String id;
        String version;
        String type;
        Long size;
    }

    public HistorySnapshotDTO trackWith(LogTraceInfoDTO info) {
        this.traceID = info.getTraceID();
        this.spanID = info.getSpanID();
        return this;
    }

}
