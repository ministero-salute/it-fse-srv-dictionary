package it.finanze.sanita.fse2.ms.edssrvdictionary;


import brave.Tracer;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.ChunksDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.ChunksETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class TerminologyServiceTest extends AbstractTest {

	private final String TEST_SYSTEM = "System_A"; 
    private final String TEST_CODE = "Code_A"; 
    private final String TEST_DESCRIPTION = "Description_A";
    ObjectId id = new ObjectId();
    final String TEST_TERMINOLOGY_ID = id.toString();
	
	@MockBean
	private Tracer tracer;

    @Autowired
    private ITerminologySRV terminologySRV;

    @BeforeEach
    public void dataInsertion(){

        mongoTemplate.dropCollection(TerminologyETY.class);
		mongoTemplate.dropCollection(ChunksETY.class);
		mongoTemplate.dropCollection(SnapshotETY.class);

        TerminologyETY ety = new TerminologyETY();
		ety.setId(TEST_TERMINOLOGY_ID);
        ety.setSystem(TEST_SYSTEM);
        ety.setCode(TEST_CODE);
        ety.setDescription(TEST_DESCRIPTION);
		ety.setInsertionDate(new Date()); 
		ety.setLastUpdateDate(new Date()); 
		
		mongoTemplate.insert(ety);
    }

    @Test
    @DisplayName("Test insertion Terminology and get entity by id")
	void findTerminologyByIdTest() throws Exception {  
        TerminologyDocumentDTO terminology = terminologySRV.getTerminologyById(TEST_TERMINOLOGY_ID);
        assertNotNull(terminology);
        assertEquals(TEST_SYSTEM, terminology.getSystem());
        assertEquals(TEST_CODE, terminology.getCode());
	}

    @Test
    @DisplayName("Test Terminology elimination by id")
    void deleteTerminologyByIdTest() throws Exception{
        int size = terminologySRV.deleteTerminologyById(TEST_TERMINOLOGY_ID);
        assertEquals(1, size);
    }

    @Test
    @DisplayName("Test Terminology elimination by System")
    void deleteTerminologyBySystemTest() throws Exception{
        int size = terminologySRV.deleteTerminologiesBySystem(TEST_SYSTEM);
        assertEquals(1, size);
    }

    @Test
    @DisplayName("Test chunks creation")
    void createChunksTest() throws Exception{
        Date lastUpdate = new Date();
        ChunksDTO chunks = terminologySRV.createChunks(lastUpdate);
        assertNotNull(chunks);
    }
        

}


    