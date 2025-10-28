package model.exceptions;

/**
 * Thrown when a motor power value turns out to be zero
 */
public class ZeroMotorPowerException extends RuntimeException {

    /**
     * Constructor of the exception with the specified message
     * @param message is the message necessary to explain this exception's reason
     */
    public ZeroMotorPowerException(String message) {
        super(message);
    }
}
