/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.repository;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;

/**
 *
 *	Vocabulary interface repository.
 */
public interface ITerminologyRepo extends IChangeSetRepo<TerminologyETY> {

 
	/**
	 * Returns terminology matching the identifier
	 * @param pk The entity identifier
	 * @return The entity matching the identifier or {@code null}
	 * @throws OperationException If a data-layer error occurs
	 */
	TerminologyETY findById(String pk) throws OperationException;

	/**
	 * Inserts all terminologies into the database.
	 *
	 * @param etys List of entities to insert
	 * @return The entities inserted
	 * @throws OperationException If a data-layer error occurs
	 */
	List<TerminologyETY> insertAll(List<TerminologyETY> etys) throws OperationException;

	/**
	 * Check if a given system is already present
	 * @param system The system parameter
	 * @return True if exists at least one term with the given system, otherwise false
	 * @throws OperationException If a data-layer error occurs
	 */
	boolean existsBySystem(String system) throws OperationException;
	
	/**
     * Retrieves all the not-deleted terminologies
     *
     * @return Any available terminology
     * @throws OperationException If a data-layer error occurs
     */
    List<TerminologyETY> getEveryActiveTerminology() throws OperationException;

	/**
	 * Returns entities matching the identifier list
	 * @param ids List containing identifiers
	 * @return The entities matching the identifiers
	 * @throws OperationException If a data-layer error occurs
	 */
	List<TerminologyETY> findByIds(List<ObjectId> ids) throws OperationException;

	/**
	 * Return entities matching the system
	 * @param system System identifier
	 * @return The entities matching the system
	 * @throws OperationException If a data-layer error occurs
	 */
	List<TerminologyETY> findBySystem(String system) throws OperationException;

	/**
	 * Delete entities matching system and returns them
	 * @param system System identifier
	 * @return The entities matching the system
	 * @throws OperationException If a data-layer error occurs
	 * @throws DataIntegrityException If database output is not the expected one
	 */
	List<TerminologyETY> deleteBySystem(String system) throws OperationException, DataIntegrityException;

	/**
	 * Update entities by system, deleting the oldest and inserting the new one
	 * @param system System identifier
	 * @param entities Entities to insert
	 * @return The inserted entities
	 * @throws OperationException If a data-layer error occurs
	 */
	List<TerminologyETY> updateBySystem(String system,String version,Date releaseDate, List<TerminologyETY> entities) throws OperationException, DocumentNotFoundException;
	
	/**
	 * Returns terminologies matching system with pagination
	 * @param system System identifier
	 * @param page Page index
	 * @return The page data
	 * @throws OperationException If a data-layer error occurs
	 */
    Page<TerminologyETY> getBySystem(String system, Pageable page) throws OperationException;

	/**
	 * Check if a given system is already present
	 * @param system The system parameter
	 * @return True if exists at least one term with the given system, otherwise false
	 * @throws OperationException If a data-layer error occurs
	 */
	boolean existsBySystemVersionAndRelease(String system, String version , Date releaseDate) throws OperationException;
	
}
