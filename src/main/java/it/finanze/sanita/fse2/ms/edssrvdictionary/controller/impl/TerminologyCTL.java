/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl;

import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.ITerminologyCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;


/**
 * Terminologies handler
 * @author Riccardo Bonesi
 */
@RestController
@Slf4j
public class TerminologyCTL extends AbstractCTL implements ITerminologyCTL {

    /**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = -805992440464600570L; 

    @Autowired
    private ITerminologySRV service;

	/**
	 * Returns terminologies matching system with pagination
	 * @param system System identifier
	 * @param page Page index
	 * @param limit Page max available items
	 * @return The requested page
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no document matching system is found
	 * @throws OutOfRangeException If the provided page index is not valid
	 */
	@Override
	public GetTermsPageResDTO getTerminologies(String system, int page, int limit) throws OperationException, DocumentNotFoundException, OutOfRangeException {
		// Retrieve Pair<Page, Entities>
		SimpleImmutableEntry<Page<TerminologyETY>, List<TerminologyDocumentDTO>> slice = service.getTerminologies(page, limit, system);
		// When returning, it builds the URL according to provided values
		return new GetTermsPageResDTO(getLogTraceInfo(), slice.getValue(), system, slice.getKey());
	}

	/**
	 * Retrieves the document by identifier
	 * @param id The document id
	 * @return The document matching the identifier
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no document matching the id is found
	 */
	@Override
	public GetTermsResDTO getTerminologyById(String id) throws OperationException, DocumentNotFoundException {
		return new GetTermsResDTO(getLogTraceInfo(), service.getTerminologyById(id));
	}

	/**
	 * Insert terminologies inside the database using an .xml file
	 * @param file An .xml file representing terminologies
	 * @param version Version identifier
	 * @return The number of terminologies inserted
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentAlreadyPresentException If the given system is already inserted
	 * @throws DataProcessingException If an error occurs while converting raw data to entity type
	 * @throws InvalidContentException  If the file is empty or null
	 */
	@Override
	public PostTermsResDTO uploadTerminologies(MultipartFile file, String version) throws OperationException, DocumentAlreadyPresentException, DataProcessingException, InvalidContentException {
		return new PostTermsResDTO(getLogTraceInfo(), service.uploadTerminologyXml(file, version));
	}

	@Override
	public ResponseEntity<PostTermsResDTO> uploadTerminologyFile(HttpServletRequest request, MultipartFile file) throws IOException, OperationException {
		Integer uploadItems = service.uploadTerminologyFile(file);
		if(uploadItems!=0) {
			return new ResponseEntity<>(new PostTermsResDTO(getLogTraceInfo(),uploadItems), null, HttpStatus.SC_CREATED);
		} else {
			return new ResponseEntity<>(new PostTermsResDTO(getLogTraceInfo(),uploadItems), null, HttpStatus.SC_OK);
		}
	}

	/**
	 * Update given terminologies using the new version and the same system
	 * @param file An .xml file representing terminologies
	 * @param version Version identifier
	 * @return The number of terminologies updated
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no document matching system is found
	 * @throws DataProcessingException If an error occurs while converting raw data to entity type
	 * @throws DataIntegrityException If database output is not the expected one
	 * @throws DocumentAlreadyPresentException If the given version already exists
	 * @throws InvalidContentException If the file is empty or null
	 */
	@Override
	public PutTermsResDTO updateTerminologies(MultipartFile file, String version) throws OperationException, DocumentNotFoundException, DataProcessingException, DataIntegrityException, DocumentAlreadyPresentException, InvalidContentException {
		return new PutTermsResDTO(getLogTraceInfo(), service.updateTerminologyXml(file, version));
	}

	/**
	 * Delete terminologies matching system
	 * @param system System identifier
	 * @return The number of terminologies deleted
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no document matching system is found
	 * @throws DataIntegrityException If database output is not the expected one
	 */
	@Override
	public DelTermsResDTO deleteTerminologies(String system) throws OperationException, DocumentNotFoundException, DataIntegrityException {
		return new DelTermsResDTO(getLogTraceInfo(), service.deleteTerminologiesBySystem(system));
	}

	/**
	 * Delete the document by identifier
	 * @param id The document it
	 * @return The number of terminologies deleted
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no document matching id is found
	 */
	@Override
	public DelTermsResDTO deleteTerminologyById(String id) throws DocumentNotFoundException, OperationException {
		return new DelTermsResDTO(getLogTraceInfo(), service.deleteTerminologyById(id));
	}

}
