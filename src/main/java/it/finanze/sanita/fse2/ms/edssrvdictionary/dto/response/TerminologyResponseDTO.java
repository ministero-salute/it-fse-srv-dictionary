package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import javax.validation.constraints.Size;

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

	@Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
	private String transactionId;

	public TerminologyResponseDTO() {
		super();
	}

	public TerminologyResponseDTO(final LogTraceInfoDTO traceInfo, final String inTransactionId) {
		super(traceInfo);
		transactionId = inTransactionId;
	}
	
}
