/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ChunksETY {

    public static final String FIELD_IDS = "ids";
    public static final String FIELD_COUNT = "chunks_count";
    public static final String FIELD_SIZE = "chunks_avg_size";
    public static final String FIELD_ITEMS = "chunks_items";

    /**
     * The chunks containing the ids of each file
     */
    @Field(FIELD_IDS)
    private List<List<ObjectId>> ids;
    /**
     * The number of chunks available
     */
    @Field(FIELD_COUNT)
    private int count;
    /**
     * The amount of entries of a single chunk (average)
     */
    @Field(FIELD_SIZE)
    private int avgSize;
    /**
     * The amount of entries of all the chunks
     */
    @Field(FIELD_ITEMS)
    private int items;

    public static ChunksETY empty() {
        return new ChunksETY(new ArrayList<>(), 0 ,0 ,0);
    }
}
