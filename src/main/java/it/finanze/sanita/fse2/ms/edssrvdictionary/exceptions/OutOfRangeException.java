package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions;


/**
 * Represent an invalid index upon a sortable, indexable data
 * @author G. Baittiner
 */
public class OutOfRangeException extends Exception {

    private final String field;

    /**
     * Message constructor.
     *
     * @param msg	Message to be shown.
     */
    public OutOfRangeException(final String msg, final String field) {
        super(msg);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}