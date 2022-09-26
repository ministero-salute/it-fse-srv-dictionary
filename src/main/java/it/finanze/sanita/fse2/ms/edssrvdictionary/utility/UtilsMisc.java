package it.finanze.sanita.fse2.ms.edssrvdictionary.utility;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import javax.validation.Path;

public final class UtilsMisc {

    /**
     * Private constructor to disallow to access from other classes
     */
    private UtilsMisc() {}

    public static OffsetDateTime convertToOffsetDateTime(Date dateToConvert) {
        return dateToConvert.toInstant().atOffset(ZoneOffset.UTC);
    }
    /**
     * Encode in Base64 the byte array passed as parameter.
     *
     * @param input	The byte array to encode.
     * @return		The encoded byte array to String.
     */
    public static String encodeBase64(final byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    public static String extractKeyFromPath(Path path) {
        String field = "";
        for(Path.Node node: path) field = node.getName();
        return field;
    }
}
