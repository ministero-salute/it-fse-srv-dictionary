package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query;

import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
public class HistoryDTO {

    String traceID;
    String spanID;

    Date timestamp;
    Date lastUpdate;

    List<HistoryInsertDTO> insertions;
    List<HistoryDeleteDTO> deletions;

    @Value
    public static class HistoryInsertDTO {
        String id;
        String version;
    }

    @Value
    public static class HistoryDeleteDTO {
        String id;
        String omit;
    }
}
