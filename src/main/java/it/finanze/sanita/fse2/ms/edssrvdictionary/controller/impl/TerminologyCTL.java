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
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.JWTTokenDTO;
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
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ProfileUtility;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility;


/**
 * Terminologies handler
 */
@RestController
@Validated
public class TerminologyCTL extends AbstractCTL implements ITerminologyCTL {

	@Autowired
	private ITerminologySRV service;

	@Autowired
	private ProfileUtility profileUtility;

	@Override
	public PostDocsResDTO uploadTerminology(FormatEnum format, @Valid RequestDTO creationInfo, MultipartFile file,HttpServletRequest request) throws OperationException, DocumentAlreadyPresentException,DataProcessingException, InvalidContentException, IOException {
//		JWTTokenDTO jwt = JWTTokenDTO.extractPayload(request , profileUtility.isDevOrDockerProfile());
//		JWTTokenDTO.uploadTerminologyValidatePayload(jwt, file);

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
	public DelDocsResDTO deleteTerminologies(String oid,String version,HttpServletRequest request)throws OperationException, DocumentNotFoundException, DataIntegrityException, DocumentAlreadyPresentException {
//		JWTTokenDTO jwt = JWTTokenDTO.extractPayload(request , profileUtility.isDevOrDockerProfile());
//		JWTTokenDTO.deleteTerminologyValidatePayload(jwt);
		return new DelDocsResDTO(getLogTraceInfo(), service.deleteTerminologiesBySystem(oid,version));	
	}

}
