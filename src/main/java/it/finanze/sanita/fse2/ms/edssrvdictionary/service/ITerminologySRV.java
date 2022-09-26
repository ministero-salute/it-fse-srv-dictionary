package it.finanze.sanita.fse2.ms.edssrvdictionary.service;


import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.VocabularyDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;

/**
 *	@author vincenzoingenito
 *	@author Riccardo Bonesi
 *
 *	Vocabulary interface service.
 */
public interface ITerminologySRV extends IChangeSetSRV {

	/**
	 * Insert terminology in database
	 * @param ety
	 * @return
	 */
	TerminologyETY insert(TerminologyETY ety);
	
	/**
	 * Insert a list of Terminology in database
	 * @param etys
	 */
	void insertAll(List<TerminologyETY> etys);
	
	/**
	 * Save new Vocaboulary System
	 * @param vocabulariesDTO
	 * @return
	 */
	Integer saveNewVocabularySystems(List<VocabularyDTO> vocabulariesDTO);

	/**
	 * Retrieves the Terminology by identifier
	 * @param id
	 * @return TerminologyDocumentDTO
	 * @throws OperationException
	 * @throws DocumentNotFoundException
	 * @throws ObjectIdNotValidException
	 */
	TerminologyDocumentDTO findById(String id) throws OperationException, DocumentNotFoundException;


	/**
	 * Insert Terminologies in database from file
	 * @param file
	 */
	void uploadTerminologyFile(MultipartFile file) throws IOException;
	

}
