package it.finanze.sanita.fse2.ms.edssrvdictionary.repository;

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;

import javax.annotation.Nullable;

public interface IChunksRepo {
    void createChunk(ChunkETY obj);
    void createChunkIndex(ChunksIndexETY obj);
    long getActiveItems();
    boolean exists(String id, String version);
    void markToRemove(String id, @Nullable String omit);

}
