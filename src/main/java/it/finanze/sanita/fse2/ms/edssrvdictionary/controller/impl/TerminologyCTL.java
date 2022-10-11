package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.chunks.GetTermsDelDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.chunks.GetTermsInsDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.ITerminologyCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.GetTerminologyResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.TerminologyResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.OperationLogEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.ResultLogEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.logging.ElasticLoggerHelper;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import lombok.extern.slf4j.Slf4j;



/** 
 * 
 * @author Riccardo Bonesi
 */
@RestController
@Slf4j
public class TerminologyCTL extends AbstractCTL implements ITerminologyCTL{

    /**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = -805992440464600570L; 

    @Autowired
    private ITerminologySRV terminologySRV;


	/**
	 * Get last inserted elements by chunks
	 * @id identifier of the target snapshot
	 * @idx int identifying a chunk
	 * @return a DTo containing the last updated elements
	 * @throws ChunkOutOfRangeException if idx represent a non-existing chunk
	 * @throws DataIntegrityException if the request violates Integrity Constraint
	 * @throws DocumentNotFoundException 
	 * @throws OperationException If a data-layer error occurs
	 */
	@Override
	public ResponseEntity<GetTerminologyResDTO> getTermsByChunkIns(String id, int idx) throws ChunkOutOfRangeException, DocumentNotFoundException, DataIntegrityException, OperationException {
		return new GetTermsInsDTO(getLogTraceInfo(), terminologySRV.getTermsByChunkIns(id, idx));
	}

	
	/**
	 * Get ids of deleted items of a chunk
	 * @id : identifier of the target snapshot
	 * @idx : int identifying a chunk
	 * @throws ChunkOutOfRangeException if idx represent a non-existing chunk
	 * @throws DataIntegrityException if the request violates Integrity Constraint
	 * @throws DocumentNotFoundException 
	 * @throws OperationException
	 */
	@Override
	public GetTermsDelDTO getTermsByChunkDel(String id, int idx) throws ChunkOutOfRangeException, DocumentNotFoundException, DataIntegrityException, OperationException {
		return new GetTermsDelDTO(getLogTraceInfo(), terminologySRV.getTermsByChunkDel(id, idx));
	}

	@Override
	public GetTerminologyResDTO getTerminologyById(HttpServletRequest request,  String id) throws OperationException, DocumentNotFoundException {
		log.info(Constants.Logs.CALLED_GET_TERMINOLOGY_BY_ID); 
		TerminologyDocumentDTO doc = terminologySRV.findById(id); 
		return new ResponseEntity<GetTerminologyResDTO>(new GetTerminologyResDTO(getLogTraceInfo(), doc), null, HttpStatus.SC_OK); 

	}


	@Override
	public ResponseEntity<TerminologyResponseDTO> uploadTerminologyFile(HttpServletRequest request, MultipartFile file) throws IOException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException {
		Integer uploadItems = terminologySRV.uploadTerminologyFile(file);
		if(uploadItems!=0) {
			return new ResponseEntity<TerminologyResponseDTO>(new TerminologyResponseDTO(getLogTraceInfo(),uploadItems), null, HttpStatus.SC_CREATED); 
		} else {
			return new ResponseEntity<TerminologyResponseDTO>(new TerminologyResponseDTO(getLogTraceInfo(),uploadItems), null, HttpStatus.SC_OK); 
		}
	}

	@Override
	public TerminologyResponseDTO deleteTerminologyById(String id) throws DocumentNotFoundException, OperationException {
		terminologySRV.deleteTerminologyById(id);
		return new TerminologyResponseDTO(getLogTraceInfo());
	}


}
