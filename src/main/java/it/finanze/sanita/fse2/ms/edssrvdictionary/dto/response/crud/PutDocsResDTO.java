/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class TerminologyResponseDTO.
 * 	Terminology Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PutDocsResDTO extends ResponseDTO {

	private int updatedItems;

	public PutDocsResDTO(final LogTraceInfoDTO traceInfo, final int updatedItems) {
		super(traceInfo);
		this.updatedItems = updatedItems;
	}
	
}
