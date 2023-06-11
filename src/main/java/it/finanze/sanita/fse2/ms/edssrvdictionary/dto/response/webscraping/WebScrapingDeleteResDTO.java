/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.webscraping;

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
public class WebScrapingDeleteResDTO extends ResponseDTO {

	private int deletedItems;

	public WebScrapingDeleteResDTO(final LogTraceInfoDTO traceInfo, final int deletedItems) {
		super(traceInfo);
		this.deletedItems = deletedItems;
	}
	
}
