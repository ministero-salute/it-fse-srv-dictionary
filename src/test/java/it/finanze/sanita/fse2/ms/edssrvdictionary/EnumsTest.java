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
package it.finanze.sanita.fse2.ms.edssrvdictionary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.ErrorClassEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.FormatEnum;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(Constants.Profile.TEST)
class EnumsTest {

    @Test
    @DisplayName("testErrorClassEnum")
    void testResultLogEnum() {
        String type = ErrorClassEnum.GENERIC.getType();
        String title = ErrorClassEnum.GENERIC.getTitle();
        String detail = ErrorClassEnum.GENERIC.getDetail();
        String instance = ErrorClassEnum.GENERIC.getInstance();
        
        assertEquals(type, ErrorClassEnum.GENERIC.getType());
        assertEquals(title, ErrorClassEnum.GENERIC.getTitle());
        assertEquals(detail, ErrorClassEnum.GENERIC.getDetail());
        assertEquals(instance, ErrorClassEnum.GENERIC.getInstance());
    }
    
    @Test
    @DisplayName("testFormatEnum")
    void testFormatEnum() {
        String fileExtension = FormatEnum.CUSTOM_CSV.getFileExtension();
        assertEquals(fileExtension, FormatEnum.CUSTOM_CSV.getFileExtension());
    }
    
}
