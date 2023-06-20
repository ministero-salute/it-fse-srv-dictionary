package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.enums.ErrorClassEnum.VALIDATION_REQUEST;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.base.ValidationException;

public class RequestValidationException extends ValidationException {
    
    /**
	 * Serial verison uid.
	 */
	private static final long serialVersionUID = 5238920610659124236L;

	public RequestValidationException(String msg) {
        super(new ErrorDTO(VALIDATION_REQUEST.getType(), VALIDATION_REQUEST.getTitle(), msg, VALIDATION_REQUEST.getInstance()));
    }
    
}
