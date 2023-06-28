package it.finanze.sanita.fse2.ms.edssrvdictionary.repository;

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface IChunksRepo {
    void createChunk(ChunkETY obj);
    void createChunkIndex(ChunksIndexETY obj);
    Optional<ChunksIndexETY> getChunkIndex(String id);
    Optional<ChunkETY> getChunk(String id);
    long getActiveItems();
    boolean exists(String id, String version);
    void markIndexAsRemovable(String id, @Nullable String omit);
    List<ChunksIndexETY> removeIndexes();
    List<ObjectId> removeOrphanChunks();
    Optional<ChunksIndexETY> findByResourceVersion(String resource, String version);
}
