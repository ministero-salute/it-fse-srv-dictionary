package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class TerminologyErrorResponseDTO.
 *
 * @author Riccardo Bonesi
 * 
 * 	Error response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TerminologyErrorResponseDTO extends ErrorResponseDTO {

	@Schema(description = "Identificativo della transazione in errore")
	@Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
	private String transactionId;
	
	public TerminologyErrorResponseDTO(final LogTraceInfoDTO traceInfo, final String inType, final String inTitle, final String inDetail, final Integer inStatus, final String inInstance, final String inTransactionId) {
		super(traceInfo, inType, inTitle, inDetail, inStatus, inInstance);
		transactionId = inTransactionId;
	}

}
