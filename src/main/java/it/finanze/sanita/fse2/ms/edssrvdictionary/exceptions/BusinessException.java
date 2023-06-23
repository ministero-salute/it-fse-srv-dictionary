package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.enums.ErrorClassEnum.GENERIC;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.base.GenericException;


public class BusinessException extends GenericException {
    
    /**
	 * Serial verison uid.
	 */
	private static final long serialVersionUID = 5238920610659124236L;

	public BusinessException(String msg) {
        super(new ErrorDTO(GENERIC.getType(), GENERIC.getTitle(), msg, GENERIC.getInstance()));
    }
    
}
