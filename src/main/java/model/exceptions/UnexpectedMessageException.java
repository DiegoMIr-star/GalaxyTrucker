package model.exceptions;

/**
 * Thrown when it's received an unexpected message
 * @see Controller.Controller
 */
public class UnexpectedMessageException extends RuntimeException {
	public UnexpectedMessageException(String message) {
		super(message);
	}
}
