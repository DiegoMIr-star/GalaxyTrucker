package model.exceptions;

/**
 * Thrown when a callback turns out to be null, unexpectedly
 */
public class NullCallbackException extends RuntimeException {

	/**
	 * Constructor of the exception with the specified message
	 * @param message is the message necessary to explain this exception's reason
	 */
	public NullCallbackException(String message) {
		super(message);
	}
}
