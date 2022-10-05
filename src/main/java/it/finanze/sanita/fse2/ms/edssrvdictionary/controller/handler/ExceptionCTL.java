package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.handler;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createGenericError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createTypeMismatchError;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createDataIntegrityError;



import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionCTL extends ResponseEntityExceptionHandler {

	@Autowired
	private Tracer tracer;
      
	
	/**
     * Handle generic exception.
     * @param ex		exception
     */
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        // Log me
        log.warn("HANDLER handleGenericException()");
        log.error("HANDLER handleGenericException()", ex);
        // Create error DTO
        ErrorResponseDTO out = createGenericError(getLogTraceInfo(), ex);
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out, headers, out.getStatus());
    }
    

/**
 * Handles Possible Mismatch in input fields
 * @param ex  exception to catch
 * 
 */
    @ExceptionHandler(TypeMismatchException.class)
    protected ResponseEntity<ErrorResponseDTO> handleTypeMismatchException(TypeMismatchException ex){
        log.warn("Handler handleTypeMismatchException()");
        log.error("Handler handleTypeMismatchException()",ex);
        // Create error DTO
        ErrorResponseDTO out = createTypeMismatchError(getLogTraceInfo(),ex);
        // set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out,headers,out.getStatus());
    }


    /**
     * Handles Exception thrown when a request violates an integrity Constraint
     * @param ex : DataIntegrityException
     * 
     * 
     */
    @ExceptionHandler(DataIntegrityException.class)
    protected ResponseEntity<ErrorResponseDTO> handleDataIntegrityException(DataIntegrityException ex){
        log.warn("Handler handleDataIntegrityException()");
        log.error("Handler handleDataIntegrityException()",ex);
        // Create error DTO
        ErrorResponseDTO out = createDataIntegrityError(getLogTraceInfo(),ex);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);

        return new ResponseEntity<>(out,headers,out.getStatus());
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