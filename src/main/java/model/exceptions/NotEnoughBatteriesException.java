package model.exceptions;

/**
 * Thrown when there's not enough batteries for a specific action
 * @see model.ShipDashboard
 */
public class NotEnoughBatteriesException extends RuntimeException {
	public NotEnoughBatteriesException(String message) {
		super(message);
	}
}
