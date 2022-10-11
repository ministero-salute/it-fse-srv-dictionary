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

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.ChunksETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBeanBuilder;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.base.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.ITerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ChangeSetUtility;
import lombok.extern.slf4j.Slf4j;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.ChunksDTO.*;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ChangeSetUtility.*;
import static java.lang.String.*;

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
			snapshot = terminologyRepo.insertSnapshot(snapshot);
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
		SnapshotETY doc = terminologyRepo.getSnapshot(id);
		// Verify existence
		if(doc == null) {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
		}
		return doc;
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
	public Integer uploadTerminologyFile(MultipartFile file) throws IOException {
		Integer output = 0;
		try {
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
			output = listToSave.size();
			log.info("Successfully inserted " + listToSave.size() + " Termonologies");
		} catch(Exception ex) {
			log.error("Error while insert csv items :" , ex);
			throw new BusinessException("Error while insert csv items :" , ex);
		}
		return output;
	}

	@Override
	public void deleteTerminologyById(String id) throws DocumentNotFoundException, OperationException {
		TerminologyETY out = terminologyRepo.deleteById(id);
		if (out == null) {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
		}
	}

	@Override
	public List<TerminologyDocumentDTO> getTermsByChunkIns(String id, int index) throws DocumentNotFoundException, OperationException, ChunkOutOfRangeException, DataIntegrityException {
		// Retrieve document chunks
		SnapshotETY chunks = getChunks(id);
		// Get chunk according to type
		List<List<ObjectId>> ids = chunks.getInsertions().getIds();
		List<ObjectId> chunk;
		// Verify index
		try {
			chunk = ids.get(index);
		}catch (IndexOutOfBoundsException e) {
			throw new ChunkOutOfRangeException("The chunk index is out of range: " + index);
		}
		// Retrieve documents
		List<TerminologyETY> docs = terminologyRepo.findByIds(chunk);
		// Verify it matches the expected size
		if(chunk.size() != docs.size()) {
			throw new DataIntegrityException(
				format("The expected document size <%s> does not match the returned one <%s>", chunk.size(), docs.size())
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
	 * @throws ChunkOutOfRangeException  If no chunk matching the given index exists
	 * @throws DataIntegrityException    If the database output does not match with the requested ids
	 */
	@Override
	public List<ObjectId> getTermsByChunkDel(String id, int index) throws DocumentNotFoundException, OperationException, ChunkOutOfRangeException, DataIntegrityException {
		// Retrieve document chunks
		SnapshotETY chunks = getChunks(id);
		// Get chunk according to type
		List<List<ObjectId>> ids = chunks.getDeletions().getIds();
		List<ObjectId> chunk;
		// Verify index
		try {
			chunk = ids.get(index);
		}catch (IndexOutOfBoundsException e) {
			throw new ChunkOutOfRangeException("The chunk index is out of range: " + index);
		}
		return chunk;
	}

	private List<TerminologyBuilderDTO> buildDTOFromCsv(Reader reader){
		List<TerminologyBuilderDTO> output = null; 
		output = new CsvToBeanBuilder(reader).withType(TerminologyBuilderDTO.class).withSeparator(',').build().parse();
		return output;
	}
	
	
}
