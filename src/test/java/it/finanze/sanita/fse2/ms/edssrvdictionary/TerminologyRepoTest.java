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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.ITerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(Constants.Profile.TEST)
class TerminologyRepoTest {

	@Autowired
	private ITerminologyRepo repository;
	
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
	@DisplayName("Find by id test")
	void findByIdTest() throws OperationException {
		
		insertTerm();
		
		List<TerminologyETY> terms = repository.getEveryActiveTerminology();
		TerminologyETY response = repository.findById(terms.get(0).getId());
		assertEquals(terms.get(0).getId(), response.getId());
		assertEquals(description, response.getDescription());
		assertEquals(insertionDate, response.getInsertionDate());
		assertEquals(lastUpdateDate, response.getLastUpdateDate());
		assertEquals(system, response.getSystem());
		assertEquals(version, response.getVersion());
		assertEquals(releaseDate, response.getReleaseDate());
	}
	
	@Test
	@DisplayName("Find by system test")
	void findBySystemTest() throws OperationException {
		
		insertTerm();
		
		List<TerminologyETY> response = repository.findBySystem(system);
		assertEquals(description, response.get(0).getDescription());
		assertEquals(insertionDate, response.get(0).getInsertionDate());
		assertEquals(lastUpdateDate, response.get(0).getLastUpdateDate());
		assertEquals(system, response.get(0).getSystem());
		assertEquals(version, response.get(0).getVersion());
		assertEquals(releaseDate, response.get(0).getReleaseDate());
	}
	
	@Test
	@DisplayName("Get by system test")
	void getBySystem() throws OperationException {
		
		insertTerm();
		
		Page<TerminologyETY> pages = repository.getBySystem(system, PageRequest.of(0, 1));
		List<TerminologyETY> terms = pages.getContent();
		
		assertEquals(description, terms.get(0).getDescription());
		assertEquals(insertionDate, terms.get(0).getInsertionDate());
		assertEquals(lastUpdateDate, terms.get(0).getLastUpdateDate());
		assertEquals(system, terms.get(0).getSystem());
		assertEquals(version, terms.get(0).getVersion());
		assertEquals(releaseDate, terms.get(0).getReleaseDate());
	}
	
	@Test
	@DisplayName("Delete by system test")
	void deleteBySystemTest() throws DataIntegrityException, OperationException {
		
		insertTerm();
		
		List<TerminologyETY> response = repository.deleteBySystem(system);
		assertEquals(description, response.get(0).getDescription());
		assertEquals(insertionDate, response.get(0).getInsertionDate());
		assertEquals(lastUpdateDate, response.get(0).getLastUpdateDate());
		assertEquals(system, response.get(0).getSystem());
		assertEquals(version, response.get(0).getVersion());
		assertEquals(releaseDate, response.get(0).getReleaseDate());
	}
	
	@Test
	@DisplayName("Update by system test")
	void updateBySystemTest() throws DataIntegrityException, OperationException, DocumentNotFoundException {
		
		insertTerm();
		TerminologyETY ety = new TerminologyETY();
		ety.setSystem("New system");
		ety.setDescription("New description");
		ety.setVersion(version);
		List<TerminologyETY> newTerms = new ArrayList<TerminologyETY>();
		newTerms.add(ety);
		
		List<TerminologyETY> response = repository.updateBySystem(system, version, releaseDate, newTerms);
		assertEquals("New description", response.get(0).getDescription());
		assertEquals("New system", response.get(0).getSystem());
		assertEquals(version, response.get(0).getVersion());
	}
	
	@Test
	@DisplayName("Exists by system test")
	void existsBySystemTest() throws OperationException {
		
		insertTerm();
		assertTrue(repository.existsBySystem(system));
	}
	
	@Test
	@DisplayName("Exists by system, version, release test")
	void existsBySystemVersionAndReleaseTest() throws OperationException {
		
		insertTerm();
		assertTrue(repository.existsBySystemVersionAndRelease(system, version, releaseDate));
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
