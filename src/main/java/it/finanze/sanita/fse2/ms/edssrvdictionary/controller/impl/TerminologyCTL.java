/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
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
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;


/**
 * Terminologies handler
 */
@RestController
@Validated
@Slf4j
public class TerminologyCTL extends AbstractCTL implements ITerminologyCTL {

	@Autowired
	private ITerminologySRV service;

	@Autowired
	private ProfileUtility profileUtility;

	@Override
	public PostDocsResDTO uploadTerminology(FormatEnum format, @Valid RequestDTO creationInfo, MultipartFile file,HttpServletRequest request) throws OperationException, DocumentAlreadyPresentException,DataProcessingException, InvalidContentException, IOException {
		if(!profileUtility.isDevProfile() && !profileUtility.isTestProfile()) {
			JWTTokenDTO jwt = JWTTokenDTO.extractPayload(request , profileUtility.isDockerProfile());
			boolean present = jwt != null;
			log.info("Jwt present:" + present);
			log.info("Upload terminology oid jwt:" + jwt.getOid());
			log.info("Upload terminology version jwt:" + jwt.getVersion());
			log.info("Upload terminology version hash:" + jwt.getFile_hash());
			JWTTokenDTO.uploadTerminologyValidatePayload(jwt,creationInfo);
		}

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
		if(!profileUtility.isDevProfile() && !profileUtility.isTestProfile()) {
			JWTTokenDTO jwt = JWTTokenDTO.extractPayload(request , profileUtility.isDockerProfile());
			JWTTokenDTO.deleteTerminologyValidatePayload(jwt);
		}
		return new DelDocsResDTO(getLogTraceInfo(), service.deleteTerminologiesBySystem(oid,version));	
	}

}
