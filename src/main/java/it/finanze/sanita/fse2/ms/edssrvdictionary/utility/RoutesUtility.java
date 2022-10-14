package it.finanze.sanita.fse2.ms.edssrvdictionary.utility;

public final class RoutesUtility {

    /**
     * Private constructor to disallow to access from other classes
     */
    private RoutesUtility() {}

    public static final String API_VERSION = "v1";
    public static final String API_CHANGESET = "changeset";
    public static final String API_QP_LAST_UPDATE = "lastUpdate";
    public static final String API_STATUS = "status";
    public static final String API_CHUNKS = "chunks";
    public static final String API_TERMINOLOGY = "terminology";

    public static final String API_CHANGESET_MAPPER = API_VERSION + "/" + API_CHANGESET;
    public static final String API_CHANGESET_STATUS = "/" + API_TERMINOLOGY + "/" + API_STATUS ;
    public static final String API_CHANGESET_CHUNKS = "/" + API_TERMINOLOGY + "/" + API_CHUNKS ;

    public static final String API_CHANGESET_TAG = "Changeset";
    public static final String API_CHANGESET_CHUNKS_TAG = "Changeset - Chunks";

    public static final String API_DOCUMENTS_TAG = "Documents";

}
