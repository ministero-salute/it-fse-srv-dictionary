package it.finanze.sanita.fse2.ms.edssrvdictionary.repository;

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import org.bson.types.ObjectId;

public interface IChunksRepo {
    ObjectId createChunk(ChunkETY obj);
}
