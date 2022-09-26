package it.finanze.sanita.fse2.ms.edssrvdictionary;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.base.MockRequests.findTerminologyByIdMockRequest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl.TerminologyCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import lombok.extern.slf4j.Slf4j;





@WebMvcTest(TerminologyCTL.class)
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
@Slf4j
public class TerminologyControllerTest extends AbstractTest {

    private final String TEST_SYSTEM = "System_A"; 
    private final String TEST_CODE = "Code_A"; 
    private final String TEST_DESCRIPTION = "Description_A";

    @Autowired
	private MockMvc mvc; 
	
	@MockBean
	private Tracer tracer;

    @Autowired
    private ITerminologySRV terminologySRV;



    @Test
	void insertFileTerminology() throws Exception {

	    MockMultipartFile multipartFile = new MockMultipartFile("file", "terminology_post.csv", MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes()); 
	    
	    MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("http://127.0.0.1:9088/v1/terminology"); 
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setMethod("POST");
	            request.setParameter("file", "multipartFile");
	            return request;
	        }
	    }); 
	    
	    mvc.perform(builder
	            .file(new MockMultipartFile("file", multipartFile.getBytes()))
	            .contentType(MediaType.MULTIPART_FORM_DATA))
	            .andExpect(MockMvcResultMatchers.status().isOk()); 
	}
    

    @Test
	void findTerminologyByIdTest() throws Exception {

        ObjectId id = new ObjectId();
        final String TEST_TERMINOLOGY_ID = id.toString();

        TerminologyETY ety = new TerminologyETY();
		
		ety.setId(TEST_TERMINOLOGY_ID);
        ety.setSystem(TEST_SYSTEM);
        ety.setCode(TEST_CODE);
        ety.setDescription(TEST_DESCRIPTION);
		ety.setInsertionDate(new Date()); 
		ety.setLastUpdateDate(new Date()); 
		
		terminologySRV.insert(ety);
        
        mvc.perform(findTerminologyByIdMockRequest(TEST_TERMINOLOGY_ID)).andExpectAll(
                status().is2xxSuccessful()
            );

	} 
    
}
