package it.finanze.sanita.fse2.ms.edssrvdictionary.service;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ResourceDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

public interface IChangeSetSRV {

    HistoryDTO history(Date lastUpdate);
    ResourceDTO resource(String resource, String version, @Nullable String ref, int chunk) throws DocumentNotFoundException, OutOfRangeException;
    void initHistoryStorage();
    long size();
    List<ChunksIndexETY> clearIndexes();
    List<ObjectId> clearOrphanChunks();

}
