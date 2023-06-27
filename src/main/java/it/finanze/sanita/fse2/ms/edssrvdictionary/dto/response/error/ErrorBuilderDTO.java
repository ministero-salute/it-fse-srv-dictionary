/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorInstance.IO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorInstance.Resource;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorInstance.Server;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorInstance.Validation;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.MiscUtility;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.*;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorInstance.Server.UNAVAILABLE;
import static org.apache.http.HttpStatus.*;

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

    public static ErrorResponseDTO createEngineInitError(LogTraceInfoDTO trace, EngineInitException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.SERVER.getType(),
            ErrorType.SERVER.getTitle(),
            ex.getMessage(),
            SC_SERVICE_UNAVAILABLE,
            ErrorType.SERVER.toInstance(UNAVAILABLE)
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
