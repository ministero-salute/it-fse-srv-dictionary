package it.finanze.sanita.fse2.ms.edssrvdictionary.utility;

import javax.validation.Path;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public final class UtilsMisc {

    /**
     * Private constructor to disallow to access from other classes
     */
    private UtilsMisc() {}

    public static OffsetDateTime convertToOffsetDateTime(Date dateToConvert) {
        return dateToConvert.toInstant().atOffset(ZoneOffset.UTC);
    }

    public static String extractKeyFromPath(Path path) {
        String field = "";
        for(Path.Node node: path) field = node.getName();
        return field;
    }
}
