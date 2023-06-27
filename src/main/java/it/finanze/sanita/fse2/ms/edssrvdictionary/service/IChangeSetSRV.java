package it.finanze.sanita.fse2.ms.edssrvdictionary.service;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public interface IChangeSetSRV {

    HistoryDTO history(Date lastUpdate);

    void initHistoryStorage();

    long size();

    List<ChunksIndexETY> clearIndexes();
    List<ObjectId> clearOrphanChunks();

}
