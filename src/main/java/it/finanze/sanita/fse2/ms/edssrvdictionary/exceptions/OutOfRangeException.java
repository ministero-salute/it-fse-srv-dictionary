package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions;


/**
 * Represent an invalid index upon a sortable, indexable data
 * @author G. Baittiner
 */
public class OutOfRangeException extends Exception {

    /**
     * Message constructor.
     *
     * @param msg	Message to be shown.
     */
    public OutOfRangeException(final String msg) {
        super(msg);
    }

}