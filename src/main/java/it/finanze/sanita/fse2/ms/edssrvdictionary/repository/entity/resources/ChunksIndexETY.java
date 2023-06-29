package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.IndexMetaETY;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "#{@chunksIndexBean}")
@Data
@AllArgsConstructor
public class ChunksIndexETY {

    public static final String FIELD_IDX_ID = "_id";
    public static final String FIELD_IDX_RESOURCE = "resource";
    public static final String FIELD_IDX_VERSION = "version";
    public static final String FIELD_IDX_CHUNKS = "chunks";
    public static final String FIELD_IDX_DELETED_AT = "deleted_at";
    public static final String FIELD_IDX_SIZE = "size";

    @Id
    private ObjectId id;
    @Field(FIELD_IDX_RESOURCE)
    private String resource;
    @Field(FIELD_IDX_VERSION)
    private String version;
    private IndexMetaETY meta;
    @Field(FIELD_IDX_CHUNKS)
    private List<ObjectId> chunks;
    @Field(FIELD_IDX_SIZE)
    private long size;
    /**
     * Using a date field to prevent invalidating reading
     * if after a subsequent synchronisation a resource is
     * removed but some client are still reading it.
     * We cannot use a boolean because the remove resource scheduler
     * may run at the same time a resource is being read, we
     * want to give a time-window where the resource still exists,
     * and it's still readable. If the scheduler run but the
     * deleted_at field is recent, it will be ignored.
     */
    @Field(FIELD_IDX_DELETED_AT)
    private Date deletedAt;

    public static ChunksIndexETY from(HistoryResourceDTO res, ObjectId root, List<ChunkETY> chunks) {
        return new ChunksIndexETY(
            root,
            res.getResourceId(),
            res.getVersionId(),
            IndexMetaETY.from(res.getMeta()),
            asChunksId(chunks),
            asChunksSize(chunks),
            null
        );
    }

    private static long asChunksSize(List<ChunkETY> chunks) {
        return chunks.stream().map(c -> c.getChunk().getSize()).reduce(Long::sum).orElse(0L);
    }

    private static List<ObjectId> asChunksId(List<ChunkETY> chunks) {
        return chunks.stream().map(ChunkETY::getId).collect(Collectors.toList());
    }

}
