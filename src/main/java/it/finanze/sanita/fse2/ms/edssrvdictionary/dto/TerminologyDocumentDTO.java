/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
	private OffsetDateTime insertionDate;
	private OffsetDateTime lastUpdateDate;

}
