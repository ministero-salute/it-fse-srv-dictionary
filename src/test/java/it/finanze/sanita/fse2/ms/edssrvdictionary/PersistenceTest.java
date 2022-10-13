package it.finanze.sanita.fse2.ms.edssrvdictionary;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyBuilderDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyFileEntryDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.VocabularyDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.FileUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = {Constants.ComponentScan.BASE})
@ActiveProfiles(Constants.Profile.TEST)
public class PersistenceTest extends AbstractTest {
	
	@Autowired
	private ITerminologySRV vocabularySRV;
	
	@Test
	@DisplayName("Populate and get vocabulary collection")
	void populateVocabularyCollection() {
		dropVocabularyCollection();
		String csvFileName = "LoincTableCore.csv";
		byte[] csvContent = FileUtility.getFileFromInternalResources("Files" + File.separator + "vocabulary" + File.separator + csvFileName);
		InputStream targetStream = new ByteArrayInputStream(csvContent);
		Reader reader = new InputStreamReader(targetStream);
		List<TerminologyBuilderDTO> vocabularyListDTO = buildDTOFromCsv(reader);
		vocabularyListDTO.remove(0);

		Date insertionDate = new Date();

		List<TerminologyETY> listToSave = new ArrayList<>();
		for(TerminologyBuilderDTO vocabularyDTO : vocabularyListDTO) {
			TerminologyETY ety = new TerminologyETY();
			ety.setCode(vocabularyDTO.getCode());
			ety.setDescription(vocabularyDTO.getDescription());
			ety.setSystem(vocabularyDTO.getSystem());
			ety.setInsertionDate(insertionDate);
			ety.setLastUpdateDate(insertionDate);
			listToSave.add(ety);
			
		}
		
				
		assertNotNull(listToSave);
		vocabularySRV.insertAll(listToSave);

		
		List<VocabularyDTO> vocabulariesDTO = new ArrayList<>();
		List<TerminologyFileEntryDTO> entriesDTO = new ArrayList<>();
		
		TerminologyFileEntryDTO entryDTO = TerminologyFileEntryDTO.builder()
				.code("")
				.description("")
				.build();
		
		entryDTO.setCode("genericCode");
		entryDTO.setDescription("genericDescription");
		
		
		VocabularyDTO voc = VocabularyDTO.builder()
				.system("")
				.entryDTO(entriesDTO)
				.build();
		
		entriesDTO.add(entryDTO);
		
		voc.setEntryDTO(entriesDTO);
		voc.setSystem("2.16.840.1.113883.6.1");

		vocabulariesDTO.add(voc);
		assertDoesNotThrow(() -> {
			Integer vocSysSaved = vocabularySRV.saveNewVocabularySystems(vocabulariesDTO);
			assertNotNull(vocSysSaved);
		});
	}
	
	@Test
	@DisplayName("test equals terminology entities ")	
	void equalsTerminologyEty() {
		//first obj
		TerminologyETY ety1 = new TerminologyETY();
		TerminologyBuilderDTO vocabularyDTO1 = new TerminologyBuilderDTO();
		
		vocabularyDTO1.setCode("code");
		vocabularyDTO1.setDescription("descrption");
		
		ety1.setCode(vocabularyDTO1.getCode());
		ety1.setDescription(vocabularyDTO1.getDescription());
		ety1.setSystem("2.16.840.1.113883.6.1");
		
		//second obj
		TerminologyETY ety2 = new TerminologyETY();
		TerminologyBuilderDTO vocabularyDTO2 = new TerminologyBuilderDTO();
		
		vocabularyDTO2.setCode("code");
		vocabularyDTO2.setDescription("description");
		
		ety2.setCode(vocabularyDTO2.getCode());
		ety2.setDescription(vocabularyDTO2.getDescription());
		ety2.setSystem("2.16.840.1.113883.6.1");

		assertNotEquals(null, ety1);
		assertNotEquals(null, ety2);
		assertEquals(ety2, ety1);

		
	}
	
	@Test
	@DisplayName("Insert Terminilogy ETY Test")
	void insertTerminologyTest() {
		String csvFileName = "LoincTableCore.csv";
		byte[] csvContent = FileUtility.getFileFromInternalResources("Files" + File.separator + "vocabulary" + File.separator + csvFileName);
		InputStream targetStream = new ByteArrayInputStream(csvContent);
		Reader reader = new InputStreamReader(targetStream);
		List<TerminologyBuilderDTO> vocabularyListDTO = buildDTOFromCsv(reader);
		vocabularyListDTO.remove(0);
		
		TerminologyBuilderDTO vocabularyDTO = vocabularyListDTO.get(0); 

		Date insertionDate = new Date();
		
		TerminologyETY ety = new TerminologyETY();
		ety.setCode(vocabularyDTO.getCode());
		ety.setDescription(vocabularyDTO.getDescription());
		ety.setSystem("2.16.840.1.113883.6.1"); 
		ety.setInsertionDate(insertionDate);
		ety.setLastUpdateDate(insertionDate);
		
		TerminologyETY insertedEty = vocabularySRV.insert(ety); 
		
		assertNotNull(insertedEty); 
	}

	
}
