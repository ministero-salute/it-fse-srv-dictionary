package it.finanze.sanita.fse2.ms.edssrvdictionary.controller;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_VAL_SYSTEM_BLANK;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_VAL_URL_BLANK;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_WEB_DELETE_MULTI;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_WEB_DELETE_SYSTEM;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_WEB_INSERT_MULTI;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_WEB_SCRAPING;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_WEB_SYSTEM_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import javax.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.DelDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.webscraping.PostSingleDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;

@RequestMapping(path = API_WEB_SCRAPING)
@Tag(name = "Web Scraping")
@Validated
public interface IWebScrapingCTL {
    
    @PostMapping(value = API_WEB_SYSTEM_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Inserimento documento tramite system e url")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Documento caricato correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostSingleDTO.class))),
        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "System già presente sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    PostSingleDTO insertWebScraping(
        @PathVariable("system") @Parameter @NotBlank(message = ERR_VAL_SYSTEM_BLANK) String system,
        @PathVariable("url") @Parameter @NotBlank(message = ERR_VAL_URL_BLANK) String url)
        throws OperationException, DocumentAlreadyPresentException;

    @PostMapping(value = API_WEB_INSERT_MULTI, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Inserimento massivo documenti tramite file CSV", description = "Servizio che permette di inserire massivamente i documenti aventi system e url. Il file CSV deve avere intestazione")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Documenti caricati correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostDocsResDTO.class))),
        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "System già presente sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    PostDocsResDTO insertMultiWebScraping(
        @RequestParam @Parameter(description = "CSV contenente system e url") MultipartFile file)
        throws DataProcessingException, DocumentAlreadyPresentException, OperationException, InvalidContentException;

    @DeleteMapping(value = API_WEB_DELETE_SYSTEM, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Cancellazione documento tramite system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documento cancellato", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = DelDocsResDTO.class))),
        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "System non presente sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    DelDocsResDTO deleteWebScraping(
        @PathVariable("system") @Parameter @NotBlank(message = ERR_VAL_SYSTEM_BLANK) String system)
        throws DataIntegrityException, DocumentNotFoundException, OperationException;

    @DeleteMapping(value = API_WEB_DELETE_MULTI, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Cancellazione massiva documenti tramite file CSV", description = "Servizio che consente di cancellare massivamente i documenti aventi system e url. Il file CSV deve avere intestazione")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documenti cancellati correttamente", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = DelDocsResDTO.class))),
        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "System non presenti sul database", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Errore interno del server", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    DelDocsResDTO deleteMultiWebScraping(
        @RequestParam @Parameter(description = "CSV contenente system e url") MultipartFile file)
        throws InvalidContentException, DataProcessingException, OperationException, DocumentNotFoundException, DataIntegrityException;
}
