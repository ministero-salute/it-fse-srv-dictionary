package it.finanze.sanita.fse2.ms.edssrvdictionary.dto;

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.ChunksETY;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

/**
 * The changeset chunk DTO
 */
@Data
@AllArgsConstructor
public class ChunksDTO {

    public static final String EMPTY_ID = null;

    String snapshotID;
    Chunk insertions;
    Chunk deletions;

    public static ChunksDTO empty() {
        return new ChunksDTO(EMPTY_ID, Chunk.empty(), Chunk.empty());
    }

    @Value
    public static class Chunk {
        /**
         * The chunks available from the snapshot
         */
        int chunksCount;
        /**
         * The number of entries each chunk is made of
         */
        int chunksAvgSize;
        /**
         * The amount of items to elaborate
         */
        int chunksItems;

        public Chunk(ChunksETY chunk) {
            this.chunksCount = chunk.getCount();
            this.chunksAvgSize = chunk.getAvgSize();
            this.chunksItems = chunk.getItems();
        }

        public Chunk(int count, int size, int items) {
            this.chunksCount = count;
            this.chunksAvgSize = size;
            this.chunksItems = items;
        }

        /**
         * An empty instance
         */
        public static Chunk empty() {
            return new Chunk(0,0,0);
        }
    }
}
