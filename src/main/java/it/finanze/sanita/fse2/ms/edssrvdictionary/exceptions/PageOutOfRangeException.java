package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions;


/**
 * Represent the absence of a given page due to invalid index
 * @author G. Baittiner
 */
public class PageOutOfRangeException extends Exception {

    /**
     * Message constructor.
     *
     * @param msg	Message to be shown.
     */
    public PageOutOfRangeException(final String msg) {
        super(msg);
    }

}