/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl;

import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.IChangeSetCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ResourceDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistorySnapshotDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.IChangeSetSRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/** 
 * 
 */
@RestController
public class ChangeSetCTL extends AbstractCTL implements IChangeSetCTL {

    @Autowired
    private IChangeSetSRV service;

    /**
     * @param lastUpdate the last updated date
     * 
     * @return the changeset chunks of the current time according to the last update
     */
    @Override
    public ChangeSetDTO changeSet(Date lastUpdate) {

        HistoryDTO history = service.history(lastUpdate);
        ChangeSetDTO response = new ChangeSetDTO();
        LogTraceInfoDTO info = getLogTraceInfo();
        response.setSpanID(info.getSpanID());
        response.setTraceID(info.getTraceID());
        response.setLastUpdate(lastUpdate);
        response.setTimestamp(history.getTimestamp());
        response.setInsertions(history.getInsertions());
        response.setDeletions(history.getDeletions());

        return response;
    }

    @Override
    public ResourceDTO resource(String resource, String version, String ref, int chunk) throws DocumentNotFoundException, OutOfRangeException {
        return service.resource(resource, version, ref, chunk).trackWith(getLogTraceInfo());
    }

    @Override
    public HistorySnapshotDTO snapshot() {
        return service.snapshot().trackWith(getLogTraceInfo());
    }

}
