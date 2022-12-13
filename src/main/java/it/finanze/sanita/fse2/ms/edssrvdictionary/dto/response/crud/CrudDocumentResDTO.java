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
 * 
 * 	Terminology Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CrudDocumentResDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6294233830475183381L;
	
	private Integer insertedItems;
	private Integer updatedItems;
	private Integer deletedItems;

	public CrudDocumentResDTO() {
		super();
	}

	public CrudDocumentResDTO(final LogTraceInfoDTO traceInfo, Integer insertedItems, Integer updatedItems, Integer deletedItems) {
		super(traceInfo);
		this.insertedItems = insertedItems;
		this.updatedItems = updatedItems;
		this.deletedItems = deletedItems;
	}
}
