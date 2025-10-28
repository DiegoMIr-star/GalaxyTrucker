package model.exceptions;

/**
 * Thrown when the coordinates of a ship are not respected
 * @see View.TUI
 * @see model.ShipDashboard
 */
public class IllegalPositionException extends RuntimeException {
	public IllegalPositionException(String message) {
		super(message);
	}
}
