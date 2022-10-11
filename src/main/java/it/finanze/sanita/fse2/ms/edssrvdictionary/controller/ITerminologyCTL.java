package it.finanze.sanita.fse2.ms.edssrvdictionary.controller;


import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.chunks.GetTermsDelDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.chunks.GetTermsInsDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;

import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.validators.ValidObjectId;

/**
 * Terminology controller
 *
 * @author Riccardo Bonesi
 */
@RequestMapping(path = "/v1/terminology")
@Validated
public interface ITerminologyCTL {

    @GetMapping(value = "/chunks/ins/{id}/{idx}", produces = {MediaType.APPLICATION_JSON_VALUE })
    @Tag(name = RoutesUtility.API_CHANGESET_TAG)
    @Operation(summary = "Returns a terminology chunk given its index and document id (insert-only)", description = "Servizio che consente di ritornare un Terminology dalla base dati dati il suo ID.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TerminologyResponseDTO.class)))
    @ApiResponses(value = {
                    @ApiResponse(responseCode = "200", description = "Richiesta terminology avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetTermsInsDTO.class))),
                    @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TerminologyErrorResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "terminology non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TerminologyErrorResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TerminologyErrorResponseDTO.class))) })
    GetTermsInsDTO getTermsByChunkIns(@PathVariable @ValidObjectId String id, @PathVariable int idx) throws ChunkOutOfRangeException, DocumentNotFoundException, DataIntegrityException, OperationException;

    @GetMapping(value = "/chunks/del/{id}/{idx}", produces = {MediaType.APPLICATION_JSON_VALUE })
    @Tag(name = RoutesUtility.API_CHANGESET_TAG)
    @Operation(summary = "Returns a terminology chunk given its index and document id (deleted-only)", description = "Servizio che consente di ritornare un Terminology dalla base dati dati il suo ID.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TerminologyResponseDTO.class)))
    @ApiResponses(value = {
                    @ApiResponse(responseCode = "200", description = "Richiesta terminology avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetTermsDelDTO.class))),
                    @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TerminologyErrorResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "terminology non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TerminologyErrorResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TerminologyErrorResponseDTO.class))) })
    GetTermsDelDTO getTermsByChunkDel(@PathVariable @ValidObjectId String id, @PathVariable int idx) throws ChunkOutOfRangeException, DocumentNotFoundException, DataIntegrityException, OperationException;

    @GetMapping(value = "/id/{id}", produces = {MediaType.APPLICATION_JSON_VALUE })
    @Tag(name = RoutesUtility.API_CHANGESET_TAG)
    @Operation(summary = "Returns a Terminology from MongoDB, given its ID", description = "Servizio che consente di ritornare un Terminology dalla base dati dati il suo ID.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TerminologyResponseDTO.class)))
    @ApiResponses(value = {
                    @ApiResponse(responseCode = "200", description = "Richiesta terminology avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TerminologyDTO.class))),
                    @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TerminologyErrorResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "terminology non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TerminologyErrorResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TerminologyErrorResponseDTO.class))) })
    GetTerminologyResDTO getTerminologyById(HttpServletRequest request, @PathVariable @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "id does not match the expected size") @ValidObjectId(message = "Document id not valid") String id)
    throws OperationException, DocumentNotFoundException;


    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Tag(name = RoutesUtility.API_TEST_TAG)
    @Operation(summary = "Add terminologies to MongoDB", description = "Servizio che consente di aggiungere terminologes alla base dati caricando un file csv.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ResponseDTO.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creazione Terminologies avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
    ResponseEntity<TerminologyResponseDTO> uploadTerminologyFile(HttpServletRequest request, @RequestPart("file") MultipartFile file) throws IOException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException;

    @DeleteMapping(value = "/id/{id}", produces = {MediaType.APPLICATION_JSON_VALUE })
    @Tag(name = RoutesUtility.API_TEST_TAG)
    TerminologyDeleteResponseDTO deleteTerminologyById(
        @PathVariable
        @ValidObjectId(message = "Document id not valid") String id
    ) throws DocumentNotFoundException, OperationException;
}
