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

    public static final String API_PATH_ID_VAR = "id";
    public static final String API_PATH_IDX_VAR = "idx";
    public static final String API_PATH_SYSTEM_VAR = "system";

    public static final String API_QP_LAST_UPDATE = "lastUpdate";
    public static final String API_SYSTEM_EXTS = "/{" + API_PATH_SYSTEM_VAR + "}";
    public static final String API_IDX_EXTS = "/{" + API_PATH_IDX_VAR + "}";
    public static final String API_ID_EXTS = "/{" + API_PATH_ID_VAR + "}";
    public static final String API_TERMS_MAPPER = API_VERSION + "/" + API_TERMINOLOGY;
    public static final String API_TERMS_GET_ONE_BY_ID = API_ID + API_ID_EXTS;

    public static final String API_CHANGESET_MAPPER = API_VERSION + "/" + API_CHANGESET;
    public static final String API_CHANGESET_STATUS = "/" + API_TERMINOLOGY + "/" + API_STATUS ;
    public static final String API_CHANGESET_CHUNKS = "/" + API_TERMINOLOGY + "/" + API_CHUNKS ;
    public static final String API_CHANGESET_CHUNKS_INS = API_CHANGESET_CHUNKS + "/" + API_CHUNKS_INS + API_ID_EXTS + API_IDX_EXTS;
    public static final String API_CHANGESET_CHUNKS_DEL = API_CHANGESET_CHUNKS + "/" + API_CHUNKS_DEL + API_ID_EXTS + API_IDX_EXTS;

    public static final String API_CHANGESET_TAG = "ChangeSet";
    public static final String API_CHANGESET_CHUNKS_TAG = "ChangeSet - Chunks";

    public static final String API_DOCUMENTS_TAG = "Documents";

}
