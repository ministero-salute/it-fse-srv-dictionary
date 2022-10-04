package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions;

import java.io.IOException;

/**
 * When the database output is not the expected one, this exception kicks in
 * @author G. Baittiner
 */
public class DataIntegrityException extends IOException {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 6134857493429760036L;

    /**
     * Complete constructor.
     *
     * @param msg	Message to be shown.
     */
    public DataIntegrityException(final String msg) {
        super(msg);
    }

}