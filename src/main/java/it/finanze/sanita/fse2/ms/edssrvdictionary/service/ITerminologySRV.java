/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.service;


import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.RequestDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.FormatEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 *
 *	Vocabulary interface service.
 */
public interface ITerminologySRV extends IChangeSetSRV {

	HistoryDTO getHistory(Date lastUpdate);

	/**
	 * Insert terminologies inside the database using a .csv file
	 * @param file A .csv file representing terminologies
	 * @return The number of terminologies inserted
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentAlreadyPresentException If the given system is already inserted
	 * @throws DataProcessingException If an error occurs while converting raw data to entity type
	 * @throws InvalidContentException  If the file is empty or null
	 */
	PostDocsResDTO uploadTerminologyCsv(FormatEnum formatEnum, MultipartFile file, RequestDTO requestDTO) throws DocumentAlreadyPresentException, OperationException, InvalidContentException, IOException;
	
	/**
	 * Delete terminologies matching system
	 * @return The number of terminologies deleted
	 * @throws OperationException If a data-layer error occurs
	 * @throws DocumentNotFoundException If no document matching system is found
	 * @throws DataIntegrityException If database output is not the expected one
	 */
	int deleteTerminologiesBySystem(String oid, String version) throws DocumentNotFoundException, OperationException, DataIntegrityException, DocumentAlreadyPresentException;
	
}
