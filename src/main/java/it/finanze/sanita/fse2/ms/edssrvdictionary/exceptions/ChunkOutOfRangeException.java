/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions;


/**
 * Represent the absence of a given chunk due to invalid index
 * @author G. Baittiner
 */
public class ChunkOutOfRangeException extends Exception {

    /**
     * Message constructor.
     *
     * @param msg	Message to be shown.
     */
    public ChunkOutOfRangeException(final String msg) {
        super(msg);
    }

}