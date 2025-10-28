package model.exceptions;

/**
 * Thrown when a player who hasn't placed the ship tries to start the final hourglass
 */
public class AccessToHourglassDeniedException extends RuntimeException {

    /**
     * Constructor of the exception with the specified message
     * @param message is the message necessary to explain this exception's reason
     */
    public AccessToHourglassDeniedException(String message) {
        super(message);
    }
}
