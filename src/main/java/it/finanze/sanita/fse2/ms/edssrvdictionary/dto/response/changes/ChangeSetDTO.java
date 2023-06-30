/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO.HistoryDeleteDTO;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO.HistoryInsertDTO;

/**
 * DTO for Change Set status endpoint response.
 *
 */
@Data
@JsonInclude(NON_NULL)
public class ChangeSetDTO {

    private static final String PTT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * Trace id log.
     */
    private String traceID;

    /**
     * Span id log.
     */
    private String spanID;

    /**
     * Last update date to consider while retrieving the change set items
     */
    @JsonFormat(pattern = PTT_ISO_8601)
    private Date lastUpdate;
    /**
     * The response date-time (usually used as the next lastUpdate)
     */
    @JsonFormat(pattern = PTT_ISO_8601)
    private Date timestamp;

    private List<HistoryInsertDTO> insertions;

    private List<HistoryDeleteDTO> deletions;

}