package model.exceptions;

/**
 * Thrown when a certain slot is already taken
 * @see model.ShipDashboard
 */
public class SlotTakenException extends RuntimeException {
	public SlotTakenException(String message) {
		super(message);
	}
}
