/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.base.ResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class TerminologyResponseDTO.
 *
 * 
 * 	Terminology Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostDocsResDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5857199886068379718L;

	private Integer insertedItems;

	public PostDocsResDTO(final LogTraceInfoDTO traceInfo, final Integer insertedItems) {
		super(traceInfo);
		this.insertedItems = insertedItems;
	}
	
}
