/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.ITerminologyCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.RequestDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.DelDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.FormatEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility;


/**
 * Terminologies handler
 */
@RestController
@Validated
public class TerminologyCTL extends AbstractCTL implements ITerminologyCTL {

	
    @Autowired
    private ITerminologySRV service;

    @Override
	public PostDocsResDTO uploadTerminology(FormatEnum format, @Valid RequestDTO creationInfo, MultipartFile file,HttpServletRequest request) throws OperationException, DocumentAlreadyPresentException,DataProcessingException, InvalidContentException, IOException {
		LogTraceInfoDTO traceInfoDTO = getLogTraceInfo();
		ValidationUtility.validateFileExtension(format, file);
		ValidationUtility.validRequiredReqBody(format, creationInfo);
		PostDocsResDTO out = service.uploadTerminologyCsv(format,file, creationInfo);
		out.setSpanID(traceInfoDTO.getSpanID());
		out.setTraceID(traceInfoDTO.getTraceID());
		return out;
	}
	
	/**
	 * Delete terminologies matching system
	 * @param system System identifier
	 * @return The number of terminologies deleted
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no document matching system is found
	 * @throws DataIntegrityException If database output is not the expected one
	 * @throws DocumentAlreadyPresentException 
	 */
    @Override
	public DelDocsResDTO deleteTerminologies(String oid,String version)throws OperationException, DocumentNotFoundException, DataIntegrityException, DocumentAlreadyPresentException {
		return new DelDocsResDTO(getLogTraceInfo(), service.deleteTerminologiesBySystem(oid,version));	
	}
	
//	/**
//	 * Returns terminologies matching system with pagination
//	 * @param system System identifier
//	 * @param page Page index
//	 * @param limit Page max available items
//	 * @return The requested page
//	 * @throws OperationException If a data-layer error occurs
//	 * @throws DocumentNotFoundException If no document matching system is found
//	 * @throws OutOfRangeException If the provided page index is not valid
//	 */
//	@Override
//	public GetDocsPageResDTO getTerminologies(String system, int page, int limit) throws OperationException, DocumentNotFoundException, OutOfRangeException {
//		// Retrieve Pair<Page, Entities>
//		SimpleImmutableEntry<Page<TerminologyETY>, List<TerminologyDocumentDTO>> slice = service.getTerminologies(page, limit, system);
//		// When returning, it builds the URL according to provided values
//		return new GetDocsPageResDTO(getLogTraceInfo(), slice.getValue(), system, slice.getKey());
//	}
//
//	/**
//	 * Retrieves the document by identifier
//	 * @param id The document id
//	 * @return The document matching the identifier
//	 * @throws OperationException If a data-layer error occurs
//	 * @throws DocumentNotFoundException If no document matching the id is found
//	 */
//	@Override
//	public GetDocsResDTO getTerminologyById(String id) throws OperationException, DocumentNotFoundException {
//		return new GetDocsResDTO(getLogTraceInfo(), service.getTerminologyById(id));
//	}
// 
 

}
