package it.finanze.sanita.fse2.ms.edssrvdictionary;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.ITerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.ChunksETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;


@SpringBootTest
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class TerminologyServiceTest extends AbstractTest {

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
    @DisplayName("Test insertion Terminology and get entity by id")
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
        
        TerminologyDocumentDTO terminology = terminologySRV.findById(TEST_TERMINOLOGY_ID);
        assertNotNull(terminology);
        assertEquals(TEST_SYSTEM, terminology.getSystem());
        assertEquals(TEST_CODE, terminology.getCode());

	} 

    @Test
    @DisplayName("Test creation Terminology from file")
    void uploadTerminologyFileTest() throws IOException {


        dropVocabularyCollection();
		String csvFileName = "LoincTableCore.csv";


        Path path = Paths.get("src" + File.separator + "test" + File.separator + "resources" + File.separator + "Files" + File.separator + "vocabulary" + File.separator + csvFileName);
        String contentType = "text/plain";
        byte[] content = Files.readAllBytes(path);

        MultipartFile file = new MockMultipartFile(csvFileName, csvFileName, contentType, content);

        assertDoesNotThrow(() -> terminologySRV.uploadTerminologyFile(file));


        

    }    
}
