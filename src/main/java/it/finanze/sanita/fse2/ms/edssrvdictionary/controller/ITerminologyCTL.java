/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.controller;


import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_VAL_FUTURE_RELEASE_DATE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_VAL_ID_BLANK;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_VAL_ID_NOT_VALID;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_VAL_SYSTEM_BLANK;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_DOCUMENTS_TAG;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_QP_LIMIT;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_QP_PAGE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_SYSTEM_EXTS;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_TERMS_GET_ONE_BY_ID;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_TERMS_MAPPER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.RequestDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.DelDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.GetDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PutDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.pagination.GetDocsPageResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.FormatEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.validators.NoFutureDate;
import it.finanze.sanita.fse2.ms.edssrvdictionary.validators.ValidObjectId;

/**
 * Terminology controller
 */
@RequestMapping(path = API_TERMS_MAPPER)
@Tag(name = API_DOCUMENTS_TAG)
public interface ITerminologyCTL {

	@PostMapping(path = "/{format}", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@Operation(summary = "Upload avvenuta con successo", description = "Upload avvenuta con successo.")
	@ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Documenti caricati correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostDocsResDTO.class))),
			@ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "409", description = "System già presente sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	}) 
	PostDocsResDTO uploadTerminology(@PathVariable FormatEnum format, @Valid@RequestPart RequestDTO creationInfo,
			@RequestPart(name = "file") MultipartFile file,HttpServletRequest request) throws OperationException, DocumentAlreadyPresentException, DataProcessingException, InvalidContentException,IOException ;

	@DeleteMapping(value = "/{oid}/{version}",produces = { APPLICATION_JSON_VALUE })
	@Operation(summary = "Cancellazione terminologie attraverso il system",description = "Servizio che consente di cancellare un certo system dalla base dati")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Documenti cancellati correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = DelDocsResDTO.class))),
			@ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "System non presente sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	DelDocsResDTO deleteTerminologies(
			@PathVariable@Parameter(description = "Identificatore del dizionario")@NotBlank(message = ERR_VAL_SYSTEM_BLANK)String oid,
			@PathVariable@Parameter(description = "Identificatore della version del dizionario")@NotBlank(message = ERR_VAL_SYSTEM_BLANK)String version) throws OperationException, DocumentNotFoundException, DataIntegrityException, DocumentAlreadyPresentException;
	


	@GetMapping(
			value = API_TERMS_GET_ONE_BY_ID,
			produces = { APPLICATION_JSON_VALUE }
			)
	@Operation(
			summary = "Restituisce un terminology dato il suo identificativo",
			description = "Servizio che consente di ritornare una terminologia dato il suo identificativo."
			)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Documento presente sul database", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetDocsResDTO.class))),
			@ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Documento non trovato sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	GetDocsResDTO getTerminologyById(
			@PathVariable
			@Parameter(description = "Identificatore documento")
			@NotBlank(message = ERR_VAL_ID_BLANK)
			@ValidObjectId(message = ERR_VAL_ID_NOT_VALID)
			String id
			) throws OperationException, DocumentNotFoundException;


	@GetMapping(
			value = API_SYSTEM_EXTS,
			produces = { APPLICATION_JSON_VALUE }
			)
	@Operation(
			summary = "Restituisce le terminologie appartenenti al system richiesto con paginazione",
			description = "Servizio che restituisce tutte le terminologie appartenenti ad un certo system con paginazione"
			)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Documenti restituite correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetDocsPageResDTO.class))),
			@ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "System non trovato sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	GetDocsPageResDTO getTerminologies(
			@PathVariable
			@Parameter(description = "Identificatore del dizionario")
			@NotBlank(message = ERR_VAL_SYSTEM_BLANK)
			String system,
			@RequestParam(API_QP_PAGE)
			@Parameter(description = "Indice pagina richiesto (eg. 0, 1, 2...)")
			int page,
			@RequestParam(API_QP_LIMIT)
			@Parameter(description = "Limite documenti per pagina (eg. 10, 20 ...)")
			int limit
			) throws OperationException, DocumentNotFoundException, OutOfRangeException;

	@PutMapping(
			produces = { APPLICATION_JSON_VALUE },
			consumes = { MULTIPART_FORM_DATA_VALUE }
			)
	@Operation(
			summary = "Modifica terminologie attraverso un file CSV",
			description = "Servizio che consente di aggiungere una nuova versione per terminologie già presenti sulla base dati caricando un file csv."
			)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Documenti caricati correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = PutDocsResDTO.class))),
			@ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "System non presente sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "409", description = "Version già presente sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	@ResponseStatus(HttpStatus.CREATED)
	PutDocsResDTO updateTerminologies(
			@RequestPart
			@Parameter(description = "CSV contenente le terminologie da inserire (e.g 2.16.840.1.113883.1.11.1.csv)")
			MultipartFile file,
			@Parameter(description = "Versione del dizionario incrementata rispetto alla precedente")
			String version,
			@Parameter(description = "Data di rilascio del dizionario incrementata rispetto alla precedente")
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			@NoFutureDate(message = ERR_VAL_FUTURE_RELEASE_DATE)
			Date releaseDate
			) throws OperationException, DocumentNotFoundException, DataProcessingException, DataIntegrityException, DocumentAlreadyPresentException, InvalidContentException;

}
