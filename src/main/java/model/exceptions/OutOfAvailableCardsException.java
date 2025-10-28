package model.exceptions;

/**
 * Thrown when there are not enough cards of a certain level
 * @see model.Game
 */
public class OutOfAvailableCardsException extends RuntimeException {
    public OutOfAvailableCardsException(String message) {
        super(message);
    }
}
