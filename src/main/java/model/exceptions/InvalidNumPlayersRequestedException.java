package model.exceptions;

/**
 * Thrown when the number of players is not allowed
 * @see Controller.Controller
 */
public class InvalidNumPlayersRequestedException extends RuntimeException {
    public InvalidNumPlayersRequestedException(String message) {
        super(message);
    }
}
