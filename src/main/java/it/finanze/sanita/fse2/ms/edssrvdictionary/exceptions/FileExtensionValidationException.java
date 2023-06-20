package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.enums.ErrorClassEnum.VALIDATION_WRONG_EXTENSION;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.base.ValidationException;


public class FileExtensionValidationException extends ValidationException {
    
    /**
	 * Serial verison uid.
	 */
	private static final long serialVersionUID = 5238920610659124236L;

	public FileExtensionValidationException(String msg) {
        super(new ErrorDTO(VALIDATION_WRONG_EXTENSION.getType(), VALIDATION_WRONG_EXTENSION.getTitle(), msg, VALIDATION_WRONG_EXTENSION.getInstance()));
    }
    
}
