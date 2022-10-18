package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud;


import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * DTO use to return a document as response to getDocument by ID request.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetTermsPageResDTO extends ResponseDTO {

    @Schema(implementation = TerminologyDocumentDTO.class)
    private List<TerminologyDocumentDTO> documents;
    private GetTermsPageLinksDTO links;

    public GetTermsPageResDTO(
        LogTraceInfoDTO traceInfo,
        List<TerminologyDocumentDTO> documents,
        String system,
        Page<TerminologyETY> page
    ) {
        super(traceInfo);
        this.documents = documents;
        this.links = GetTermsPageLinksDTO.fromPage(system, page);
    }
}
