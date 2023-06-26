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

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.data.domain.Page;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Data
@AllArgsConstructor
public class GetDocsPageLinksDTO {

    private String next;
    private String prev;

    public static GetDocsPageLinksDTO fromPage(String system, Page<TerminologyETY> page) {
        return new GetDocsPageLinksDTO(
            getNext(system, page),
            getPrev(system, page)
        );
    }

    private static String getPrev(String system, Page<TerminologyETY> page) {
        // Default state
        String prev = null;
        // Check if there is a previous page and the current page is on the right index
        if(page.hasPrevious() && page.hasContent()) {
            // Create URI
            prev = new URIBuilder(fromCurrentContextPath().build().toUri())
                .setPathSegments(
                    API_VERSION,
                    API_TERMINOLOGY,
                    system
                )
                .addParameter(API_QP_PAGE, String.valueOf(page.getNumber() - 1))
                .addParameter(API_QP_LIMIT, String.valueOf(page.getSize()))
                .toString();
        }
        return prev;
    }

    private static String getNext(String system, Page<TerminologyETY> page) {
        // Default state
        String next = null;
        // Check if there is a next page
        if(page.hasNext()) {
            // Create URI
            next = new URIBuilder(fromCurrentContextPath().build().toUri())
                .setPathSegments(
                    API_VERSION,
                    API_TERMINOLOGY,
                    system
                )
                .addParameter(API_QP_PAGE, String.valueOf(page.getNumber() + 1))
                .addParameter(API_QP_LIMIT, String.valueOf(page.getSize()))
                .toString();
        }
        return next;
    }

}
