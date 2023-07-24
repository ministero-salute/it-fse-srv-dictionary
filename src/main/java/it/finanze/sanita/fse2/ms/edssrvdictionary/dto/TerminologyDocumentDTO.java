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
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.MiscUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ChangeSetChunkDTO.PTT_ISO_8601;
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
	@JsonFormat(pattern = PTT_ISO_8601)
	private OffsetDateTime releaseDate;
	@JsonFormat(pattern = PTT_ISO_8601)
	private OffsetDateTime insertionDate;
	@JsonFormat(pattern = PTT_ISO_8601)
	private OffsetDateTime lastUpdateDate;

	public static TerminologyDocumentDTO fromEntity(TerminologyETY e) {
		return new TerminologyDocumentDTO(
				e.getId(),
				e.getSystem(),
				e.getVersion(),
				e.getCode(),
				e.getDescription(),
				MiscUtility.convertToOffsetDateTime(e.getReleaseDate()),
				MiscUtility.convertToOffsetDateTime(e.getInsertionDate()),
				MiscUtility.convertToOffsetDateTime(e.getLastUpdateDate()));
	}

}
