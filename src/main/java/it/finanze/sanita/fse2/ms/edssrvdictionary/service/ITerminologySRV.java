package it.finanze.sanita.fse2.ms.edssrvdictionary.service;


import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.VocabularyDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;
import org.bson.types.ObjectId;
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

	Integer saveNewVocabularySystems(List<VocabularyDTO> vocabulariesDTO) throws OperationException;

	TerminologyDocumentDTO findById(String id) throws OperationException, DocumentNotFoundException;

	int deleteTerminologyById(String id) throws DocumentNotFoundException, OperationException;

	Integer uploadTerminologyFile(MultipartFile file) throws IOException, OperationException;

	int uploadTerminologyXml(MultipartFile file, String version) throws DocumentAlreadyPresentException, OperationException, DataProcessingException;

	/**
	 * Aggregates and return documents by chunk
	 * @param id The snapshot instance
	 * @param index The chunk index
	 * @return The terminologies associated with the chunk
 	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no snapshot matching the given id exists
	 * @throws ChunkOutOfRangeException If no chunk matching the given index exists
	 * @throws DataIntegrityException If the database output does not match with the requested ids
	 */
	List<TerminologyDocumentDTO> getTermsByChunkIns(String id, int index) throws  DocumentNotFoundException, OperationException, ChunkOutOfRangeException, DataIntegrityException;

	/**
	 * Aggregates and return documents by chunk
	 * @param id The snapshot instance
	 * @param index The chunk index
	 * @return The terminologies associated with the chunk
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no snapshot matching the given id exists
	 * @throws ChunkOutOfRangeException If no chunk matching the given index exists
	 */
	List<ObjectId> getTermsByChunkDel(String id, int index) throws  DocumentNotFoundException, OperationException, ChunkOutOfRangeException;

	int deleteTerminologiesBySystem(String system) throws DocumentNotFoundException, OperationException, DataIntegrityException;
}
