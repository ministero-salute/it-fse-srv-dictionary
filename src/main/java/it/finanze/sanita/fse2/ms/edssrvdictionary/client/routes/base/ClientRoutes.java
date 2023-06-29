package it.finanze.sanita.fse2.ms.edssrvdictionary.client.routes.base;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientRoutes {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Query {
        // PATH PARAMS
        public static final String LAST_UPDATE_QP = "lastUpdate";
        // ENDPOINT
        public static final String API_VERSION = "v1";
        public static final String HISTORY_PATH = "history";
        public static final String RESOURCE_PATH = "resource";
        public static final String INTEGRITY_PATH = "integrity";
    }

}
