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

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.ChunksETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(Constants.Profile.TEST)
class TerminologyControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    void setup() {
        mongoTemplate.dropCollection(TerminologyETY.class);
        mongoTemplate.dropCollection(ChunksETY.class);
        mongoTemplate.dropCollection(SnapshotETY.class);
    }
    
//    @Test
//    void uploadTerminologiesTest() throws Exception {
//    	
//        // Retrieve file from internal resources
//        byte[] file = FileUtility.getFileFromInternalResources("Files/vocabulary/Terminology_10Items.csv");
//
//        // Build the request
//        MockHttpServletRequestBuilder requestBuilder = multipart("/{format}", FormatEnum.CUSTOM_CSV)
//                .file(new MockMultipartFile("file", "Terminology_10Items.csv", "text/csv", file))
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .contentType(MediaType.MULTIPART_FORM_DATA);
//        
//        // Perform the request
//        MvcResult result = mvc.perform(requestBuilder)
//                .andExpect(status().isCreated()).andReturn();
//        
//        PostDocsResDTO response = new Gson().fromJson(result.getResponse().getContentAsString(), PostDocsResDTO.class);
//        
//        // Assertions
//        assertEquals(10, response.getInsertedItems(), "The file contains 10 rows, the items inserted should be 10");
//        List<TerminologyETY> terminologies = getTerminologies();
//        assertEquals(10, terminologies.size(), "The file contains 10 rows, the items inserted should be 10");
//    }
    
    // TODO - tests on /v1/terminology/{oid}/{version}

    /**
     * Returns all existing terminologies from database.
     * * @return List of TerminologyETY.
     */
    private List<TerminologyETY> getTerminologies() {
        return mongoTemplate.findAll(TerminologyETY.class);
    }
}
