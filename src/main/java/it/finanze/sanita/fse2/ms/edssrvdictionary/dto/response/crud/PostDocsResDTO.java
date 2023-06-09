/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * The Class TerminologyResponseDTO.
 * 	Terminology Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PostDocsResDTO extends ResponseDTO {

	private String location;
	
	private Integer insertedItems;

	public PostDocsResDTO(final LogTraceInfoDTO traceInfo,final String location, final Integer insertedItems) {
		super(traceInfo);
		this.location = location;
		this.insertedItems = insertedItems;
	}
	
}
