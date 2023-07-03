package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import org.apache.http.client.utils.URIBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Value
@JsonInclude(NON_NULL)
public class PaginationLinks {

    String next;
    public static PaginationLinks from(
        String id,
        String resource,
        String version,
        int current,
        int size
    ) {
        PaginationLinks out = null;
        if(current < size - 1) {
            String next = new URIBuilder(fromCurrentContextPath().build().toUri())
                .setPathSegments(
                    API_VERSION,
                    API_CHANGESET,
                    API_TERMINOLOGY,
                    API_RESOURCE,
                    resource,
                    version
                )
                .addParameter(API_QP_REF, id)
                .addParameter(API_QP_CHUNK, String.valueOf(current + 1))
                .toString();
            out = new PaginationLinks(next);
        }
        return out;
    }
}
