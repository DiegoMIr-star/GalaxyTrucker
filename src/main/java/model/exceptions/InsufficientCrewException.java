package model.exceptions;

/**
 * Thrown when an action requires more crew members than are available
 */
public class InsufficientCrewException extends RuntimeException {

	/**
	 * Constructor of the exception with the specified message
	 * @param message is the message necessary to explain this exception's reason
	 */
	public InsufficientCrewException(String message) {
		super(message);
	}
}
