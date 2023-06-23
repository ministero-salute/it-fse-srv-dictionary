/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.service.impl;

import it.finanze.sanita.fse2.ms.edssrvdictionary.client.IQueryClient;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.GetResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.RequestDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.FormatEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_SRV_DOCUMENT_NOT_EXIST;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_SRV_FILE_NOT_VALID;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorInstance.Fields.FILE;

/**

 *	Terminology service.
 */
@Service
public class TerminologySRV implements ITerminologySRV {

	@Autowired
	private IQueryClient client;

	@Override
	public PostDocsResDTO uploadTerminologyCsv(FormatEnum formatEnum, MultipartFile file, RequestDTO requestDTO) throws InvalidContentException, IOException {
		// Check file integrity
		if(file == null || file.isEmpty()) throw new InvalidContentException(ERR_SRV_FILE_NOT_VALID, FILE);
		return client.callUploadTerminology(formatEnum, requestDTO, file);
	}
	

	@Override
	public int deleteTerminologiesBySystem(String oid, String version) throws DocumentNotFoundException {
		GetResponseDTO res = client.getTerminology(oid, version);
		if(!res.isPresent()) {
			throw new DocumentNotFoundException(ERR_SRV_DOCUMENT_NOT_EXIST);
		}
		// Delete any matching document system (then return size)
		client.deleteTerminology(res.getId());
		return res.getCounter();
	}


}
