/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.service.impl;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_UNABLE_RETRIVE_FILENAME;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_SRV_CHUNK_MISMATCH;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_SRV_DOCUMENT_NOT_EXIST;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_SRV_FILE_NOT_VALID;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_SRV_PAGE_IDX_LESS_ZERO;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_SRV_PAGE_LIMIT_LESS_ZERO;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_SRV_PAGE_NOT_EXISTS;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_SRV_SYSTEM_ALREADY_EXISTS;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_SRV_SYSTEM_NOT_EXISTS;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_VAL_IDX_CHUNK_NOT_VALID;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorInstance.Fields.FILE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY.FILE_CSV_EXT_DOTTED;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ChangeSetUtility.CHUNKS_SIZE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ChangeSetUtility.chunks;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_PATH_IDX_VAR;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_QP_LIMIT;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.API_QP_PAGE;
import static java.lang.String.format;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;

import it.finanze.sanita.fse2.ms.edssrvdictionary.client.IQueryClient;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.GetResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.MetadataResourceResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.RequestDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.SystemUrlDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.data.snapshot.ChunksDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.data.snapshot.ChunksDTO.Chunk;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.FormatEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.ITerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.ChunksETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ChangeSetUtility;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.FileUtility;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.StringUtility;

/**

 *	Terminology service.
 */
@Service
public class TerminologySRV implements ITerminologySRV {

	@Autowired
	private ITerminologyRepo repository;

	@Autowired
	private IQueryClient queryClient;

	@Override
	public List<ChangeSetDTO> getInsertions(Date lastUpdate) throws OperationException {
		// Retrieve insertions
		List<TerminologyETY> insertions;
		// Verify no null value has been provided
		if (lastUpdate != null) {
			insertions = repository.getInsertions(lastUpdate);
		} else {
			insertions = repository.getEveryActiveTerminology();
		}
		// Iterate and populate
		return insertions.stream().map(ChangeSetUtility::toChangeset).collect(Collectors.toList());
	}
	

	@Override
	public List<ChangeSetDTO> getDeletions(Date lastUpdate) throws OperationException {
		// Create empty container
		List<ChangeSetDTO> changes = new ArrayList<>();
		// Verify no null value has been provided
		if(lastUpdate != null) {
			// Retrieve deletions
			List<TerminologyETY> deletions = repository.getDeletions(lastUpdate);
			// Iterate and populate
			changes = deletions.stream().map(ChangeSetUtility::toChangeset).collect(Collectors.toList());
		}
		return changes;
	}

	/**
	 * Creates an insertion/deletion snapshot according to the given timeframe
	 *
	 * @param lastUpdate The timeframe to consider while calculating
	 * @return The chunks instance
	 * @throws OperationException If a data-layer error occurs
	 */
	@Override
	public ChunksDTO createChunks(Date lastUpdate) throws OperationException {
		// Working var
		ChunksDTO chunks = ChunksDTO.empty();
		// Retrieve data
		List<ChangeSetDTO> insertions = getInsertions(lastUpdate);
		List<ChangeSetDTO> deletions = getDeletions(lastUpdate);
		// Checks insertions and deletions are not-empty
		if(!insertions.isEmpty() || !deletions.isEmpty()) {
			// Create snapshot instance
			SnapshotETY snapshot = SnapshotETY.empty();
			// Check emptiness on payloads
			if(!insertions.isEmpty()) {
				// Create entity
				ChunksETY in = chunks(insertions, CHUNKS_SIZE);
				// Set into snapshot instance
				snapshot.setInsertions(in);
				// Convert to DTO
				chunks.setInsertions(new Chunk(in));
			}
			if(!deletions.isEmpty()) {
				// Create entity
				ChunksETY out = chunks(deletions, CHUNKS_SIZE);
				// Set into snapshot instance
				snapshot.setDeletions(out);
				// Convert to DTO
				chunks.setDeletions(new Chunk(out));
			}
			// Insert into database
			snapshot = repository.insertSnapshot(snapshot);
			// Aggregate
			chunks = new ChunksDTO(snapshot.getId(), chunks.getInsertions(), chunks.getDeletions());
		}

		return chunks;
	}

