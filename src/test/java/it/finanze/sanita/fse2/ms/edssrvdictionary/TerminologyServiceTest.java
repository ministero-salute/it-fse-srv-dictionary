/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary;


import brave.Tracer;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.chunks.snapshot.ChunksDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.ITerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.ChunksETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class TerminologyServiceTest extends AbstractTest {

	private final String TEST_TERMINOLOGY_SYSTEM = "System_A"; 
    private final String TEST_TERMINOLOGY_CODE = "Code_A"; 
    private final String TEST_TERMINOLOGY_DESCRIPTION = "Description_A";
    private final String TEST_TERMINOLOGY_VERSION = "Version_A";
    ObjectId termId = new ObjectId();
    final String TEST_TERMINOLOGY_ID = termId.toString();
    final String TEST_FILENAME = "test_terminolgy";
	
	@MockBean
	private Tracer tracer;

    @Autowired
    private ITerminologySRV terminologySRV;

    @Autowired
    private ITerminologyRepo terminologyRepo;

    @BeforeEach
    public void dataInsertion(){

        mongoTemplate.dropCollection(TerminologyETY.class);
		mongoTemplate.dropCollection(ChunksETY.class);
		mongoTemplate.dropCollection(SnapshotETY.class);

        TerminologyETY termEty = new TerminologyETY();
		termEty.setId(TEST_TERMINOLOGY_ID);
        termEty.setSystem(TEST_TERMINOLOGY_SYSTEM);
        termEty.setCode(TEST_TERMINOLOGY_CODE);
        termEty.setVersion(TEST_TERMINOLOGY_VERSION);
        termEty.setDescription(TEST_TERMINOLOGY_DESCRIPTION);
		termEty.setInsertionDate(new Date()); 
		termEty.setLastUpdateDate(new Date()); 

		mongoTemplate.insert(termEty);
    }

    @Test
    @DisplayName("Test Terminology insertion by id")
	void findTerminologyByIdTest() throws Exception {  
        TerminologyDocumentDTO terminology = terminologySRV.getTerminologyById(TEST_TERMINOLOGY_ID);
        assertNotNull(terminology);
        assertEquals(TEST_TERMINOLOGY_SYSTEM, terminology.getSystem());
        assertEquals(TEST_TERMINOLOGY_CODE, terminology.getCode());
        boolean existsBySystem = terminologyRepo.existsBySystem(TEST_TERMINOLOGY_SYSTEM);
        assertTrue(existsBySystem);
        boolean existsBySystemAndVersion = terminologyRepo.existsBySystemAndVersion(TEST_TERMINOLOGY_SYSTEM, TEST_TERMINOLOGY_VERSION);
        assertTrue(existsBySystemAndVersion);
	}

    @Test
    @DisplayName("Test Terminology elimination by id")
    void deleteTerminologyByIdTest() throws Exception{
        int size = terminologySRV.deleteTerminologyById(TEST_TERMINOLOGY_ID);
        assertEquals(1, size);
        List<TerminologyETY> deletions = terminologyRepo.getEveryActiveTerminology();
        assertEquals(0, deletions.size());
    }

    @Test
    @DisplayName("Test Terminology elimination by System")
    void deleteTerminologyBySystemTest() throws Exception{
        int size = terminologySRV.deleteTerminologiesBySystem(TEST_TERMINOLOGY_SYSTEM);
        assertEquals(1, size);
    }

    @Test
    @DisplayName("Test chunks creation")
    void createChunksTest() throws Exception{
        ChunksDTO chunks = terminologySRV.createChunks(null);
        assertNotNull(chunks);
        SnapshotETY snapshot = terminologySRV.getChunks(chunks.getSnapshotID());
        assertNotNull(snapshot);
        assertEquals(String.class, snapshot.getId().getClass());
        assertEquals(chunks.getSnapshotID(), snapshot.getId());
        List<TerminologyDocumentDTO> insDocs = terminologySRV.getTermsByChunkIns(chunks.getSnapshotID(), 0);
        assertEquals(1, insDocs.size());
        assertThrows(OutOfRangeException.class, () -> terminologySRV.getTermsByChunkDel(chunks.getSnapshotID(), 0));
            
    }

    @Test
    @DisplayName("Test get Insertions")
    void getInsertionsTest() throws Exception{
        List<ChangeSetDTO> insertions = terminologySRV.getInsertions(null);
        assertNotNull(insertions);
    }

    @Test
    @DisplayName("Test get Terminologies")
    void getTerminologiesTest() throws Exception{
        SimpleImmutableEntry<Page<TerminologyETY>, List<TerminologyDocumentDTO>> entry = terminologySRV.getTerminologies(0, 10, TEST_TERMINOLOGY_SYSTEM);
        assertNotNull(entry);
        assertNotNull(entry.getKey());
        assertNotNull(entry.getValue());
    }
    
}


    