/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.pagination;


import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
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
public class GetDocsPageResDTO extends ResponseDTO {

    @ArraySchema(schema = @Schema(implementation = TerminologyDocumentDTO.class))
    private List<TerminologyDocumentDTO> items;
    private GetDocsPageLinksDTO links;
    private long numberOfItems;
    
    public GetDocsPageResDTO(
        LogTraceInfoDTO traceInfo,
        List<TerminologyDocumentDTO> items,
        String system,
        Page<TerminologyETY> page
    ) {
        super(traceInfo);
        this.items = items;
        this.links = GetDocsPageLinksDTO.fromPage(system, page);
        this.numberOfItems = items.size();
    }
}
