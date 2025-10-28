package View.ColorManagement;

/**
 * Thrown when an unexpected gradient is found
 */
public class UnexpectedGradientException extends RuntimeException {

	/**
	 * Constructor of the exception with the specified message
	 * @param message is the message necessary to explain this exception's reason
	 */
	public UnexpectedGradientException(String message) {
		super(message);
	}
}
