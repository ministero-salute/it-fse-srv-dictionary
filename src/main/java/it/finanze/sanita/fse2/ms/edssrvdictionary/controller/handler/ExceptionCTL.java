package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.handler;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorBuilderDTO.createGenericError;

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
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionCTL extends ResponseEntityExceptionHandler {

	@Autowired
	private Tracer tracer;
      
	
	/**
     * Handle generic exception.
     *
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