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

import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.ITerminologyCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.DelDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.GetDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PutDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.pagination.GetDocsPageResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Date;
import java.util.List;


/**
 * Terminologies handler
 */
@RestController
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
	public GetDocsPageResDTO getTerminologies(String system, int page, int limit) throws OperationException, DocumentNotFoundException, OutOfRangeException {
		// Retrieve Pair<Page, Entities>
		SimpleImmutableEntry<Page<TerminologyETY>, List<TerminologyDocumentDTO>> slice = service.getTerminologies(page, limit, system);
		// When returning, it builds the URL according to provided values
		return new GetDocsPageResDTO(getLogTraceInfo(), slice.getValue(), system, slice.getKey());
	}

	/**
	 * Retrieves the document by identifier
	 * @param id The document id
	 * @return The document matching the identifier
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no document matching the id is found
	 */
	@Override
	public GetDocsResDTO getTerminologyById(String id) throws OperationException, DocumentNotFoundException {
		return new GetDocsResDTO(getLogTraceInfo(), service.getTerminologyById(id));
	}

	/**
	 * Insert terminologies inside the database using a .csv file
	 * @param file A .csv file representing terminologies
	 * @param version Version identifier
	 * @return The number of terminologies inserted
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentAlreadyPresentException If the given system is already inserted
	 * @throws DataProcessingException If an error occurs while converting raw data to entity type
	 * @throws InvalidContentException  If the file is empty or null
	 */	
	@Override
	public PostDocsResDTO uploadTerminologies(MultipartFile file, String version, Date releaseDate) throws OperationException, DocumentAlreadyPresentException, DataProcessingException, InvalidContentException {
		return new PostDocsResDTO(getLogTraceInfo(), service.uploadTerminologyCsv(file, version, releaseDate));
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
	 * @throws InvalidContentException If the file is empty or null
	 */
	@Override
	public PutDocsResDTO updateTerminologies(MultipartFile file, String version, Date releaseDate) throws OperationException, DocumentNotFoundException, DataProcessingException, DataIntegrityException, InvalidContentException {
		return new PutDocsResDTO(getLogTraceInfo(), service.updateTerminologyCsv(file, version, releaseDate));
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
	public DelDocsResDTO deleteTerminologies(String system) throws OperationException, DocumentNotFoundException, DataIntegrityException {
		return new DelDocsResDTO(getLogTraceInfo(), service.deleteTerminologiesBySystem(system));
	}

}
