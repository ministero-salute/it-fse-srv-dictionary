package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.base;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationException extends RuntimeException {

    /**
	 * Serial version uuid.
	 */
	private static final long serialVersionUID = 80950179850288286L;
	
	private final transient ErrorDTO error;
}
