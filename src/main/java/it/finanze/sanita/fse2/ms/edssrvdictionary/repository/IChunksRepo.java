package it.finanze.sanita.fse2.ms.edssrvdictionary.repository;

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;
import java.util.List;

public interface IChunksRepo {
    void createChunk(ChunkETY obj);
    void createChunkIndex(ChunksIndexETY obj);
    long getActiveItems();
    boolean exists(String id, String version);
    void markIndexAsRemovable(String id, @Nullable String omit);
    List<ChunksIndexETY> removeIndexes();
    List<ObjectId> removeOrphanChunks();
}
