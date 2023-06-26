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
package it.finanze.sanita.fse2.ms.edssrvdictionary.utility;

import com.google.common.collect.Lists;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.ChunksETY;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

public final class ChangeSetUtility {

	public static final int CHUNKS_SIZE = 10000;

	private ChangeSetUtility() {
		// This method is intentionally left blank.
	}

	/**
	 * Creates a ChangesetDTO from a TerminologyETY.
	 */
	public static ChangeSetDTO toChangeset(TerminologyETY entity) {
		return new ChangeSetDTO(entity.getId(), new ChangeSetDTO.Payload(entity.getSystem(), entity.getCode()));
    }

	public static List<ObjectId> extractIds(List<ChangeSetDTO> items) {
		return items.stream().map(ChangeSetDTO::getId).map(ObjectId::new).collect(Collectors.toList());
	}

	public static ChunksETY chunks(List<ChangeSetDTO> items, int size) {
		// Working var
		ChunksETY chunk = null;
		List<List<ObjectId>> chunks;
		// Transform to ids
		List<ObjectId> ids = extractIds(items);
		// Check emptiness
		if(!ids.isEmpty()) {
			// Get item size
			chunks = Lists.partition(ids, size);
			// Create chunk
			chunk = new ChunksETY(chunks, chunks.size(), size, ids.size());
		}
		return ids.isEmpty() ? ChunksETY.empty() : chunk;
	}

}
