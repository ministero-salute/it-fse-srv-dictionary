/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.handler;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createArgumentMismatchError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createConstraintError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createDataIntegrityError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createDataProcessingError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createDocumentAlreadyPresentError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createDocumentNotFoundError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createGenericError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createInvalidContentError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createMissingParameterError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createMissingPartError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createOperationError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createOutOfRangeError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.enums.ErrorClassEnum.TIMEOUT;

import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.ClientException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.FileExtensionValidationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.RequestValidationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.TokenException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.base.ValidationException;
import lombok.extern.slf4j.Slf4j;

/**
 *	Exceptions handler
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Tracker log.
	 */
	@Autowired
	private Tracer tracer;

	/**
	 * Handles any document not found due to invalid references.
	 *
	 * @param ex exception
	 */
	@ExceptionHandler(DocumentNotFoundException.class)
	protected ResponseEntity<ErrorResponseDTO> handleDocumentNotFoundException(DocumentNotFoundException ex) {
		// Log me
		log.error("HANDLER handleDocumentNotFoundException()", ex);
		// Create error DTO
		ErrorResponseDTO out = createDocumentNotFoundError(getLogTraceInfo(), ex);
		// Set HTTP headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		// Bye bye
		return new ResponseEntity<>(out, headers, out.getStatus());
	}

	@ExceptionHandler(InvalidContentException.class)
	protected ResponseEntity<ErrorResponseDTO> handleInvalidContentException(InvalidContentException ex) {
		// Log me
		log.error("HANDLER handleInvalidContentException()", ex);
		// Create error DTO
		ErrorResponseDTO out = createInvalidContentError(getLogTraceInfo(), ex);
		// Set HTTP headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		return new ResponseEntity<>(out, headers, out.getStatus());
	}

	/**
	 * Handles any document conflict.
	 *
	 * @param ex exception
	 */
	@ExceptionHandler(DocumentAlreadyPresentException.class)
	protected ResponseEntity<ErrorResponseDTO> handleDocumentAlreadyPresentException(DocumentAlreadyPresentException ex) {
		// Log me
		log.error("HANDLER handleDocumentAlreadyPresentException()", ex);
		// Create error DTO
		ErrorResponseDTO out = createDocumentAlreadyPresentError(getLogTraceInfo(), ex);
		// Set HTTP headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		// Bye bye
		return new ResponseEntity<>(out, headers, out.getStatus());
	}

	/**
	 * Handles an invalid index reference
	 *
	 * @param ex exception
	 */
	@ExceptionHandler(OutOfRangeException.class)
	protected ResponseEntity<ErrorResponseDTO> handleOutOfRangeException(OutOfRangeException ex) {
		// Log me
		log.error("HANDLER handleOutOfRangeException()", ex);
		// Create error DTO
		ErrorResponseDTO out = createOutOfRangeError(getLogTraceInfo(), ex);
		// Set HTTP headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		// Bye bye
		return new ResponseEntity<>(out, headers, out.getStatus());
	}

	/**
	 * Handles exceptions thrown by the database when
	 * the number of expected operations does not match
	 * with the ones executed.
	 *
	 * @param ex exception
	 */
	@ExceptionHandler(DataIntegrityException.class)
	protected ResponseEntity<ErrorResponseDTO> handleDataIntegrityException(DataIntegrityException ex) {
		// Log me
		log.error("HANDLER DataIntegrityException()", ex);
		// Create error DTO
		ErrorResponseDTO out = createDataIntegrityError(getLogTraceInfo(), ex);
		// Set HTTP headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		// Bye bye
		return new ResponseEntity<>(out, headers, out.getStatus());
	}

	/**
	 * Handles exceptions thrown by the data-layer. (e.g database write/read errors)
	 *
	 * @param ex exception
	 */
	@ExceptionHandler(OperationException.class)
	protected ResponseEntity<ErrorResponseDTO> handleOperationException(OperationException ex) {
		// Log me
		log.error("HANDLER handleOperationException()", ex);
		// Create error DTO
		ErrorResponseDTO out = createOperationError(getLogTraceInfo(), ex);
		// Set HTTP headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		// Bye bye
		return new ResponseEntity<>(out, headers, out.getStatus());
	}

	/**
	 * Handles exceptions generated by the conversion into bytes of the data passed by the user.
	 * (e.g. UTF-8 is required but user provided ISO-8859)
	 *
	 * @param ex exception
	 */
	@ExceptionHandler(DataProcessingException.class)
	protected ResponseEntity<ErrorResponseDTO> handleDataProcessingException(DataProcessingException ex) {
		// Log me
		log.error("HANDLER handleDataProcessingException()", ex);
		// Create error DTO
		ErrorResponseDTO out = createDataProcessingError(getLogTraceInfo(), ex);
		// Set HTTP headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		// Bye bye
		return new ResponseEntity<>(out, headers, out.getStatus());
	}



	/**
	 * Handles exceptions thrown by the absence of one or more required parts of a request.
	 *
	 * @param ex exception
	 */
	@NonNull
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(@NonNull MissingServletRequestPartException ex, HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
		// Log me
		log.error("HANDLER handleMissingServletRequestPart()", ex);
		// Create error DTO
		ErrorResponseDTO out = createMissingPartError(getLogTraceInfo(), ex);
		// Set HTTP headers
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		// Bye bye
		return new ResponseEntity<>(out, headers, out.getStatus());
	}

	/**
	 * Handles exceptions thrown by the absence of one or more required parameter of a request.
	 *
	 * @param ex exception
	 */
	@NonNull
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(@NonNull MissingServletRequestParameterException ex, HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
		// Log me
		log.error("HANDLER handleMissingServletRequestParameter()", ex);
		// Create error DTO
		ErrorResponseDTO out = createMissingParameterError(getLogTraceInfo(), ex);
		// Set HTTP headers
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		// Bye bye
		return new ResponseEntity<>(out, headers, out.getStatus());
	}

	/**
	 * Handles exceptions thrown by the validation check performed on the request submitted by the user.
	 *
	 * @param ex exception
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(ConstraintViolationException ex) {
		// Log me
		log.error("HANDLER handleConstraintViolationException()", ex);
		// Create error DTO
		ErrorResponseDTO out = createConstraintError(getLogTraceInfo(), ex);
		// Set HTTP headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		// Bye bye
		return new ResponseEntity<>(out, headers, out.getStatus());
	}

	/**
	 * Handles exceptions thrown by the inability to convert a certain value from a type X to a type Y.
	 * (e.g. {@link String} to {@link Date})
	 *
	 * @param ex exception
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		// Log me
		log.error("HANDLER MethodArgumentTypeMismatchException()", ex);
		// Create error DTO
		ErrorResponseDTO out = createArgumentMismatchError(getLogTraceInfo(), ex);
		// Set HTTP headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		// Bye bye
		return new ResponseEntity<>(out, headers, out.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders hheaders, HttpStatus status, WebRequest request) {
		ErrorResponseDTO out = createArgumentMismatchError(getLogTraceInfo(), ex);
		out.setStatus(HttpStatus.BAD_REQUEST.value());
		// Set HTTP headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);

		return new ResponseEntity<>(out, headers, HttpStatus.BAD_REQUEST);

	}

	/**
	 * Handles generic or unknown exceptions, unexpected thrown during the execution of any operation.
	 *
	 * @param ex exception
	 */
	@ExceptionHandler(value = {ClientException.class})
	protected ResponseEntity<ErrorResponseDTO> handleResourceAccessException(ClientException ex) {
		log.error("HANDLER handleResourceAccessException()", ex);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		return new ResponseEntity<>(ex.getError(), headers, ex.getStatusCode());
	}
	
	/**
	 * Handles generic or unknown exceptions, unexpected thrown during the execution of any operation.
	 *
	 * @param ex exception
	 */
	@ExceptionHandler(value = {ResourceAccessException.class})
	protected ResponseEntity<ErrorResponseDTO> handleResourceAccessException(ResourceAccessException ex) {
		log.error("HANDLER handleResourceAccessException()", ex);
		ErrorResponseDTO out = new ErrorResponseDTO(getLogTraceInfo(), TIMEOUT.getType(), TIMEOUT.getTitle(), TIMEOUT.getDetail(), 504, TIMEOUT.getInstance());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		return new ResponseEntity<>(out, headers, out.getStatus());
	}

	@ExceptionHandler(value = {FileExtensionValidationException.class,RequestValidationException.class, TokenException.class})
	protected ResponseEntity<ErrorResponseDTO> handleBadRequestException(final ValidationException ex, final WebRequest request) {

		LogTraceInfoDTO traceInfo = getLogTraceInfo();
		ErrorResponseDTO response = new ErrorResponseDTO(traceInfo, ex.getError());
		response.setStatus(HttpStatus.BAD_REQUEST.value());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Handles generic or unknown exceptions, unexpected thrown during the execution of any operation.
	 *
	 * @param ex exception
	 */
	@ExceptionHandler(value = {Exception.class})
	protected ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
		// Log me
		log.error("HANDLER handleGenericException()", ex);
		// Create error DTO
		ErrorResponseDTO out = createGenericError(getLogTraceInfo(), ex);
		out.setStatus(out.getStatus());
		// Set HTTP headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		// Bye bye
		return new ResponseEntity<>(out, headers, out.getStatus());
	}

	/**
	 * Generate a new {@link LogTraceInfoDTO} instance
	 * @return The new instance
	 */
	private LogTraceInfoDTO getLogTraceInfo() {
		// Create instance
		LogTraceInfoDTO out = new LogTraceInfoDTO(null, null);
		// Verify if context is available
		if (tracer.currentSpan() != null) {
			out = new LogTraceInfoDTO(
					tracer.currentSpan().context().spanIdString(),
					tracer.currentSpan().context().traceIdString());
		}
		// Return the log trace
		return out;
	}
}
