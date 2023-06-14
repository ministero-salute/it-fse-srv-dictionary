/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.webscraping;

import java.util.List;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.WebScrapingETY;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class TerminologyResponseDTO.
 * 	Terminology Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebScrapingGetResDTO extends ResponseDTO {

	private List<WebScrapingETY> entities;

	public WebScrapingGetResDTO(final LogTraceInfoDTO traceInfo, final List<WebScrapingETY> inEntities) {
		super(traceInfo);
		this.entities = inEntities;
	}

}
