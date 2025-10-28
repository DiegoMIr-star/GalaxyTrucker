package model.exceptions;

/**
 * Thrown when the capacity of a cargo hold is not respected
 * @see model.DifferentShipComponents.CargoHold
 */
public class CapacityException extends RuntimeException {
	public CapacityException(String message) {
		super(message);
	}
}
