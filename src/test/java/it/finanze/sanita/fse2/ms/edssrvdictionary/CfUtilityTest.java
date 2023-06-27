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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.CfUtility;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(Constants.Profile.TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CfUtilityTest {
    @Test
    void successTest() {
        String invalidRandomCF16 = "33NDMA80A01A883I";
        Assertions.assertFalse(CfUtility.isValidCf(invalidRandomCF16));

        String validRandomCF16 = "DKNDMA80A01A883I";
        Assertions.assertTrue(CfUtility.isValidCf(validRandomCF16));

        String invalidRandomCF11 = "DKNDMA80A01";
        Assertions.assertFalse(CfUtility.isValidCf(invalidRandomCF11));

        String validRandomCF11 = "29259870359";
        Assertions.assertTrue(CfUtility.isValidCf(validRandomCF11));

        String eniInvalidRandomCF16 = "ENIDMA80A01A883I";
        Assertions.assertFalse(CfUtility.isValidCf(eniInvalidRandomCF16));

        String stpInvalidRandomCF16 = "STPDMA80A01A883I";
        Assertions.assertFalse(CfUtility.isValidCf(stpInvalidRandomCF16));
    }
    
}
