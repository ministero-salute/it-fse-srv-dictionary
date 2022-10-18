package it.finanze.sanita.fse2.ms.edssrvdictionary.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.operation.changeset.GetChangeSet;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.operation.changeset.GetChangeSetChunks;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.base.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.chunks.ChangeSetChunkDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.chunks.GetTermsDelDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.chunks.GetTermsInsDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostTermsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.validators.NoFutureDate;
import it.finanze.sanita.fse2.ms.edssrvdictionary.validators.ValidObjectId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.*;

/**
 * ChangeSet retriever controller
 *
 * @author Riccardo Bonesi
 */
@Validated
@RequestMapping(API_CHANGESET_MAPPER)
public interface IChangeSetCTL {
	@GetChangeSet
	@GetMapping(API_CHANGESET_STATUS)
	@Tag(name = API_CHANGESET_TAG)
    ChangeSetResDTO changeSet(
		@RequestParam(value=API_QP_LAST_UPDATE, required = false)
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@NoFutureDate(message = "The last update date cannot be in the future")
		Date lastUpdate
	) throws OperationException;

	@GetChangeSetChunks
	@GetMapping(API_CHANGESET_CHUNKS)
	@Tag(name = API_CHANGESET_CHUNKS_TAG)
    ChangeSetChunkDTO changeSetChunks(
		@RequestParam(value=API_QP_LAST_UPDATE, required = false)
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@NoFutureDate(message = "The last update date cannot be in the future")
		Date lastUpdate
	) throws OperationException;

	@GetMapping(value = API_CHANGESET_CHUNKS_INS, produces = {MediaType.APPLICATION_JSON_VALUE })
	@Tag(name = API_CHANGESET_CHUNKS_TAG)
	@Operation(summary = "Returns a terminology chunk given its index and document id (insert-only)", description = "Servizio che consente di ritornare un Terminology dalla base dati dati il suo ID.")
	@ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = PostTermsResDTO.class)))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Richiesta terminology avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetTermsInsDTO.class))),
		@ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
		@ApiResponse(responseCode = "404", description = "terminology non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
	GetTermsInsDTO getTermsByChunkIns(
		@PathVariable
		@ValidObjectId
		String id,
		@PathVariable
		int idx
	) throws OutOfRangeException, DocumentNotFoundException, DataIntegrityException, OperationException;

	@GetMapping(value = API_CHANGESET_CHUNKS_DEL, produces = {MediaType.APPLICATION_JSON_VALUE })
	@Tag(name = API_CHANGESET_CHUNKS_TAG)
	@Operation(summary = "Returns a terminology chunk given its index and document id (deleted-only)", description = "Servizio che consente di ritornare un Terminology dalla base dati dati il suo ID.")
	@ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = PostTermsResDTO.class)))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Richiesta terminology avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetTermsDelDTO.class))),
		@ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
		@ApiResponse(responseCode = "404", description = "terminology non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
	GetTermsDelDTO getTermsByChunkDel(
		@PathVariable
		@ValidObjectId
		String id,
		@PathVariable
		int idx
	) throws OutOfRangeException, DocumentNotFoundException, DataIntegrityException, OperationException;


}
