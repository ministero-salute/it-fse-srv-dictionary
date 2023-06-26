/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_VAL_MISSING_PARAMETER;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_VAL_MISSING_PART;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_VAL_UNABLE_CONVERT;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_UNPROCESSABLE_ENTITY;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorInstance.IO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorInstance.Resource;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorInstance.Server;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorInstance.Validation;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.MiscUtility;

/**
 * Builder class converting a given {@link Exception} into its own {@link ErrorResponseDTO} representation
 *
 */
public final class ErrorBuilderDTO {

    /**
     * Private constructor to disallow to access from other classes
     */
    private ErrorBuilderDTO() {}

    public static ErrorResponseDTO createConstraintError(LogTraceInfoDTO trace, ConstraintViolationException ex) {
        // Retrieve the first constraint error
        ConstraintViolation<?> violation = ex.getConstraintViolations().iterator().next();
        String field = MiscUtility.extractKeyFromPath(violation.getPropertyPath());
        // Return associated information
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            violation.getMessage(),
            SC_BAD_REQUEST,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, field)
        );
    }

    public static ErrorResponseDTO createArgumentMismatchError(LogTraceInfoDTO trace, MethodArgumentTypeMismatchException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            String.format(
                ERR_VAL_UNABLE_CONVERT,
                ex.getName(),
                ex.getParameter().getParameter().getType().getSimpleName()
            ),
            SC_BAD_REQUEST,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, ex.getName())
        );
    }
    
    public static ErrorResponseDTO createArgumentMismatchError(LogTraceInfoDTO trace, MethodArgumentNotValidException ex) {
    	FieldError err = ex.getBindingResult().getFieldError();
		String message = "Errore generico "; 
		if (err != null ) {
			message = err.getDefaultMessage();
		}

		return new ErrorResponseDTO(
	            trace,
	            ErrorType.VALIDATION.getType(),
	            ErrorType.VALIDATION.getTitle(),
	            message,
	            SC_BAD_REQUEST,
	            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, "")
	        );
    }

    public static ErrorResponseDTO createInvalidContentError(LogTraceInfoDTO trace, InvalidContentException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            ex.getMessage(),
            SC_BAD_REQUEST,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, ex.getField())
        );

    }

    public static ErrorResponseDTO createMissingPartError(LogTraceInfoDTO trace, MissingServletRequestPartException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            String.format(ERR_VAL_MISSING_PART, ex.getRequestPartName()),
            SC_BAD_REQUEST,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, ex.getRequestPartName())
        );
    }

    public static ErrorResponseDTO createOutOfRangeError(LogTraceInfoDTO trace, OutOfRangeException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            ex.getMessage(),
            SC_BAD_REQUEST,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, ex.getField())
        );
    }

    public static ErrorResponseDTO createMissingParameterError(LogTraceInfoDTO trace, MissingServletRequestParameterException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            String.format(ERR_VAL_MISSING_PARAMETER, ex.getParameterName()),
            SC_BAD_REQUEST,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, ex.getParameterName())
        );
    }

    public static ErrorResponseDTO createGenericError(LogTraceInfoDTO trace, Exception ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.SERVER.getType(),
            ErrorType.SERVER.getTitle(),
            ex.getMessage(),
            SC_INTERNAL_SERVER_ERROR,
            ErrorType.SERVER.toInstance(Server.INTERNAL)
        );
    }

    public static ErrorResponseDTO createOperationError(LogTraceInfoDTO trace, OperationException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.SERVER.getType(),
            ErrorType.SERVER.getTitle(),
            ex.getMessage(),
            SC_INTERNAL_SERVER_ERROR,
            ErrorType.SERVER.toInstance(Server.INTERNAL)
        );
    }

    public static ErrorResponseDTO createDocumentAlreadyPresentError(LogTraceInfoDTO trace, DocumentAlreadyPresentException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.RESOURCE.getType(),
            ErrorType.RESOURCE.getTitle(),
            ex.getMessage(),
            SC_CONFLICT,
            ErrorType.RESOURCE.toInstance(Resource.CONFLICT)
        );
    }

    public static ErrorResponseDTO createDocumentNotFoundError(LogTraceInfoDTO trace, DocumentNotFoundException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.RESOURCE.getType(),
            ErrorType.RESOURCE.getTitle(),
            ex.getMessage(),
            SC_NOT_FOUND,
            ErrorType.RESOURCE.toInstance(Resource.NOT_FOUND)
        );
    }

    public static ErrorResponseDTO createDataProcessingError(LogTraceInfoDTO trace, DataProcessingException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.IO.getType(),
            ErrorType.IO.getTitle(),
            ex.getMessage(),
            SC_UNPROCESSABLE_ENTITY,
            ErrorType.IO.toInstance(IO.CONVERSION)
        );
    }

    public static ErrorResponseDTO createDataIntegrityError(LogTraceInfoDTO trace, DataIntegrityException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.IO.getType(),
            ErrorType.IO.getTitle(),
            ex.getMessage(),
            SC_INTERNAL_SERVER_ERROR,
            ErrorType.IO.toInstance(IO.INTEGRITY)
        );
    }

}
