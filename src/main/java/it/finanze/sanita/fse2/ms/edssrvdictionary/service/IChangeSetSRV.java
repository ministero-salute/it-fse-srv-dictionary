/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.service;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.chunks.snapshot.ChunksDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import org.springframework.lang.Nullable;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.base.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;

import java.util.Date;
import java.util.List;

public interface IChangeSetSRV {
    /**
     * Retrieves the latest insertions according to the given timeframe
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing insertions
     * @throws OperationException If a data-layer error occurs
     */
    List<ChangeSetDTO> getInsertions(@Nullable Date lastUpdate) throws OperationException;

    /**
     * Retrieves the latest deletions according to the given timeframe
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing deletions
     * @throws OperationException If a data-layer error occurs
     */
    List<ChangeSetDTO> getDeletions(@Nullable Date lastUpdate) throws OperationException;

    /**
     * Creates an insertion/deletion snapshot according to the given timeframe
     * @param lastUpdate The timeframe to consider while calculating
     * @return The chunks instance
     * @throws OperationException If a data-layer error occurs
     */
    ChunksDTO createChunks(@Nullable Date lastUpdate) throws OperationException;

    /**
     * Retrieves the snapshot document according to the given id
     * @param id The document identifier
     * @return The chunks instance
     * @throws OperationException If a data-layer error occurs
     * @throws DocumentNotFoundException If no snapshot exists matching the identifier
     */
    SnapshotETY getChunks(String id) throws OperationException, DocumentNotFoundException;
}
