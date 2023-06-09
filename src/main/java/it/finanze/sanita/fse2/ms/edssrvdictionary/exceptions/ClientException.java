package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

	private final ErrorResponseDTO error;
	
	private final Integer statusCode;
	
	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 2940592148957767936L;
 	
	/**
	 * Exception constructor.
	 * 
	 * @param e Exception to be shown.
	 */
	public ClientException(final ErrorResponseDTO error, final Integer statusCode) {
		this.error = error;
		this.statusCode = statusCode;
	}

}