	/**
	 * Retrieves the snapshot document according to the given id
	 *
	 * @param id The document identifier
	 * @return The chunks instance
	 * @throws OperationException        If a data-layer error occurs
	 * @throws DocumentNotFoundException If no snapshot exists matching the identifier
	 */
	@Override
	public SnapshotETY getChunks(String id) throws OperationException, DocumentNotFoundException {
		// Retrieve document
		SnapshotETY doc = repository.getSnapshot(id);
		// Verify existence
		if(doc == null) {
			throw new DocumentNotFoundException(ERR_SRV_DOCUMENT_NOT_EXIST);
		}
		return doc;
	}

	/**
	 * Retrieves the expected collection size after the alignment
	 *
	 * @return The collection size
	 * @throws OperationException If a data-layer error occurs
	 */
	@Override
	public long getCollectionSize() throws OperationException {
		return repository.getActiveDocumentCount();
	}


	@Override
	public TerminologyDocumentDTO getTerminologyById(String id) throws OperationException, DocumentNotFoundException {

		TerminologyETY output = repository.findById(id);

		if (output == null) {
			throw new DocumentNotFoundException(ERR_SRV_DOCUMENT_NOT_EXIST);
		}

		return TerminologyDocumentDTO.fromEntity(output);
	}

	@Override
	public List<TerminologyDocumentDTO> getTermsByChunkIns(String id, int index) throws DocumentNotFoundException, OperationException, OutOfRangeException, DataIntegrityException {
		// Retrieve document chunks
		SnapshotETY chunks = getChunks(id);
		// Get chunk according to type
		List<List<ObjectId>> ids = chunks.getInsertions().getIds();
		List<ObjectId> chunk;
		// Verify index
		try {
			chunk = ids.get(index);
		}catch (IndexOutOfBoundsException e) {
			throw new OutOfRangeException(ERR_VAL_IDX_CHUNK_NOT_VALID, API_PATH_IDX_VAR);
		}
		// Retrieve documents
		List<TerminologyETY> docs = repository.findByIds(chunk);
		// Verify it matches the expected size
		if(chunk.size() != docs.size()) {
			throw new DataIntegrityException(
					format(ERR_SRV_CHUNK_MISMATCH, chunk.size(), docs.size())
					);
		}
		// Return mapping back to DTO type
		return docs.stream().map(TerminologyDocumentDTO::fromEntity).collect(Collectors.toList());
	}

	/**
	 * Aggregates and return documents by chunk
	 *
	 * @param id    The snapshot instance
	 * @param index The chunk index
	 * @return The terminologies associated with the chunk
	 * @throws OperationException        If a data-layer error occurs
	 * @throws DocumentNotFoundException If no snapshot matching the given id exists
	 * @throws OutOfRangeException  If no chunk matching the given index exists
	 */
	@Override
	public List<ObjectId> getTermsByChunkDel(String id, int index) throws DocumentNotFoundException, OperationException, OutOfRangeException {
		// Retrieve document chunks
		SnapshotETY chunks = getChunks(id);
		// Get chunk according to type
		List<List<ObjectId>> ids = chunks.getDeletions().getIds();
		List<ObjectId> chunk;
		// Verify index
		try {
			chunk = ids.get(index);
		}catch (IndexOutOfBoundsException e) {
			throw new OutOfRangeException(ERR_VAL_IDX_CHUNK_NOT_VALID, API_PATH_IDX_VAR);
		}
		return chunk;
	}


