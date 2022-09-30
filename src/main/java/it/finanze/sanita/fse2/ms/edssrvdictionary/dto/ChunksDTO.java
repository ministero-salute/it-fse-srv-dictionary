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
    Payload insertions;
    Payload deletions;

    public static ChunksDTO empty() {
        return new ChunksDTO(EMPTY_ID, Payload.empty(), Payload.empty());
    }

    @Value
    public static class Payload {
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

        public Payload(ChunksETY chunk) {
            this.chunksCount = chunk.getCount();
            this.chunksAvgSize = chunk.getAvgSize();
            this.chunksItems = chunk.getItems();
        }

        public Payload(int count, int size, int items) {
            this.chunksCount = count;
            this.chunksAvgSize = size;
            this.chunksItems = items;
        }

        /**
         * An empty instance
         */
        public static Payload empty() {
            return new Payload(0,0,0);
        }
    }
}
