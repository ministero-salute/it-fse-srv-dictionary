/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto;


import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
@Setter
public class VocabularyDTO {

	private String system;

	List<TerminologyFileEntryDTO> entryDTO;
	
}
