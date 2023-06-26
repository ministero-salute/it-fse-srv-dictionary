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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ChangeSetChunkDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.data.snapshot.ChunksDTO.Chunk;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.ChunksETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(Constants.Profile.TEST)
@AutoConfigureMockMvc
class ChangesetControllerTest {
    
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

    @ParameterizedTest
    @ValueSource(ints = {100, 1000})
    @DisplayName("Get Changeset chunk")
    void getChangeSetTest(int numTerminologies) throws Exception {
        
        List<TerminologyETY> terminologies = createTerminologies(numTerminologies, false, new Date(), new Date());
        
        MvcResult result = mvc.perform(get("/v1/changeset/terminology/chunks"))
            .andExpect(status().isOk()).andReturn();
        
        ChangeSetChunkDTO responseBody = new Gson().fromJson(result.getResponse().getContentAsString(), ChangeSetChunkDTO.class);
        Chunk insertions = responseBody.getChunks().getInsertions();
        Chunk deletions = responseBody.getChunks().getDeletions();

        assertEquals(terminologies.size(), insertions.getChunksItems());
        assertEquals(0, deletions.getChunksItems());
        assertNotNull(responseBody.getChunks().getSnapshotID());
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @ValueSource(ints = {100, 1000})
    @DisplayName("Get Changeset terms inserted by Id")
    void getChangesetInsTermsById(int numTerminologies) throws Exception {
        List<TerminologyETY> terminologies = createTerminologies(numTerminologies, false, new Date(), new Date());
        
        MvcResult result = mvc.perform(get("/v1/changeset/terminology/chunks"))
            .andExpect(status().isOk()).andReturn();
        
        ChangeSetChunkDTO responseBody = new Gson().fromJson(result.getResponse().getContentAsString(), ChangeSetChunkDTO.class);

        String chunkId = responseBody.getChunks().getSnapshotID();
        int chunkIndex = 0;

        result = mvc.perform(get("/v1/changeset/terminology/chunks/ins/{id}/{idx}", chunkId, chunkIndex))
            .andExpect(status().isOk()).andReturn();

        Document chunkResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Document.class);
        List<TerminologyDocumentDTO> terminologiesResponse = (List<TerminologyDocumentDTO>) chunkResponse.get("documents");
        assertEquals(terminologies.size(), terminologiesResponse.size());

        mvc.perform(get("/v1/changeset/terminology/chunks/ins/{id}/{idx}", chunkId, 100))
            .andExpect(status().isBadRequest());
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @ValueSource(ints = {100, 1000})
    @DisplayName("Get Changeset deleted terms by Id")
    void getChangesetDelTermsById(int numTerminologies) throws Exception {
        // Inserting noise
        createTerminologies(numTerminologies, false, new Date(), new Date());
        List<TerminologyETY> terminologies = createTerminologies(numTerminologies, true, Date.from(LocalDate.now().minusDays(5).atStartOfDay().toInstant(ZoneOffset.UTC)), new Date());
        String lastUpdate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(Date.from(LocalDate.now().minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)));

        MvcResult result = mvc.perform(get("/v1/changeset/terminology/chunks")
            .param("lastUpdate", lastUpdate))
            .andExpect(status().isOk()).andReturn();
        
        ChangeSetChunkDTO responseBody = new Gson().fromJson(result.getResponse().getContentAsString(), ChangeSetChunkDTO.class);

        String chunkId = responseBody.getChunks().getSnapshotID();
        int chunkIndex = 0;

        result = mvc.perform(get("/v1/changeset/terminology/chunks/del/{id}/{idx}", chunkId, chunkIndex))
            .andExpect(status().isOk()).andReturn();

        Document chunkResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Document.class);
        List<TerminologyDocumentDTO> terminologiesResponse = (List<TerminologyDocumentDTO>) chunkResponse.get("documents");
        assertEquals(terminologies.size(), terminologiesResponse.size());

        String unexistingChunkId = new ObjectId().toString();
        mvc.perform(get("/v1/changeset/terminology/chunks/del/{id}/{idx}", unexistingChunkId, chunkIndex))
            .andExpect(status().isNotFound());
    }

    private List<TerminologyETY> createTerminologies(int numTerminologies, boolean isDeleted, Date insertionDate, Date lastUpdateDate) {
        
        List<TerminologyETY> terminologies = new ArrayList<>();
        
        for (int i = 0; i < numTerminologies; i++) {
            TerminologyETY terminology = new TerminologyETY();
            terminology.setCode("code" + i);
            terminology.setDeleted(isDeleted);
            terminology.setDescription("description" + i);
            terminology.setInsertionDate(insertionDate);
            terminology.setReleaseDate(null);
            terminology.setLastUpdateDate(lastUpdateDate);
            terminology.setSystem("system" + i);
            terminology.setVersion("1.0");
            
            terminologies.add(terminology);
        }

        mongoTemplate.insertAll(terminologies);

        return terminologies;
    }
}
