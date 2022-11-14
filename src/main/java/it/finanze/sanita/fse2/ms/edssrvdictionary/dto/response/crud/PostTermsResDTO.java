/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.LogTraceInfoDTO;
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
public class PostTermsResDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5857199886068379718L;

	private Integer insertedTerminology;

	public PostTermsResDTO(final LogTraceInfoDTO traceInfo, final Integer inInsertedTerminology) {
		super(traceInfo);
		insertedTerminology = inInsertedTerminology;
	}
	
}
