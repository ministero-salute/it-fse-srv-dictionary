/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
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

    @Test
    @SuppressWarnings("rawtypes")
    void getTerminologyTest() throws Exception {

        TerminologyETY terminology = new TerminologyETY();
        String id = new ObjectId().toString();
        terminology.setId(id);
        terminology.setCode("CODE");
        terminology.setDeleted(false);
        terminology.setDescription("DESCRIPTION");
        terminology.setInsertionDate(new Date());
        terminology.setLastUpdateDate(new Date());
        terminology.setSystem("SYSTEM");
        terminology.setVersion("1.0.0");

        mongoTemplate.save(terminology);

        MvcResult result = mvc.perform(get("/v1/terminology/id/{id}", id))
                .andExpect(status().isOk()).andReturn();

        Document response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Document.class);
        LinkedHashMap document = response.get("document", LinkedHashMap.class);

        assertEquals(terminology.getCode(), document.get("code").toString());
        assertEquals(terminology.getDescription(), document.get("description").toString());
        assertEquals(terminology.getSystem(), document.get("system").toString());
        assertEquals(terminology.getVersion(), document.get("version").toString());
        assertEquals(terminology.getId(), document.get("id").toString());

    }

    @Test
    void insertTerminologyOkTest() throws Exception {

        String version = "1.0.0";
        Date releaseDate = new Date();
        byte[] file = FileUtility.getFileFromInternalResources("Files/vocabulary/Terminology_10Items.csv");

        MockHttpServletRequestBuilder requestBuilder = multipart("/v1/terminology")
                .file(new MockMultipartFile("file", "Terminology_10Items.csv", "text/xml", file))
                .param("version", version)
                .param("releaseDate", new SimpleDateFormat("yyyy-MM-dd").format(releaseDate))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isCreated()).andReturn();

        PostDocsResDTO response = new Gson().fromJson(result.getResponse().getContentAsString(), PostDocsResDTO.class);
        assertEquals(10, response.getInsertedItems(), "The file contains 10 rows, the items inserted should be 10");

        List<TerminologyETY> terminologies = getTerminologies();
        assertEquals(10, terminologies.size(), "The file contains 10 rows, the items inserted should be 10");
    }

    @Test
    void insertTerminologyAlreadyExistingTest() throws Exception {

        String version = "1.0.0";
        Date releaseDate = new Date();
        byte[] file = FileUtility.getFileFromInternalResources("Files/vocabulary/Terminology_10Items.csv");

        MockHttpServletRequestBuilder requestBuilder = multipart("/v1/terminology")
                .file(new MockMultipartFile("file", "Terminology_10Items.csv", "text/xml", file))
                .param("version", version)
                .param("releaseDate", new SimpleDateFormat("yyyy-MM-dd").format(releaseDate))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        mvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();

        MvcResult result = mvc.perform(requestBuilder).andExpect(status().isConflict()).andReturn();
        assertEquals(DocumentAlreadyPresentException.class, result.getResolvedException().getClass());
    }

    @Test
    void invalidContentUpload() throws Exception {

        String version = "1.0.0";
        Date releaseDate = new Date();
        byte[] file = new byte[0];

        MockHttpServletRequestBuilder requestBuilder = multipart("/v1/terminology")
                .file(new MockMultipartFile("file", "Terminology_10Items.csv", "text/xml", file))
                .param("version", version)
                .param("releaseDate", new SimpleDateFormat("yyyy-MM-dd").format(releaseDate))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        mvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    @Test
    @SuppressWarnings("rawtypes")
    void getPagedTerminologyTest() throws Exception {
        String version = "1.0.0";
        Date releaseDate = new Date();
        String system = "Terminology_10Items";
        byte[] file = FileUtility.getFileFromInternalResources("Files/vocabulary/" + system + ".csv");

        MockHttpServletRequestBuilder requestBuilder = multipart("/v1/terminology")
                .file(new MockMultipartFile("file", system + ".csv", "text/xml", file))
                .param("version", version)
                .param("releaseDate", new SimpleDateFormat("yyyy-MM-dd").format(releaseDate))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        mvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();

        int limit = 5;

        MvcResult result = mvc.perform(get("/v1/terminology/{system}", system)
            .param("page", "0")
            .param("limit", String.valueOf(limit)))
            .andExpect(status().isOk()).andReturn();

        Document response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Document.class);
        assertEquals(limit, response.get("items", List.class).size(), "The limit is 5, the items returned should be 5");

        LinkedHashMap links = response.get("links", LinkedHashMap.class);

        assertNotNull(links.get("next"), "The next link should be not null");
        assertNull(links.get("prev"), "The previous link should be null");

        // Link next should return 5 items
        result = mvc.perform(get(links.get("next").toString())).andExpect(status().isOk()).andReturn();

        response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Document.class);
        assertEquals(limit, response.get("items", List.class).size(), "The limit is 5, the items returned should be 5");

        links = response.get("links", LinkedHashMap.class); 
        assertNull(links.get("next"), "The next link should be null");
        assertNotNull(links.get("prev"), "The previous link should not be null");
    }

    @Test
    void getTerminologyWrongPage() throws Exception {

        String version = "1.0.0";
        Date releaseDate = new Date();
        String system = "Terminology_10Items";
        byte[] file = FileUtility.getFileFromInternalResources("Files/vocabulary/" + system + ".csv");

        MockHttpServletRequestBuilder requestBuilder = multipart("/v1/terminology")
                .file(new MockMultipartFile("file", system + ".csv", "text/xml", file))
                .param("version", version)
                .param("releaseDate", new SimpleDateFormat("yyyy-MM-dd").format(releaseDate))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        mvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();

        int limit = 5;

        MvcResult result = mvc.perform(get("/v1/terminology/{system}", system)
            .param("page", "3")
            .param("limit", String.valueOf(limit)))
            .andExpect(status().isBadRequest()).andReturn();

        assertEquals(OutOfRangeException.class, result.getResolvedException().getClass());
    }

    @Test
    void getTerminologyWithWrongSystem() throws Exception {

        MvcResult result = mvc.perform(get("/v1/terminology/{system}", "UNEXISTING")
            .param("page", "0")
            .param("limit", String.valueOf(10)))
            .andExpect(status().isNotFound()).andReturn();

        assertEquals(DocumentNotFoundException.class, result.getResolvedException().getClass());
    }

    @Test
    void updateTerminology() throws Exception {
        String version = "1.0.0";
        Date releaseDate = new Date();
        String system = "Terminology_10Items";
        byte[] file = FileUtility.getFileFromInternalResources("Files/vocabulary/" + system + ".csv");

        MockHttpServletRequestBuilder requestBuilder = multipart("/v1/terminology")
                .file(new MockMultipartFile("file", system + ".csv", "text/xml", file))
                .param("version", version)
                .param("releaseDate", new SimpleDateFormat("yyyy-MM-dd").format(releaseDate))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        mvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();

        Date newReleaseDate = new Date();

        requestBuilder = multipart("/v1/terminology")
                .file(new MockMultipartFile("file", system + ".csv", "text/xml", file))
                .param("version", version)
                .param("releaseDate", new SimpleDateFormat("yyyy-MM-dd").format(newReleaseDate))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        requestBuilder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        mvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();
    }

    @Test
    void updateTerminologyWrongVersion() throws Exception {
        String version = "1.0.0";
        Date releaseDate = new Date();
        String system = "Terminology_10Items";
        byte[] file = FileUtility.getFileFromInternalResources("Files/vocabulary/" + system + ".csv");

        MockHttpServletRequestBuilder requestBuilder = multipart("/v1/terminology")
                .file(new MockMultipartFile("file", system + ".csv", "text/xml", file))
                .param("version", version)
                .param("releaseDate", new SimpleDateFormat("yyyy-MM-dd").format(releaseDate))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        mvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();

        Date newReleaseDate = new Date();

        String wrongVersion = "1.0.1";
        requestBuilder = multipart("/v1/terminology")
                .file(new MockMultipartFile("file", system + ".csv", "text/xml", file))
                .param("version", wrongVersion)
                .param("releaseDate", new SimpleDateFormat("yyyy-MM-dd").format(newReleaseDate))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        requestBuilder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        MvcResult result = mvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
        assertEquals(DocumentNotFoundException.class, result.getResolvedException().getClass());
    }

    @Test
    void deleteTerminologyTest() throws Exception {
        String version = "1.0.0";
        Date releaseDate = new Date();
        String system = "Terminology_10Items";
        byte[] file = FileUtility.getFileFromInternalResources("Files/vocabulary/" + system + ".csv");

        MockHttpServletRequestBuilder requestBuilder = multipart("/v1/terminology")
                .file(new MockMultipartFile("file", system + ".csv", "text/xml", file))
                .param("version", version)
                .param("releaseDate", new SimpleDateFormat("yyyy-MM-dd").format(releaseDate))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        mvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();

        MvcResult result = mvc.perform(delete("/v1/terminology/{system}", system)).andExpect(status().isOk()).andReturn();
        Document response = Document.parse(result.getResponse().getContentAsString());
        
        assertEquals(10, response.get("deletedItems"), "Every inserted item should be deleted");

        List<TerminologyETY> terms = getTerminologies();
        assertEquals(10, terms.size(), "Terminology should be deleted logically");

        terms.forEach(t -> {
            assertTrue(t.isDeleted(), "Terminology should be deleted logically");
        });
    }

    @Test
    void deleteUnexisting() throws Exception {
        String system = "UNEXISTING";
        MvcResult result = mvc.perform(delete("/v1/terminology/{system}", system)).andExpect(status().isNotFound()).andReturn();
        assertEquals(DocumentNotFoundException.class, result.getResolvedException().getClass());
    }

    /**
     * Returns all existing terminologies from database.
     * * @return List of TerminologyETY.
     */
    private List<TerminologyETY> getTerminologies() {
        return mongoTemplate.findAll(TerminologyETY.class);
    }
}
