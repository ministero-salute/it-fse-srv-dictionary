package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response;

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
public class TerminologyResponseDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5857199886068379718L;


	public TerminologyResponseDTO() {
		super();
	}


	/**
	 *  DTO used to return log relative to upload terminolpgy files in uploadTerminologyFile
	 * @param traceInfo
	 */
	public TerminologyResponseDTO(final LogTraceInfoDTO traceInfo) {
		super(traceInfo);
	}
	
}
