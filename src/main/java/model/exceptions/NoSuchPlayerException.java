package model.exceptions;

/**
 * Thrown when there's no such players with a certain nickname in a message
 * @see Controller.Controller
 */
public class NoSuchPlayerException extends RuntimeException {
	public NoSuchPlayerException(String message) {
		super(message);
	}
}
