package model.exceptions;

/**
 * Thrown when you try to put a human where's not allowed
 * @see model.DifferentShipComponents.Cabin
 */
public class NotAllowedHumanException extends RuntimeException {
    public NotAllowedHumanException(String message) {
        super(message);
    }
}
