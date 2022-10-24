/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import javax.validation.constraints.NotBlank;
import java.util.Date;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.*;
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
		@NoFutureDate(message = ERR_VAL_FUTURE_DATE)
		Date lastUpdate
	) throws OperationException;

	@GetChangeSetChunks
	@GetMapping(API_CHANGESET_CHUNKS)
	@Tag(name = API_CHANGESET_CHUNKS_TAG)
    ChangeSetChunkDTO changeSetChunks(
		@RequestParam(value=API_QP_LAST_UPDATE, required = false)
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@NoFutureDate(message = ERR_VAL_FUTURE_DATE)
		Date lastUpdate
	) throws OperationException;

	@GetMapping(
		value = API_CHANGESET_CHUNKS_INS,
		produces = {MediaType.APPLICATION_JSON_VALUE }
	)
	@Tag(name = API_CHANGESET_CHUNKS_TAG)
	@Operation(
		summary = "Restituisce un chunk dato indice e identificativo snapshot (solo-inserimenti)",
		description = "Servizio che consente di restituire le terminologie presenti nel chunk di un dato snapshot."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Richiesta terminologie avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetTermsInsDTO.class))),
		@ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
		@ApiResponse(responseCode = "404", description = "Chunk richiesto non presente", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
		@ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	GetTermsInsDTO getTermsByChunkIns(
		@PathVariable
		@Parameter(description = "Identificatore documento (snapshot)")
		@NotBlank(message = ERR_VAL_ID_BLANK)
		@ValidObjectId(message = ERR_VAL_ID_NOT_VALID)
		String id,
		@PathVariable
		@Parameter(description = "Indice chunk richiesto (eg. 0, 1, 2...)")
		int idx
	) throws OutOfRangeException, DocumentNotFoundException, DataIntegrityException, OperationException;

	@GetMapping(
		value = API_CHANGESET_CHUNKS_DEL,
		produces = {MediaType.APPLICATION_JSON_VALUE }
	)
	@Tag(name = API_CHANGESET_CHUNKS_TAG)
	@Operation(
		summary = "Restituisce un chunk dato indice e identificativo snapshot (solo-cancellazioni)",
		description = "Servizio che consente di restituire le terminologie presenti nel chunk di un dato snapshot."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Richiesta terminologie avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetTermsDelDTO.class))),
		@ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
		@ApiResponse(responseCode = "404", description = "Chunk richiesto non presente", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
		@ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	GetTermsDelDTO getTermsByChunkDel(
		@PathVariable
		@Parameter(description = "Identificatore documento (snapshot)")
		@NotBlank(message = ERR_VAL_ID_BLANK)
		@ValidObjectId(message = ERR_VAL_ID_NOT_VALID)
		String id,
		@PathVariable
		@Parameter(description = "Indice chunk richiesto (eg. 0, 1, 2...)")
		int idx
	) throws OutOfRangeException, DocumentNotFoundException, DataIntegrityException, OperationException;


}
