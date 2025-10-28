package model.exceptions;

/**
 * Thrown when there's a card, which is not expected
 * @see Controller.Controller
 */
public class WrongCardException extends RuntimeException {
	public WrongCardException(String message) {
		super(message);
	}
}
