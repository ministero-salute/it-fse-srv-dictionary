/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.utility;

import javax.validation.Path;

public final class MiscUtility {

    /**
     * Private constructor to disallow to access from other classes
     */
    private MiscUtility() {
        // This method is intentionally left blank.
    }

    public static String extractKeyFromPath(Path path) {
        String field = "";
        for(Path.Node node: path) field = node.getName();
        return field;
    }

}
