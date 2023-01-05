/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions;

import lombok.Getter;

public class InvalidContentException extends Exception {

    @Getter
    private final String field;

	/**
     * Message constructor.
     *
     * @param msg	Message to be shown.
     */
    public InvalidContentException(final String msg, final String field) {
        super(msg);
        this.field = field;
    }

}
