package it.finanze.sanita.fse2.ms.edssrvdictionary.service;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.ChunksDTO;
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
    ChunksDTO createSnapshot(@Nullable Date lastUpdate) throws OperationException;
}
