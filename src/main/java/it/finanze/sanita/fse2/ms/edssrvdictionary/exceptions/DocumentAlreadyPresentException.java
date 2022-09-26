package it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions;

public class DocumentAlreadyPresentException extends Exception {

    /**
	 * Serial version UID 
	 */
	private static final long serialVersionUID = 7925016596669482549L;

	
	/**
     * Message constructor.
     *
     * @param msg	Message to be shown.
     */
    public DocumentAlreadyPresentException(final String msg) {
        super(msg);
    }
}
