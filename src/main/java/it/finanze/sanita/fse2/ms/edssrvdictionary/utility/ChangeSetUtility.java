/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
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
