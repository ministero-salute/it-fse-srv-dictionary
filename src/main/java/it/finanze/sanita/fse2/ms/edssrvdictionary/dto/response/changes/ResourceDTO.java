package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.PaginationLinks;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.base.ResMetaETY;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceItemDTO;

@Data
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class ResourceDTO {

    private String traceID;
    private String spanID;

    private String resourceId;
    private String versionId;

    private ResMetaETY meta;
    private List<ResourceItemDTO> items;

    private PaginationLinks links;

    public static ResourceDTO from(ChunksIndexETY index, ChunkETY chunk, int current) {
        String ref = index.getId().toString();
        String resource = index.getResource();
        String version = index.getVersion();
        int size = index.getChunks().size();
        return new ResourceDTO(
            null,
            null,
            resource,
            version,
            index.getMeta(),
            chunk.getChunk().getValues(),
            PaginationLinks.from(ref, resource, version, current, size)
        );
    }

    public ResourceDTO trackWith(LogTraceInfoDTO trace) {
        setTraceID(trace.getTraceID());
        setSpanID(trace.getSpanID());
        return this;
    }
}