	@Override
	public SimpleImmutableEntry<Page<TerminologyETY>, List<TerminologyDocumentDTO>> getTerminologies(int page, int limit, String system) throws OperationException, DocumentNotFoundException, OutOfRangeException {
		// Check system exists
		if(!repository.existsBySystem(system)) {
			// Let the caller know about it
			throw new DocumentNotFoundException(String.format(ERR_SRV_SYSTEM_NOT_EXISTS, system));
		}
		// Check valid limit was provided
		if(limit <= 0) {
			// Let the caller know about it
			throw new OutOfRangeException(ERR_SRV_PAGE_LIMIT_LESS_ZERO, API_QP_LIMIT);
		}
		// Check valid index was provided
		if(page < 0) {
			// Let the caller know about it
			throw new OutOfRangeException(ERR_SRV_PAGE_IDX_LESS_ZERO, API_QP_PAGE);
		}
		// Retrieve page
		Page<TerminologyETY> current = repository.getBySystem(system, PageRequest.of(page, limit));
		// Check valid index was provided
		if(page >= current.getTotalPages()) {
			// Let the caller know about it
			throw new OutOfRangeException(String.format(ERR_SRV_PAGE_NOT_EXISTS, 0, current.getTotalPages() - 1), API_QP_PAGE);
		}
		// Convert entities to dto
		List<TerminologyDocumentDTO> entities = current.stream().map(TerminologyDocumentDTO::fromEntity).collect(Collectors.toList());
		// Return Pair<Page,Entities> object
		return new SimpleImmutableEntry<>(current, entities);
	}

	@Override
	public PostDocsResDTO uploadTerminologyCsv(FormatEnum formatEnum, MultipartFile file, RequestDTO requestDTO) throws DocumentAlreadyPresentException, OperationException, InvalidContentException, IOException {
		// Check file integrity
		if(file == null || file.isEmpty()) throw new InvalidContentException(ERR_SRV_FILE_NOT_VALID, FILE);
		 
		// Verify this system does not exist
		if(queryClient.getTerminology(requestDTO.getOid(), requestDTO.getVersion()).isPresent()) {
			throw new DocumentAlreadyPresentException(String.format(ERR_SRV_SYSTEM_ALREADY_EXISTS, requestDTO.getOid()));
		}

		return queryClient.callUploadTerminology(formatEnum, requestDTO, file);
	}
	

	@Override
	public int deleteTerminologiesBySystem(String oid, String version) throws DocumentNotFoundException,
			OperationException, DataIntegrityException, DocumentAlreadyPresentException {
		// Check oid exists
		
		GetResponseDTO res = queryClient.getTerminology(oid, version);
		if(!res.isPresent()) {
			throw new DocumentNotFoundException(String.format(ERR_SRV_DOCUMENT_NOT_EXIST, oid));
		}
		// Delete any matching document system (then return size)
		queryClient.deleteTerminology(res.getId());
		return res.getCounter();
	}

	@Override
	public int updateTerminologyCsv(MultipartFile file, String version, Date releaseDate) throws DocumentNotFoundException, OperationException, DataProcessingException, InvalidContentException {
		// Check file integrity
		if(file == null || file.isEmpty()) throw new InvalidContentException(ERR_SRV_FILE_NOT_VALID, FILE);
		// Extract system from filename
		String system = file.getOriginalFilename();
		// Check we got the original filename
		if (system == null || system.isEmpty()) {
			throw new DataProcessingException(ERR_REP_UNABLE_RETRIVE_FILENAME);
		}
		// Remove extension
		system = system.replace(FILE_CSV_EXT_DOTTED, "");
		// Check system exists
		if(!repository.existsBySystem(system)) {
			// Let the caller know about it
			throw new DocumentNotFoundException(String.format(ERR_SRV_SYSTEM_NOT_EXISTS, system));
		}

		// Extract binary content
		byte[] raw = FileUtility.throwIfEmpty(file);
		// Parse entities
		List<TerminologyETY> entities = TerminologyETY.fromCSV(raw, system, version, releaseDate);
		// Execute and return size
		return repository.updateBySystem(system,version,releaseDate, entities).size();
	}

	@Override
	public MetadataResourceResponseDTO  callQueryToManageMetadataResource() {
		String jsonFile = new String(FileUtility.getFileFromInternalResources("dictionary.json"), StandardCharsets.UTF_8);
		List<SystemUrlDTO> listCodeSystemUrls = StringUtility.fromJsonForList(jsonFile, new TypeReference<List<SystemUrlDTO>>() {});
		return queryClient.callMetadataResourceEp(listCodeSystemUrls);
	}


}
