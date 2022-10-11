package it.finanze.sanita.fse2.ms.edssrvdictionary;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.base.MockRequests.findTerminologyByIdMockRequest;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.base.MockRequests.getTerminologyByChunkDelMockRequest;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.base.MockRequests.getTerminologyByChunkInsMockRequest;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl.TerminologyCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.ChunkOutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.ITerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.ChunksETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;





@WebMvcTest(TerminologyCTL.class)
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
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

	@Autowired
	private ITerminologyRepo terminologyRepo;


	@BeforeAll
    public void setup() throws Exception {
		mongoTemplate.dropCollection(TerminologyETY.class);
		mongoTemplate.dropCollection(ChunksETY.class);
		mongoTemplate.dropCollection(SnapshotETY.class);


    } 


	@AfterAll
    public void teardown() {
        mongoTemplate.dropCollection(TerminologyETY.class);
		mongoTemplate.dropCollection(ChunksETY.class);
		mongoTemplate.dropCollection(SnapshotETY.class);
    } 

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
	            .andExpect(MockMvcResultMatchers.status().is(201)); 
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



	// Chunk insertion tests //



	@Test
	@Disabled
	void getTermsByChunkInsValidTest() throws Exception{
		SnapshotETY snapshot = SnapshotETY.empty();
		ChunksETY chunk = ChunksETY.empty();

		chunk.setAvgSize(1);
		chunk.setCount(1);
		chunk.setItems(1);
		snapshot.setInsertions(chunk);

		terminologyRepo.insertSnapshot(snapshot);

		

		MvcResult out = mvc.perform(getTerminologyByChunkInsMockRequest(snapshot.getId().toString(),0)).andReturn();

		System.out.println(out.getResponse().getContentAsString());
		

}

@Test
@Disabled
	void getTermsByChunkInsWithChunkOutOfRangeTest() throws Exception{
		SnapshotETY snapshot = SnapshotETY.empty();
		ChunksETY chunk = ChunksETY.empty();

		chunk.setAvgSize(1);
		chunk.setCount(1);
		chunk.setItems(1);
		snapshot.setInsertions(chunk);

		terminologyRepo.insertSnapshot(snapshot);


	assertThrows(ChunkOutOfRangeException.class,()-> getTerminologyByChunkInsMockRequest(snapshot.getId().toString(),1000));


}



@Test
@Disabled
void getTermsByChunkInsInvalidIdTest() throws Exception{
	SnapshotETY snapshot = SnapshotETY.empty();
	ChunksETY chunk = ChunksETY.empty();

	chunk.setAvgSize(1);
	chunk.setCount(1);
	chunk.setItems(0);
	chunk.setItems(1);
	snapshot.setInsertions(chunk);

	terminologyRepo.insertSnapshot(snapshot);

	mvc.perform(getTerminologyByChunkInsMockRequest(INVALID_SNAPSHOT_ID,0)).andExpectAll(
		status().is4xxClientError()
		);

}





	// Chunk deletion tests //

@Test
@Disabled
void getTermsByChunkDelValidTest() throws Exception{
	SnapshotETY snapshot = SnapshotETY.empty();
	ChunksETY chunk = ChunksETY.empty();

	chunk.setAvgSize(10);
	chunk.setCount(1);
	chunk.setItems(0);
	chunk.setItems(1);
	snapshot.setDeletions(chunk);

	terminologyRepo.insertSnapshot(snapshot);

	mvc.perform(getTerminologyByChunkDelMockRequest(snapshot.getId(),1)).andExpectAll(
		status().is2xxSuccessful()
		);

}

@Test
@Disabled
void getTermsByChunkDelInvalidIdTest() throws Exception{
	SnapshotETY snapshot = SnapshotETY.empty();
	ChunksETY chunk = ChunksETY.empty();

	chunk.setAvgSize(10);
	chunk.setCount(1);
	chunk.setItems(0);
	chunk.setItems(1);
	snapshot.setDeletions(chunk);

	terminologyRepo.insertSnapshot(snapshot);

	mvc.perform(getTerminologyByChunkInsMockRequest(INVALID_SNAPSHOT_ID,1)).andExpectAll(
		status().is4xxClientError()
		);

}



@Test
@Disabled
void getTermsByChunkDelWithChunkOutOfRangeTest() throws Exception{
		SnapshotETY snapshot = SnapshotETY.empty();
		ChunksETY chunk = ChunksETY.empty();

		chunk.setAvgSize(1);
		chunk.setCount(1);
		chunk.setItems(1);
		snapshot.setDeletions(chunk);

		terminologyRepo.insertSnapshot(snapshot);


		assertThrows(ChunkOutOfRangeException.class,()-> getTerminologyByChunkDelMockRequest(snapshot.getId().toString(),1000));


}

    
}
