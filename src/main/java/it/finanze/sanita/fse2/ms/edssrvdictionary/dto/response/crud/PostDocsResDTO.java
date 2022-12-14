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
public class PostDocsResDTO extends ResponseDTO {

	private int insertedItems;

	public PostDocsResDTO(final LogTraceInfoDTO traceInfo, final int insertedItems) {
		super(traceInfo);
		this.insertedItems = insertedItems;
	}
	
}
