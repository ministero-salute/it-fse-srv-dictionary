/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes;

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
    private Date lastUpdate;
    /**
     * The response date-time (usually used as the next lastUpdate)
     */
    private Date timestamp;

    private List<HistoryInsertDTO> insertions;

    private List<HistoryDeleteDTO> deletions;

    /**
     * The current active collection items
     */
    private long collectionSize;

}