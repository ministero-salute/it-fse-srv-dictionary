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
package it.finanze.sanita.fse2.ms.edssrvdictionary.controller;


import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_VAL_SYSTEM_BLANK;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_DOCUMENTS_TAG;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.RequestDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.DelDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.FormatEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.gtw.dispatcher.dto.response.ValidationResDTO;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Headers.JWT_BUSINESS_HEADER;

/**
 * Terminology controller
 */
@RequestMapping(path = "v1/terminology")
@Tag(name = API_DOCUMENTS_TAG)
public interface ITerminologyCTL {

	@PostMapping(path = "/{format}", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@Operation(summary = "Upload avvenuta con successo", description = "Upload avvenuta con successo.")
	@SecurityRequirements({
		@SecurityRequirement(name = "bearerAuth"),
		@SecurityRequirement(name = JWT_BUSINESS_HEADER)})
	@ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostDocsResDTO.class)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Documenti caricati correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostDocsResDTO.class))),
			@ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "409", description = "System già presente sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	}) 
	@ResponseStatus(HttpStatus.CREATED)
	PostDocsResDTO uploadTerminology(@PathVariable FormatEnum format, 
			@Valid@RequestPart(required = false) RequestDTO creationInfo,
			@RequestPart(name = "file")@Schema(format = "binary") MultipartFile file,HttpServletRequest request) throws OperationException, DocumentAlreadyPresentException, DataProcessingException, InvalidContentException,IOException ;

	@DeleteMapping(value = "/{oid}/{version}",produces = { APPLICATION_JSON_VALUE })
	@Operation(summary = "Cancellazione terminologie attraverso il system",description = "Servizio che consente di cancellare un certo system dalla base dati")
	@SecurityRequirements({
		@SecurityRequirement(name = "bearerAuth"),
		@SecurityRequirement(name = JWT_BUSINESS_HEADER)})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Documenti cancellati correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = DelDocsResDTO.class))),
			@ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "System non presente sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	@ResponseStatus(HttpStatus.OK)
	DelDocsResDTO deleteTerminologies(
			@PathVariable@Parameter(description = "Identificatore del dizionario")@NotBlank(message = ERR_VAL_SYSTEM_BLANK)
			@Schema(description = "oid",  minLength = 0, maxLength = 1000)String oid,
			@PathVariable@Parameter(description = "Identificatore della version del dizionario")@NotBlank(message = ERR_VAL_SYSTEM_BLANK)
			@Schema(description = "version",  minLength = 0, maxLength = 1000)String version,
			HttpServletRequest request) throws OperationException, DocumentNotFoundException, DataIntegrityException, DocumentAlreadyPresentException;
	 
}
