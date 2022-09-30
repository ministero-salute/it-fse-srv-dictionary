package it.finanze.sanita.fse2.ms.edssrvdictionary.utility;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;

public final class ChangeSetUtility {

	private ChangeSetUtility() {
		
	}

	/**
	 * Creates a ChangesetDTO from a TerminologyETY.
	 */
	public static ChangeSetDTO terminologyToChangeset(TerminologyETY entity) {
		return new ChangeSetDTO(entity.getId(), new ChangeSetDTO.Payload(entity.getSystem(), entity.getCode()));
    }

}
