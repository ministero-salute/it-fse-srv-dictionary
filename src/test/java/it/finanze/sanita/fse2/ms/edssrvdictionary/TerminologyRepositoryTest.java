package it.finanze.sanita.fse2.ms.edssrvdictionary;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl.TerminologyCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.ITerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.ChunksETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import lombok.extern.slf4j.Slf4j;

@WebMvcTest(TerminologyCTL.class)
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
@Slf4j
public class TerminologyRepositoryTest extends AbstractTest{
    
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


}
