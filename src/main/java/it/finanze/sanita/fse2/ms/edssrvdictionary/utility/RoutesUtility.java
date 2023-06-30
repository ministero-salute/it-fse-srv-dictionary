/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.utility;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class RoutesUtility {

    public static final String API_VERSION = "v1";
    public static final String API_CHANGESET = "changeset";

    public static final String API_TERMINOLOGY = "terminology";
    public static final String API_RESOURCE = "resource";
    public static final String API_RESOURCE_ID = "id";
    public static final String API_RESOURCE_VERSION = "version";
    public static final String API_INTEGRITY = "integrity";

    public static final String WEB_SCRAPING = "web-scraping";

    public static final String API_PATH_SYSTEM_VAR = "system";
    public static final String API_PATH_URL = "url";

    public static final String API_QP_LAST_UPDATE = "lastUpdate";
    public static final String API_SYSTEM_EXTS = "/{" + API_PATH_SYSTEM_VAR + "}";
    public static final String API_RESOURCE_ID_EXTS = "/{" + API_RESOURCE_ID + "}";
    public static final String API_RESOURCE_VERS_EXTS = "/{" + API_RESOURCE_VERSION + "}";

    public static final String API_URL_EXTS = "/{" + API_PATH_URL + "}";
    public static final String API_TERMS_MAPPER = API_VERSION + "/" + API_TERMINOLOGY;


    public static final String API_CHANGESET_MAPPER = API_VERSION + "/" + API_CHANGESET;
    public static final String API_CHANGESET_CHUNKS = "/" + API_TERMINOLOGY ;
    public static final String API_CHANGESET_RESOURCE = "/" + API_TERMINOLOGY + "/" + API_RESOURCE + API_RESOURCE_ID_EXTS + API_RESOURCE_VERS_EXTS;
    public static final String API_CHANGESET_INTEGRITY = "/" + API_TERMINOLOGY + "/" + API_INTEGRITY;

    public static final String API_CHANGESET_TAG = "ChangeSet";

    public static final String API_WEB_SCRAPING = API_VERSION + "/" + WEB_SCRAPING;
    public static final String API_WEB_DELETE_SYSTEM = API_SYSTEM_EXTS;
    public static final String API_WEB_DELETE_MULTI = "/delete-multi";
    public static final String API_WEB_SYSTEM_URL = API_SYSTEM_EXTS + API_URL_EXTS;
    public static final String API_WEB_INSERT_MULTI = "/insert-multi";

    public static final String API_DOCUMENTS_TAG = "Documents";

    public static final String API_QP_REF = "ref";
    public static final String API_QP_CHUNK = "chunk";

}
