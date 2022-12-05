/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.service;


import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.csv.vocabulary.VocabularyDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;

/**
 *
 *	Vocabulary interface service.
 */
public interface ITerminologySRV extends IChangeSetSRV {

	Integer saveNewVocabularySystems(List<VocabularyDTO> vocabulariesDTO) throws OperationException;

	/**
	 * Retrieves the document by identifier
	 * @param id The document id
	 * @return The document matching the identifier
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no document matching the id is found
	 */
	TerminologyDocumentDTO getTerminologyById(String id) throws OperationException, DocumentNotFoundException;

	/**
	 * Delete the document by identifier
	 * @param id The document it
	 * @return The number of terminologies deleted
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no document matching id is found
	 */
	int deleteTerminologyById(String id) throws DocumentNotFoundException, OperationException;

	/**
	 * Aggregates and return documents by chunk
	 * @param id The snapshot instance
	 * @param index The chunk index
	 * @return The terminologies associated with the chunk
 	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no snapshot matching the given id exists
	 * @throws OutOfRangeException If no chunk matching the given index exists
	 * @throws DataIntegrityException If the database output does not match with the requested ids
	 */
	List<TerminologyDocumentDTO> getTermsByChunkIns(String id, int index) throws  DocumentNotFoundException, OperationException, OutOfRangeException, DataIntegrityException;

	/**
	 * Aggregates and return documents by chunk
	 * @param id The snapshot instance
	 * @param index The chunk index
	 * @return The terminologies associated with the chunk
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no snapshot matching the given id exists
	 * @throws OutOfRangeException If no chunk matching the given index exists
	 */
	List<ObjectId> getTermsByChunkDel(String id, int index) throws  DocumentNotFoundException, OperationException, OutOfRangeException;

	/**
	 * Returns terminologies matching system with pagination
	 * @param system System identifier
	 * @param page Page index
	 * @param limit Page max available items
	 * @return The page data and the document list
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no document matching system is found
	 * @throws OutOfRangeException If the provided page index is not valid
	 */
	SimpleImmutableEntry<Page<TerminologyETY>, List<TerminologyDocumentDTO>> getTerminologies(int page, int limit, String system) throws OperationException, DocumentNotFoundException, OutOfRangeException;

 
	/**
	 * Insert terminologies inside the database using a .csv file
	 * @param file A .csv file representing terminologies
	 * @param version Version identifier
	 * @param releaseDate Release Date of the CodeSystem
	 * @return The number of terminologies inserted
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentAlreadyPresentException If the given system is already inserted
	 * @throws DataProcessingException If an error occurs while converting raw data to entity type
	 * @throws InvalidContentException  If the file is empty or null
	 */
	int uploadTerminologyCsv(MultipartFile file, String version, Date releaseDate) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, InvalidContentException;
	
	/**
	 * Update given terminologies using the new version and the same system
	 * @param file A .csv file representing terminologies
	 * @param version Version identifier
	 * @param releaseDate Release Date of the CodeSystem
	 * @return The number of terminologies updated
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no document matching system is found
	 * @throws DataProcessingException If an error occurs while converting raw data to entity type
	 * @throws DataIntegrityException If database output is not the expected one
	 * @throws DocumentAlreadyPresentException If the given version already exists
	 * @throws InvalidContentException If the file is empty or null
	 */
	int updateTerminologyCsv(MultipartFile file, String version, Date releaseDate) throws DocumentNotFoundException, OperationException, DataProcessingException, DataIntegrityException, DocumentAlreadyPresentException, InvalidContentException;
	
	/**
	 * Delete terminologies matching system
	 * @param system System identifier
	 * @return The number of terminologies deleted
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no document matching system is found
	 * @throws DataIntegrityException If database output is not the expected one
	 */
	int deleteTerminologiesBySystem(String system) throws DocumentNotFoundException, OperationException, DataIntegrityException;

}
