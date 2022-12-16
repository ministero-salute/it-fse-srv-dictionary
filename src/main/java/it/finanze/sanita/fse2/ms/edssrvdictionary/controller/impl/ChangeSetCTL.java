/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl;

import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.IChangeSetCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ChangeSetChunkDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.data.GetTermsDelDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.data.GetTermsInsDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.data.snapshot.ChunksDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/** 
 * 
 */
@RestController
public class ChangeSetCTL extends AbstractCTL implements IChangeSetCTL{

    /**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = -805992420464600570L;

    @Autowired
    private transient ITerminologySRV terminologySRV;

    @Override
    public GetTermsInsDTO getTermsByChunkIns(String id, int idx) throws OutOfRangeException, DocumentNotFoundException, DataIntegrityException, OperationException {
        return new GetTermsInsDTO(getLogTraceInfo(), terminologySRV.getTermsByChunkIns(id, idx));
    }

    @Override
    public GetTermsDelDTO getTermsByChunkDel(String id, int idx) throws OutOfRangeException, DocumentNotFoundException, OperationException {
        return new GetTermsDelDTO(getLogTraceInfo(), terminologySRV.getTermsByChunkDel(id, idx));
    }

    /**
     * @param lastUpdate the last updated date
     * 
     * @return the changeset chunks of the current time according to the last update
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public ChangeSetChunkDTO changeSetChunks(Date lastUpdate) throws OperationException {

        ChunksDTO chunks = terminologySRV.createChunks(lastUpdate);
        long collectionSize = terminologySRV.getCollectionSize();
        ChangeSetChunkDTO response = new ChangeSetChunkDTO();
        LogTraceInfoDTO info = getLogTraceInfo();
        response.setSpanID(info.getSpanID());
        response.setTraceID(info.getTraceID());
        response.setLastUpdate(lastUpdate);
        response.setTimestamp(new Date());
        response.setChunks(chunks);
        response.setTotalNumberOfElements(
            (long) chunks.getInsertions().getChunksItems() + chunks.getDeletions().getChunksItems()
        );
        response.setCollectionSize(collectionSize);
        return response;
    }

}
