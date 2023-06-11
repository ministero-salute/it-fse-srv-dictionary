/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.webscraping;

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
public class WebscrapingPostMultiResDTO extends ResponseDTO {

	
	private Integer insertedItems;

	public WebscrapingPostMultiResDTO(final LogTraceInfoDTO traceInfo, final Integer insertedItems) {
		super(traceInfo);
		this.insertedItems = insertedItems;
	}
	
}
