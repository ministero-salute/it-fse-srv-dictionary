package it.finanze.sanita.fse2.ms.edssrvdictionary.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.MetadataResourceResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;

public interface ISchedulerCTL {

	@PostMapping("/run-scheduler")
	@Operation(summary = "Run scheduler", description = "Run scheduler.")
	@ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = MetadataResourceResponseDTO.class)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Garbage startato", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = void.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)) })
	MetadataResourceResponseDTO runScheduler(HttpServletRequest request) throws OperationException;
	
}
