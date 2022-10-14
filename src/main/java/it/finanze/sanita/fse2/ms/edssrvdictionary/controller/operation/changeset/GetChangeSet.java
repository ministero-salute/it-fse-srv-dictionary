package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.operation.changeset;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.base.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import org.springframework.http.MediaType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

// OpenAPI descriptor
@Operation(
    summary = "Retrieve changeset by last-update",
    description = "Returns an on-the-fly snapshot status for the given timeframe"
)
@ApiResponses(
    value = {
        @ApiResponse(
            responseCode = "200",
            description = "Status retrieved",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ChangeSetResDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid parameters",
            content = @Content(
                mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unable to execute the request due to an internal error",
            content = @Content(
                mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    }
)
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetChangeSet {
}