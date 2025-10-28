package model.exceptions;

/**
 * Thrown when the component is not a cabin
 * @see model.ShipDashboard
 */
public class IncompatibleTargetComponent extends RuntimeException {
	public IncompatibleTargetComponent(String message) {
		super(message);
	}
}
