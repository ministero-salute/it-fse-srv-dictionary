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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.RequestDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.FormatEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.TypeEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.ChunksETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.FileUtility;

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
    
    // TODO - tests on /v1/terminology/{format}
    
    // TODO - tests on /v1/terminology/{oid}/{version}

    /**
     * Returns all existing terminologies from database.
     * * @return List of TerminologyETY.
     */
    private List<TerminologyETY> getTerminologies() {
        return mongoTemplate.findAll(TerminologyETY.class);
    }
}
