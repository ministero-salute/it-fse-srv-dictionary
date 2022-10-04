package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response; 


import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * DTO use to return a document as response to getDocumentByChunk request
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetTerminologiesResDTO extends ResponseDTO {

    @ArraySchema(schema = @Schema(implementation = TerminologyDocumentDTO.class))
    private List<TerminologyDocumentDTO> documents;

    public GetTerminologiesResDTO(LogTraceInfoDTO traceInfo, List<TerminologyDocumentDTO> data) {
        super(traceInfo);
        this.documents = data;
    }
}
