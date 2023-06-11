/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.webscraping;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.WebScrapingDTO;
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
public class WebScrapingPostSingleResDTO extends ResponseDTO {

	private WebScrapingDTO dto;

	public WebScrapingPostSingleResDTO(final LogTraceInfoDTO traceInfo, WebScrapingDTO dto) {
		super(traceInfo);
		this.dto = new WebScrapingDTO(dto.getId(), dto.getSystem(), dto.getUrl(), false);
	}
	
}
