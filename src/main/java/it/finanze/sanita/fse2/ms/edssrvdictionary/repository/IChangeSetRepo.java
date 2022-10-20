/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.repository;


import java.util.Date;
import java.util.List;

import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;

public interface IChangeSetRepo<T> {

    String FIELD_ID = "_id";
    String FIELD_INSERTION_DATE = "insertion_date";
    String FIELD_LAST_UPDATE = "last_update_date";
    String FIELD_DELETED = "deleted";

    /**
     * Retrieves the latest insertions according to the given timeframe
     *
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing insertions
     * @throws OperationException If a data-layer error occurs
     */
    List<T> getInsertions(Date lastUpdate) throws OperationException;

    /**
     * Retrieves the latest deletions according to the given timeframe
     *
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing deletions
     * @throws OperationException If a data-layer error occurs
     */
    List<T> getDeletions(Date lastUpdate) throws OperationException;

    /**
     * Retrieves a snapshot instance according to the given id
     *
     * @param id The snapshot id
     * @return The snapshot instance
     * @throws OperationException If a data-layer error occurs
     */
    SnapshotETY getSnapshot(String id) throws OperationException;

    /**
     * Insert a given snapshot inside the database
     *
     * @param entity The snapshot instance
     * @return The inserted snapshot instance
     * @throws OperationException If a data-layer error occurs
     */
    SnapshotETY insertSnapshot(SnapshotETY entity) throws OperationException;
}
