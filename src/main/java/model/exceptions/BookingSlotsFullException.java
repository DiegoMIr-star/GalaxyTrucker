package model.exceptions;

/**
 * Thrown when there are still two booked components in a ship dashboard
 * @see model.ShipDashboard
 */
public class BookingSlotsFullException extends RuntimeException {
	public BookingSlotsFullException(String message) {
		super(message);
	}
}
