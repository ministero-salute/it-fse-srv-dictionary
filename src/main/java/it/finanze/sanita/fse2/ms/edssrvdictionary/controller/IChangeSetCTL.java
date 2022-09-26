package it.finanze.sanita.fse2.ms.edssrvdictionary.controller;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.ChangeSetResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.validators.NoFutureDate;

/**
 * ChangeSet retriever controller
 *
 * @author Riccardo Bonesi
 */
@RequestMapping(path = "/v1/changeset")
@Tag(name = "Change Set controller")
@Validated
public interface IChangeSetCTL {

	@Operation(summary = "Terminology ChangeSet status", description = "Creazione lista ChangeSet degli Terminology")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Operazione eseguita correttamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChangeSetResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	@GetMapping("/terminology/status")
    Object getTerminologyChangeSet(HttpServletRequest httpServletRequest, @RequestParam(value="lastUpdate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @NoFutureDate Date lastUpdate)
        throws OperationException;

}
