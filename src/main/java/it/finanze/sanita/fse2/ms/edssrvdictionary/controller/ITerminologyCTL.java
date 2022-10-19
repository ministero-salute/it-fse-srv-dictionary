package it.finanze.sanita.fse2.ms.edssrvdictionary.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.*;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.*;
import static org.springframework.http.MediaType.*;

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
        produces = { APPLICATION_JSON_VALUE }
    )
    @Operation(
        summary = "Restituisce un terminology dato il suo identificativo",
        description = "Servizio che consente di ritornare una terminologia dato il suo identificativo."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documento presente sul database", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetTermsResDTO.class))),
        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Documento non trovato sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    GetTermsResDTO getTerminologyById(
        @PathVariable
        @Parameter(description = "Identificatore documento")
        @NotBlank(message = ERR_VAL_ID_BLANK)
        @ValidObjectId(message = ERR_VAL_ID_NOT_VALID)
        String id
    ) throws OperationException, DocumentNotFoundException;

    @DeleteMapping(
        value = API_TERMS_GET_ONE_BY_ID,
        produces = { APPLICATION_JSON_VALUE }
    )
    @Operation(
        summary = "Cancellazione di un terminology dato il suo identificativo",
        description = "Servizio che consente di cancellare una terminologia dato il suo identificativo."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documento cancellato correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = DelTermsResDTO.class))),
        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Documento non trovato sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    DelTermsResDTO deleteTerminologyById(
        @PathVariable
        @Parameter(description = "Identificatore documento")
        @NotBlank(message = ERR_VAL_ID_BLANK)
        @ValidObjectId(message = ERR_VAL_ID_NOT_VALID)
        String id
    ) throws DocumentNotFoundException, OperationException;

    @PostMapping(
        value = API_TERMS_GET_BY_CSV,
        produces = { APPLICATION_JSON_VALUE },
        consumes = { MULTIPART_FORM_DATA_VALUE }
    )
    @Operation(
        summary = "Aggiunge terminologie attraverso un file CSV (DEPRECATO)",
        description = "Servizio che consente di aggiungere terminologie sulla base dati caricando un file csv. (DEPRECATO)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Creazione avvenuta con successo", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<PostTermsResDTO> uploadTerminologyFile(HttpServletRequest request, @RequestPart("file") MultipartFile file) throws IOException, OperationException;

    @GetMapping(
        value = API_SYSTEM_EXTS,
        produces = { APPLICATION_JSON_VALUE }
    )
    @Operation(
        summary = "Restituisce le terminologie appartenenti al system richiesto con paginazione",
        description = "Servizio che restituisce tutte le terminologie appartenenti ad un certo system con paginazione"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documenti restituite correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetTermsPageResDTO.class))),
        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "System non trovato sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    GetTermsPageResDTO getTerminologies(
        @PathVariable
        @Parameter(description = "Identificatore del sistema")
        @NotBlank(message = ERR_VAL_SYSTEM_BLANK)
        String system,
        @RequestParam(API_QP_PAGE)
        @Parameter(description = "Indice pagina richiesto (eg. 0, 1, 2...)")
        int page,
        @RequestParam(API_QP_LIMIT)
        @Parameter(description = "Limite documenti per pagina (eg. 10, 20 ...)")
        int limit
    ) throws OperationException, DocumentNotFoundException, OutOfRangeException;

    @PostMapping(
        produces = { APPLICATION_JSON_VALUE },
        consumes = { MULTIPART_FORM_DATA_VALUE }
    )
    @Operation(
        summary = "Aggiunge terminologie attraverso un file XML",
        description = "Servizio che consente di aggiungere terminologie sulla base dati caricando un file xml."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Documenti caricati correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostTermsResDTO.class))),
        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "System già presente sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    PostTermsResDTO uploadTerminologies(
        @RequestPart
        @Parameter(description = "XML contenente le terminologie da inserire (e.g 2.16.840.1.113883.1.11.1.xml)")
        MultipartFile file,
        @RequestPart
        @Parameter(description = "Versione del sistema")
        @NotBlank(message = ERR_VAL_VERSION_BLANK)
        String version
    ) throws OperationException, DocumentAlreadyPresentException, DataProcessingException, InvalidContentException;

    @PutMapping(
        produces = { APPLICATION_JSON_VALUE },
        consumes = { MULTIPART_FORM_DATA_VALUE }
    )
    @Operation(
        summary = "Modifica terminologie attraverso un file XML",
        description = "Servizio che consente di aggiungere una nuova versione per terminologie già presenti sulla base dati caricando un file xml."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Documenti caricati correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = PutTermsResDTO.class))),
        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "System non presente sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "Version già presente sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    PutTermsResDTO updateTerminologies(
        @RequestPart
        @Parameter(description = "XML contenente le terminologie da inserire (e.g 2.16.840.1.113883.1.11.1.xml)")
        MultipartFile file,
        @RequestPart
        @Parameter(description = "Versione del sistema incrementata rispetto alla precedente")
        @NotBlank(message = ERR_VAL_VERSION_BLANK)
        String version
    ) throws OperationException, DocumentNotFoundException, DataProcessingException, DataIntegrityException, DocumentAlreadyPresentException, InvalidContentException;

    @DeleteMapping(
        value = API_SYSTEM_EXTS,
        produces = { APPLICATION_JSON_VALUE }
    )
    @Operation(
        summary = "Cancellazione terminologie attraverso il system",
        description = "Servizio che consente di cancellare un certo system dalla base dati"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documenti cancellati correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = DelTermsResDTO.class))),
        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "System non presente sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    DelTermsResDTO deleteTerminologies(
        @PathVariable
        @Parameter(description = "Identificatore del sistema")
        @NotBlank(message = ERR_VAL_SYSTEM_BLANK)
        String system
    ) throws OperationException, DocumentNotFoundException, DataIntegrityException;

}
