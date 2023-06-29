package it.finanze.sanita.fse2.ms.edssrvdictionary;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.FileUtility;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.StringUtility;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(Constants.Profile.TEST)
class StringUtilityTest {
	
	@Test
    @DisplayName("encodeSHA256 Test")
    void encodeSHA256Test() {
        byte[] pdfAttachment = FileUtility.getFileFromInternalResources("Files/web-scraping.csv");
        assertNotNull(StringUtility.encodeSHA256(pdfAttachment));
    }
	
	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("isNullOrEmpty String Test")
	void isNullOrEmptyStringTest(String str) {
		assertTrue(StringUtility.isNullOrEmpty(str));
		assertFalse(StringUtility.isNullOrEmpty("notEmpty"));
	}
}
