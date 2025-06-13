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
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.data.snapshot;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.ChunksETY;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * The changeset chunk DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChunksDTO {

    public static final String EMPTY_ID = null;

    @Schema(maxLength = 1000)
    String snapshotID;
    Chunk insertions;
    Chunk deletions;

    public static ChunksDTO empty() {
        return new ChunksDTO(EMPTY_ID, Chunk.empty(), Chunk.empty());
    }

    @Value
    public static class Chunk {
        /**
         * The chunks available from the snapshot
         */
        @Schema(format = "int32")
        @Min(0)
        @Max(Integer.MAX_VALUE)
        int chunksCount;
        /**
         * The number of entries each chunk is made of
         */
        @Schema(format = "int32")
        @Min(0)
        @Max(Integer.MAX_VALUE)
        int chunksAvgSize;
        /**
         * The amount of items to elaborate
         */
        @Schema(format = "int32")
        @Min(0)
        @Max(Integer.MAX_VALUE)
        int chunksItems;

        public Chunk(ChunksETY chunk) {
            this.chunksCount = chunk.getCount();
            this.chunksAvgSize = chunk.getAvgSize();
            this.chunksItems = chunk.getItems();
        }

        public Chunk(int count, int size, int items) {
            this.chunksCount = count;
            this.chunksAvgSize = size;
            this.chunksItems = items;
        }

        /**
         * An empty instance
         */
        public static Chunk empty() {
            return new Chunk(0,0,0);
        }
    }
}
