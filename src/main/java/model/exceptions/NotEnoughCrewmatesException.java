package model.exceptions;

/**
 * Thrown when there are not enough crew mates to remove
 * @see model.ShipDashboard
 */
public class NotEnoughCrewmatesException extends RuntimeException {
	public NotEnoughCrewmatesException(String message) {
		super(message);
	}
}
