package model.exceptions;

/**
 * Thrown when there are not enough stocks to remove
 * @see model.Stocks
 */
public class OutOfStockException extends RuntimeException {
	public OutOfStockException(String message) {
		super(message);
	}
}
