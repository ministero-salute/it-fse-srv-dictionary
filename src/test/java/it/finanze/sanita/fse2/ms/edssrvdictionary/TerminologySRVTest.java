package it.finanze.sanita.fse2.ms.edssrvdictionary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(Constants.Profile.TEST)
class TerminologySRVTest {
	
	@Autowired
	private ITerminologySRV service;
	
	@Autowired
    MongoTemplate mongoTemplate;
	
	static final String description = "description_test";
	static final String system = "system_test";
	static final String version = "1.0.0";
	static final Date insertionDate = new Date();
	static final Date lastUpdateDate = new Date();
	static final Date releaseDate = new Date();
	
	@BeforeEach
    void setup() {
        mongoTemplate.dropCollection(TerminologyETY.class);
    }

	@Test
	void getTerminologies() throws OperationException, DocumentNotFoundException, OutOfRangeException {
		
		insertTerm();
		
		SimpleImmutableEntry<Page<TerminologyETY>, List<TerminologyDocumentDTO>> response = service.getTerminologies(0, 1, system);
		List<TerminologyDocumentDTO> docs = response.getValue();
		assertEquals(description, docs.get(0).getDescription());
		assertEquals(insertionDate, Date.from(docs.get(0).getInsertionDate().toInstant()));
		assertEquals(lastUpdateDate, Date.from(docs.get(0).getLastUpdateDate().toInstant()));
		assertEquals(system, docs.get(0).getSystem());
		assertEquals(version, docs.get(0).getVersion());
		assertEquals(releaseDate, Date.from(docs.get(0).getReleaseDate().toInstant()));
		
	}
	
	private void insertTerm() {
		TerminologyETY ety = new TerminologyETY();
		ety.setDescription(description);
		ety.setInsertionDate(insertionDate);
		ety.setLastUpdateDate(lastUpdateDate);
		ety.setSystem(system);
		ety.setVersion(version);
		ety.setReleaseDate(releaseDate);
		mongoTemplate.insert(ety);
	}
}
