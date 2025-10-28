package model.exceptions;

/**
 * Thrown when there are no booked components
 * @see Connections.ClientInterface
 */
public class NoBookedComponentException extends RuntimeException {
	public NoBookedComponentException(String message) {
		super(message);
	}
}
