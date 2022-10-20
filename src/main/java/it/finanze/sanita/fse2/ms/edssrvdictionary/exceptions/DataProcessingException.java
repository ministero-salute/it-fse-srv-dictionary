/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions;

import java.io.IOException;

/**
 * Describe data elaboration exception (I/O conversions)
 * @author G. Baittiner
 */
public class DataProcessingException extends IOException {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 6134857493429760036L;

    /**
     * Constructs an {@code IOException} with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    public DataProcessingException(String message) {
        super(message);
    }

    /**
     * Complete constructor.
     *
     * @param msg	Message to be shown.
     * @param e		Exception to be shown.
     */
    public DataProcessingException(final String msg, final Exception e) {
        super(msg, e);
    }

}