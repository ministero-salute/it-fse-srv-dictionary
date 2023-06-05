package it.finanze.sanita.fse2.ms.edssrvdictionary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.google.gson.Gson;

import io.swagger.models.HttpMethod;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.DelDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.webscraping.PostSingleDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.WebScrapingETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.FileUtility;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(Constants.Profile.TEST)
public class WebScrapingControllerTest {
    
    @Autowired
    MockMvc mvc;

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    void setup() {
        mongoTemplate.dropCollection(WebScrapingETY.class);
    }

    @ParameterizedTest
    @CsvSource({"systemTest1,urlTest1", "systemTest2,urlTest2", "systemTest3,urlTest3"})
    void insertSingleDocument(String system, String url) throws Exception {
        MvcResult result = mvc.perform(post("/v1/web-scraping/{system}/{url}", system, url))
            .andExpect(status().isCreated()).andReturn();

        PostSingleDTO response = new Gson().fromJson(result.getResponse().getContentAsString(), PostSingleDTO.class);
        assertEquals(system, response.getDto().getSystem());
        assertEquals(url, response.getDto().getUrl());
    }

    @Test
    void insertByCSV() throws Exception {
        byte[] file = FileUtility.getFileFromInternalResources("Files/web-scraping.csv");

        MockHttpServletRequestBuilder requestBuilder = multipart("/v1/web-scraping/insert-multi")
                .file(new MockMultipartFile("file", "web-scraping.csv", "text/csv", file));

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isCreated()).andReturn();

        PostDocsResDTO response = new Gson().fromJson(result.getResponse().getContentAsString(), PostDocsResDTO.class);
        assertEquals(9, response.getInsertedItems(), "The file contains 9 rows, the items inserted should be 9");

        List<WebScrapingETY> documents = getDocuments();
        assertEquals(9, documents.size(), "The file contains 9 rows, the items inserted should be 9");      
    }

    @Test
    void deleteBySystem() throws Exception {
        String system = "systemTest";
        String url = "urlTest";

        WebScrapingETY ety = new WebScrapingETY(null, system, url, false);
        mongoTemplate.insert(ety);

        MvcResult result = mvc.perform(delete("/v1/web-scraping/delete/{system}", system))
        .andExpect(status().isOk()).andReturn();

        DelDocsResDTO response = new Gson().fromJson(result.getResponse().getContentAsString(), DelDocsResDTO.class);
        assertEquals(1, response.getDeletedItems());
    }

    @Test
    void deleteByCSV() throws Exception {
        byte[] file = FileUtility.getFileFromInternalResources("Files/web-scraping.csv");
		// Parse entities
		List<WebScrapingETY> entities = WebScrapingETY.fromCSV(file);
        // Insert all documents in db
        mongoTemplate.insertAll(entities);

        MockHttpServletRequestBuilder requestBuilder = multipart("/v1/web-scraping/delete-multi")
                .file(new MockMultipartFile("file", "web-scraping.csv", "text/csv", file))
                .with(req -> {
                    req.setMethod(HttpMethod.DELETE.name());
                    return req;
                });
        
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn();

        DelDocsResDTO response = new Gson().fromJson(result.getResponse().getContentAsString(), DelDocsResDTO.class);
        assertEquals(9, response.getDeletedItems(), "The file contains 9 rows, the items deleted should be 9");

        List<WebScrapingETY> documents = getDocuments();
        assertEquals(9, documents.size(), "The file contains 9 rows, the items deleted should be 9");
    }

    private List<WebScrapingETY> getDocuments() {
        return mongoTemplate.findAll(WebScrapingETY.class);
    }
}
