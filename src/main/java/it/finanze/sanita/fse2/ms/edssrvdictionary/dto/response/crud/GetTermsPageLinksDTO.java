package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud;

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.data.domain.Page;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Data
@AllArgsConstructor
public class GetTermsPageLinksDTO {

    private String next;
    private String prev;

    public static GetTermsPageLinksDTO fromPage(String system, Page<TerminologyETY> page) {
        return new GetTermsPageLinksDTO(
            getNext(system, page),
            getPrev(system, page)
        );
    }

    private static String getPrev(String system, Page<TerminologyETY> page) {
        // Default state
        String prev = null;
        // Check if there is a previous page and the current page is on the right index
        if(page.hasPrevious() && page.hasContent()) {
            // Create URI
            prev = new URIBuilder(fromCurrentContextPath().build().toUri())
                .setPathSegments(
                    API_VERSION,
                    API_TERMINOLOGY,
                    system
                )
                .addParameter(API_QP_PAGE, String.valueOf(page.getNumber() - 1))
                .addParameter(API_QP_LIMIT, String.valueOf(page.getSize()))
                .toString();
        }
        return prev;
    }

    private static String getNext(String system, Page<TerminologyETY> page) {
        // Default state
        String next = null;
        // Check if there is a next page
        if(page.hasNext()) {
            // Create URI
            next = new URIBuilder(fromCurrentContextPath().build().toUri())
                .setPathSegments(
                    API_VERSION,
                    API_TERMINOLOGY,
                    system
                )
                .addParameter(API_QP_PAGE, String.valueOf(page.getNumber() + 1))
                .addParameter(API_QP_LIMIT, String.valueOf(page.getSize()))
                .toString();
        }
        return next;
    }

}
