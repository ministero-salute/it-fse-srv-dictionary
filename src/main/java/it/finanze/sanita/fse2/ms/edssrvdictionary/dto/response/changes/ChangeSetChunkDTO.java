/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
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
