package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.enums.ErrorClassEnum.VALIDATION_WRONG_TOKEN;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.base.ValidationException;

public class TokenException  extends ValidationException {
    
    /**
	 * Serial verison uid.
	 */
	private static final long serialVersionUID = 5238920610659124236L;

	public TokenException(String msg) {
        super(new ErrorDTO(VALIDATION_WRONG_TOKEN.getType(), VALIDATION_WRONG_TOKEN.getTitle(), msg, VALIDATION_WRONG_TOKEN.getInstance()));
    }
    
}
