package model.exceptions;

/**
 * Thrown when there is more than one alien on a single ship cell
 */
public class IllegalAlienAmountException extends RuntimeException {

	/**
	 * Constructor of the exception with the specified message
	 * @param message is the message necessary to explain this exception's reason
	 */
	public IllegalAlienAmountException(String message) {
		super(message);
	}
}
