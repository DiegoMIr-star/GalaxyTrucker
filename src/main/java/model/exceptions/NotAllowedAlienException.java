package model.exceptions;

/**
 * Thrown when you try to put an alien where's not allowed
 * @see model.DifferentShipComponents.Cabin
 */
public class NotAllowedAlienException extends RuntimeException {
    public NotAllowedAlienException(String message) {
      super(message);
    }
}
