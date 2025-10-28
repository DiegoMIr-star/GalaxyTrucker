package model.exceptions;

/**
 * Thrown when the planet signaled is not on the card
 * @see model.Cards.Planets
 */
public class InvalidIndexException extends RuntimeException {
    public InvalidIndexException(String message) {
        super(message);
    }
}
