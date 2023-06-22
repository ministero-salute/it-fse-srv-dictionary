package it.finanze.sanita.fse2.ms.edssrvdictionary.client.routes;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.MicroservicesURLCFG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.client.routes.base.ClientRoutes.Query.*;


@Component
public final class QueryClientRoutes {

    @Autowired
    private MicroservicesURLCFG microservices;

    private UriComponentsBuilder base() {
        return UriComponentsBuilder.fromHttpUrl(microservices.getQueryHost());
    }

    public String history(Date lastUpdate) {
        return base()
            .pathSegment(API_VERSION, HISTORY_PATH)
            .queryParam(LAST_UPDATE_QP, lastUpdate)
            .build()
            .toUriString();
    }

    public String resource(String resourceId, String versionId) {
        return base()
            .pathSegment(API_VERSION, RESOURCE_PATH, resourceId, versionId)
            .build()
            .toUriString();
    }

}
