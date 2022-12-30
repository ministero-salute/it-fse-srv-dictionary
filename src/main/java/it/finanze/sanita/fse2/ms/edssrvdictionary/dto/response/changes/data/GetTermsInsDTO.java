/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.data;


import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * DTO use to return a document as response to getDocumentByChunk request
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetTermsInsDTO extends ResponseDTO {

    @ArraySchema(schema = @Schema(implementation = TerminologyDocumentDTO.class))
    private List<TerminologyDocumentDTO> documents;

    public GetTermsInsDTO(LogTraceInfoDTO traceInfo, List<TerminologyDocumentDTO> data) {
        super(traceInfo);
        this.documents = data;
    }
}