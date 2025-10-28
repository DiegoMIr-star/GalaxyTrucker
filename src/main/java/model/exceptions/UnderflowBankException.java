package model.exceptions;

/**
 * Thrown when you try to ask an exaggerated amount to the bank
 * @see model.Bank
 */
public class UnderflowBankException extends RuntimeException {
    public UnderflowBankException(String message) {
        super(message);
    }
}
