package it.finanze.sanita.fse2.ms.edssrvdictionary.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.validators.ValidObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import java.io.IOException;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.*;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

/**
 * Terminology controller
 *
 * @author Riccardo Bonesi
 */
@RequestMapping(path = API_TERMS_MAPPER)
@Tag(name = API_DOCUMENTS_TAG)
@Validated
public interface ITerminologyCTL {

    @GetMapping(
        value = API_TERMS_GET_ONE_BY_ID,
        produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    @Operation(summary = "Restituisce un terminology dato il suo identificativo", description = "Servizio che consente di ritornare un Terminology dalla base dati dati il suo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recupero dati terminology", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetTermsResDTO.class))),
        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Documento non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    GetTermsResDTO getTerminologyById(@PathVariable @Size(max = DEFAULT_STRING_MAX_SIZE, message = "id does not match the expected size") @ValidObjectId(message = "Document id not valid") String id)
        throws OperationException, DocumentNotFoundException;

    @DeleteMapping(
        value = API_TERMS_GET_ONE_BY_ID,
        produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    DelTermsResDTO deleteTerminologyById(
        @PathVariable
        @ValidObjectId(message = "Document id not valid") String id
    ) throws DocumentNotFoundException, OperationException;

    @PostMapping(value = API_TERMS_GET_BY_CSV, produces = {MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Add terminologies to MongoDB", description = "Servizio che consente di aggiungere terminologes alla base dati caricando un file csv.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = PostTermsResDTO.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creazione Terminologies avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
    ResponseEntity<PostTermsResDTO> uploadTerminologyFile(HttpServletRequest request, @RequestPart("file") MultipartFile file) throws IOException, OperationException;

    @GetMapping(
        value = API_SYSTEM_EXTS,
        produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    GetTermsPageResDTO getTerminologies(
        @PathVariable String system,
        @RequestParam(API_QP_PAGE) int page,
        @RequestParam(API_QP_LIMIT) int limit
    ) throws OperationException, DocumentNotFoundException, OutOfRangeException;

    @PostMapping(
        produces = { MediaType.APPLICATION_JSON_VALUE },
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    @ResponseStatus(HttpStatus.CREATED)
    PostTermsResDTO uploadTerminologies(
        @RequestPart
        MultipartFile file,
        @RequestPart
        String version
    ) throws OperationException, DocumentAlreadyPresentException, DataProcessingException;

    @PutMapping(
        produces = { MediaType.APPLICATION_JSON_VALUE },
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    @ResponseStatus(HttpStatus.CREATED)
    PutTermsResDTO updateTerminologies(
        @RequestPart
        MultipartFile file,
        @RequestPart
        String version
    ) throws OperationException, DocumentNotFoundException, DataProcessingException, DataIntegrityException, DocumentAlreadyPresentException;

    @DeleteMapping(
        value = API_SYSTEM_EXTS,
        produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    DelTermsResDTO deleteTerminologies(
        @PathVariable
        String system
    ) throws OperationException, DocumentNotFoundException, DataIntegrityException;

}
