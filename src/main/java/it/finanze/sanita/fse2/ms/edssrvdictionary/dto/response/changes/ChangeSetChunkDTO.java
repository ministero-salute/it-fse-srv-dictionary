/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.data.snapshot.ChunksDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * DTO for Change Set status endpoint response.
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSetChunkDTO {

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

    /**
     * The chunks descriptor
     */
    private ChunksDTO chunks;

    /**
     * The total number of items returned (inserted/modified/deleted)
     */
    private long totalNumberOfElements;

    /**
     * The current active collection items
     */
    private long collectionSize;
}