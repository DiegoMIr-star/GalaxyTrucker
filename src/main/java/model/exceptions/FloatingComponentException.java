package model.exceptions;

/**
 * Thrown when a component is not attached to the rest of the ship
 * @see model.ShipDashboard
 */
public class FloatingComponentException extends RuntimeException {
	public FloatingComponentException(String message) {
		super(message);
	}
}
