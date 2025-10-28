package model.exceptions;

/**
 * Thrown when you try to add too many aliens in a cabin
 * @see model.ShipDashboard
 */
public class TooManyAliensException extends RuntimeException {
	public TooManyAliensException(String message) {
		super(message);
	}
}
