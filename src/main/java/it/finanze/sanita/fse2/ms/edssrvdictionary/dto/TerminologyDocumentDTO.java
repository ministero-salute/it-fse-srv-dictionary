/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

import java.io.Serializable;
import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.MiscUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerminologyDocumentDTO implements Serializable {

	/**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = -7764465063914528589L; 
	
	
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String id;
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String system;
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String version;
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String code;
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String description;
	private OffsetDateTime releaseDate;
	private OffsetDateTime lastUpdateDate;

	public static TerminologyDocumentDTO fromEntity(TerminologyETY e) {
		return new TerminologyDocumentDTO(
				e.getId(),
				e.getSystem(),
				e.getVersion(),
				e.getCode(),
				e.getDescription(),
				MiscUtility.convertToOffsetDateTime(e.getReleaseDate()),
				MiscUtility.convertToOffsetDateTime(e.getLastUpdateDate()));
	}

}
