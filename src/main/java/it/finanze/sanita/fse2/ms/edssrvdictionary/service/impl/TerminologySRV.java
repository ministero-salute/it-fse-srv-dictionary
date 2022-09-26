package it.finanze.sanita.fse2.ms.edssrvdictionary.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBeanBuilder;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyBuilderDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyFileEntryDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.VocabularyDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.ITerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ChangeSetUtility;
import lombok.extern.slf4j.Slf4j;

/**
 *	@author vincenzoingenito
 *	@author Riccardo Bonesi
 *
 *	Terminology service.
 */
@Service
@Slf4j
public class TerminologySRV implements ITerminologySRV {

	@Autowired
	private ITerminologyRepo terminologyRepo;
	
//	@Autowired
//	private IDictionaryRepo dictionaryRepo;
	
	@Override
	public TerminologyETY insert(final TerminologyETY ety) {
		TerminologyETY output = null;
		try {
			output = terminologyRepo.insert(ety);
		} catch(Exception ex) {
			log.error("Error inserting ety vocabulary :" , ex);
			throw new BusinessException("Error inserting ety vocabulary :" , ex);
		}
		return output;
	}

	@Override
	public void insertAll(List<TerminologyETY> etys) {
		try {
			terminologyRepo.insertAll(etys);
		} catch(Exception ex) {
			log.error("Error inserting all ety vocabulary :" , ex);
			throw new BusinessException("Error inserting all ety vocabulary :" , ex);
		}
	}


	@Override
	public Integer saveNewVocabularySystems(final List<VocabularyDTO> vocabulariesDTO) {
		Integer recordSaved = 0;
		if(vocabulariesDTO!=null && !vocabulariesDTO.isEmpty()) {
			for(VocabularyDTO entry : vocabulariesDTO) {
				boolean exist = terminologyRepo.existsBySystem(entry.getSystem());
				
				List<TerminologyETY> vocabularyETYS = buildDtoToETY(entry.getEntryDTO(), entry.getSystem());
				if(Boolean.TRUE.equals(exist)) {
					log.info("Save new version vocabulary");
					List<String> codeList = vocabularyETYS.stream().map(e-> e.getCode()).collect(Collectors.toList());
					List<TerminologyETY> vocabularyFinded = terminologyRepo.findByInCodeAndSystem(codeList,entry.getSystem());
					List<TerminologyETY> vocabularyToSave = minus(vocabularyETYS, vocabularyFinded);
					terminologyRepo.insertAll(vocabularyToSave);
					recordSaved = recordSaved+vocabularyToSave.size();
				} else {
					terminologyRepo.insertAll(vocabularyETYS);
					recordSaved = recordSaved+vocabularyETYS.size();
				}
			}
		}
		log.info("Vocabulary saved on db : " + recordSaved);
		return recordSaved;
	}
	
	
	private List<TerminologyETY> minus(List<TerminologyETY> base, List<TerminologyETY> toRemove) {
		List<TerminologyETY> out = new ArrayList<>(); 
		for (TerminologyETY s:base) {
			if (!toRemove.contains(s) && s!=null) {
				out.add(s);
			} 
		}
		return out;    
	}
	
	private List<TerminologyETY> buildDtoToETY(List<TerminologyFileEntryDTO> vocabularyEntriesDTO, String system) {
		List<TerminologyETY> output = new ArrayList<>();
		for(TerminologyFileEntryDTO vocabularyEntryDTO : vocabularyEntriesDTO) {
			TerminologyETY ety = new TerminologyETY();
			ety.setId(null);
			ety.setCode(vocabularyEntryDTO.getCode());
			ety.setDescription(vocabularyEntryDTO.getDescription());
			ety.setSystem(system);
			output.add(ety);
		}
		return output;
	}


	@Override
	public List<ChangeSetDTO> getInsertions(Date lastUpdate) throws OperationException {

		List<TerminologyETY> insertions;

		if (lastUpdate != null) {
			insertions = terminologyRepo.getInsertions(lastUpdate);
		} else {
			insertions = terminologyRepo.getEveryActiveTerminology();
		}

		return insertions.stream().map(ChangeSetUtility::terminologyToChangeset).collect(Collectors.toList());

	}

    @Override
	public List<ChangeSetDTO> getDeletions(Date lastUpdate) throws OperationException {
		try {

			List<ChangeSetDTO> deletions = new ArrayList<>();

			if (lastUpdate != null) {
				List<TerminologyETY> deletionsETY = terminologyRepo.getDeletions(lastUpdate);
				deletions = deletionsETY.stream().map(ChangeSetUtility::terminologyToChangeset)
						.collect(Collectors.toList());

			}

			return deletions;

		} catch (Exception e) {
			log.error(Constants.Logs.ERROR_UNABLE_FIND_DELETIONS, e); 
			throw new BusinessException(Constants.Logs.ERROR_UNABLE_FIND_DELETIONS, e); 
		}
	}


	@Override
	public TerminologyDocumentDTO findById(String id) throws OperationException, DocumentNotFoundException {

		TerminologyETY output = terminologyRepo.findById(id);

        if (output == null) {
            throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
        }
		
		return TerminologyDocumentDTO.fromEntity(output);
	}

	@Override
	public void uploadTerminologyFile(MultipartFile file) throws IOException {

		byte [] byteArr = file.getBytes();
		InputStream targetStream = new ByteArrayInputStream(byteArr);

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
		
		terminologyRepo.insertAll(listToSave);

		log.info("Successfully inserted " + listToSave.size() + " Termonologies");
	}

	private List<TerminologyBuilderDTO> buildDTOFromCsv(Reader reader){
		List<TerminologyBuilderDTO> output = null; 
		output = new CsvToBeanBuilder(reader).withType(TerminologyBuilderDTO.class).withSeparator(',').build().parse();
		return output;
	}
	
	
}
