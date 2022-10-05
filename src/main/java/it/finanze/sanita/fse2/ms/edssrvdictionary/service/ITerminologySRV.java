package it.finanze.sanita.fse2.ms.edssrvdictionary.service;


import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.VocabularyDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.ChunksTypeEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.ChunkOutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;

import org.springframework.beans.TypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
	 */
	TerminologyDocumentDTO findById(String id) throws OperationException, DocumentNotFoundException;


	/**
	 * Insert Terminologies in database from file
	 * @param file
	 */
	void uploadTerminologyFile(MultipartFile file) throws IOException;

	/**
	 * Aggregates and return documents by chunk
	 * @param id The snapshot instance
	 * @param type The chunk type (insert/delete)
	 * @param index The chunk index
	 * @return The terminologies associated with the chunk
	 * @throws TypeMismatchException if the enom type il mispelled
 	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no snapshot matching the given id exists
	 * @throws ChunkOutOfRangeException If no chunk matching the given index exists
	 * @throws DataIntegrityException If the database output does not match with the requested ids
	 */
	List<TerminologyDocumentDTO> getDocsByChunk(String id, ChunksTypeEnum type, int index) throws TypeMismatchException,  DocumentNotFoundException, OperationException, ChunkOutOfRangeException, DataIntegrityException;
}
