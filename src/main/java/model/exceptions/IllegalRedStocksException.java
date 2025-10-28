package model.exceptions;

/**
 * Thrown when a normal cargo hold has special red stocks
 * @see model.DifferentShipComponents.CargoHold
 */
public class IllegalRedStocksException extends RuntimeException {

	/**
	 * Constructor of the exception with the specified message
	 * @param message is the message necessary to explain this exception's reason
	 */
	public IllegalRedStocksException(String message) {
		super(message);
	}
}
