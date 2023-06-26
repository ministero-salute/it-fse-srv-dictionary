/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.utility;

public final class RoutesUtility {

    /**
     * Private constructor to disallow to access from other classes
     */
    private RoutesUtility() {}

    public static final String API_VERSION = "v1";
    public static final String API_CHANGESET = "changeset";
    public static final String API_STATUS = "status";
    public static final String API_CHUNKS = "chunks";
    public static final String API_ID = "id";
    public static final String API_CHUNKS_INS = "ins";
    public static final String API_CHUNKS_DEL = "del";
    public static final String API_TERMINOLOGY = "terminology";
    public static final String WEB_SCRAPING = "web-scraping";
    public static final String API_CSV = "csv";
    public static final String API_XML = "xml";

    public static final String API_PATH_ID_VAR = "id";
    public static final String API_PATH_IDX_VAR = "idx";
    public static final String API_PATH_SYSTEM_VAR = "system";
    public static final String API_PATH_URL = "url";

    public static final String API_QP_LAST_UPDATE = "lastUpdate";
    public static final String API_QP_PAGE = "page";
    public static final String API_QP_LIMIT= "limit";
    public static final String API_SYSTEM_EXTS = "/{" + API_PATH_SYSTEM_VAR + "}";
    public static final String API_IDX_EXTS = "/{" + API_PATH_IDX_VAR + "}";
    public static final String API_ID_EXTS = "/{" + API_PATH_ID_VAR + "}";
    public static final String API_URL_EXTS = "/{" + API_PATH_URL + "}";
    public static final String API_TERMS_MAPPER = API_VERSION + "/" + API_TERMINOLOGY;
    public static final String API_TERMS_GET_ONE_BY_ID = API_ID + API_ID_EXTS;
    public static final String API_TERMS_GET_BY_CSV = "/" + API_CSV;
    public static final String API_TERMS_GET_BY_XML = "/" + API_XML;

    public static final String API_GET_BY_CSV_FULL = "/" + API_TERMS_MAPPER + API_TERMS_GET_BY_CSV;

    public static final String API_CHANGESET_MAPPER = API_VERSION + "/" + API_CHANGESET;
    public static final String API_CHANGESET_STATUS = "/" + API_TERMINOLOGY + "/" + API_STATUS ;
    public static final String API_CHANGESET_CHUNKS = "/" + API_TERMINOLOGY + "/" + API_CHUNKS ;
    public static final String API_CHANGESET_CHUNKS_INS = API_CHANGESET_CHUNKS + "/" + API_CHUNKS_INS + API_ID_EXTS + API_IDX_EXTS;
    public static final String API_CHANGESET_CHUNKS_DEL = API_CHANGESET_CHUNKS + "/" + API_CHUNKS_DEL + API_ID_EXTS + API_IDX_EXTS;

    public static final String API_CHANGESET_TAG = "ChangeSet";
    public static final String API_CHANGESET_CHUNKS_TAG = "ChangeSet - Chunks";

    public static final String API_WEB_SCRAPING = API_VERSION + "/" + WEB_SCRAPING;
    public static final String API_WEB_DELETE_SYSTEM = API_SYSTEM_EXTS;
    public static final String API_WEB_DELETE_MULTI = "/delete-multi";
    public static final String API_WEB_SYSTEM_URL = API_SYSTEM_EXTS + API_URL_EXTS;
    public static final String API_WEB_INSERT_MULTI = "/insert-multi";

    public static final String API_DOCUMENTS_TAG = "Documents";

}
