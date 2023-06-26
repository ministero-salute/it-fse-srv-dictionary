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
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base; 

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The Class ErrorResponseDTO.
 *
 * 
 * 	Error response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ErrorResponseDTO extends ResponseDTO {

	@Schema(description = "Identificativo del problema verificatosi")
	@Size(min = 0, max = 100)
	private String type;
	
	@Schema(description = "Sintesi del problema (invariante per occorrenze diverse dello stesso problema)")
	@Size(min = 0, max = 1000)
	private String title;

	@Schema(description = "Descrizione del problema")
	@Size(min = 0, max = 1000)
	private String detail;

	@Schema(description = "Stato http")
	@Min(value = 100)
	@Max(value = 599)
	private Integer status;
	
	@Schema(description = "URI che potrebbe fornire ulteriori informazioni riguardo l'occorrenza del problema")
	@Size(min = 0, max = 100)
	private String instance;

	public ErrorResponseDTO(final LogTraceInfoDTO traceInfo, final String inType, final String inTitle, final String inDetail, final Integer inStatus, final String inInstance) {
		super(traceInfo);
		type = inType;
		title = inTitle;
		detail = inDetail;
		status = inStatus;
		instance = inInstance;
	}
	
	public ErrorResponseDTO(final LogTraceInfoDTO traceInfo, final ErrorDTO inError) {
		super(traceInfo);
		type = inError.getType();
		title = inError.getTitle();
		detail = inError.getDetail();
		instance = inError.getInstance();
	}

}