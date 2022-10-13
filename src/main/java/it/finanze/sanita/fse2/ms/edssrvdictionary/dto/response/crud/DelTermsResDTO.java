package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.base.ResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class TerminologyResponseDTO.
 *
 * @author Riccardo Bonesi
 * 
 * 	Terminology Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DelTermsResDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5857199886068379718L;

	public DelTermsResDTO(final LogTraceInfoDTO traceInfo) {
		super(traceInfo);
	}
	
}
