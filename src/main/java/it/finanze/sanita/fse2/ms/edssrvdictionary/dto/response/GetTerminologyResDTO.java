package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response; 


import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO use to return a document as response to getDocument by ID request.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetTerminologyResDTO extends ResponseDTO {

    @Schema(implementation = TerminologyDocumentDTO.class)
    private TerminologyDocumentDTO document;

    public GetTerminologyResDTO(LogTraceInfoDTO traceInfo, TerminologyDocumentDTO data) {
        super(traceInfo);
        this.document = data;
    }
}
