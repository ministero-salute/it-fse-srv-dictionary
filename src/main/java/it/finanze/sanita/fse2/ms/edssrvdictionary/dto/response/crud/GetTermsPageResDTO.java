/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud;


import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
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
    private List<TerminologyDocumentDTO> items;
    private GetTermsPageLinksDTO links;
    private long numberOfItems;
    
    public GetTermsPageResDTO(
        LogTraceInfoDTO traceInfo,
        List<TerminologyDocumentDTO> items,
        String system,
        Page<TerminologyETY> page,
        long numberOfItems
    ) {
        super(traceInfo);
        this.items = items;
        this.links = GetTermsPageLinksDTO.fromPage(system, page);
        this.numberOfItems = numberOfItems;
    }
}
