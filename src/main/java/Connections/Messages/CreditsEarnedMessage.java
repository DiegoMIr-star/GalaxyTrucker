package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the client in order to give the earned credits
 */
public class CreditsEarnedMessage extends Message implements Serializable {

	/**
	 * Earned credits
	 */
	private final int creditsEarned;

	/**
	 * Message constructor, used to initialize the earned credits
	 * @param creditsEarned earned credits
	 */
	public CreditsEarnedMessage( int creditsEarned) {
		super("Server", MessageKind.CREDITS_EARNED);
		this.creditsEarned = creditsEarned;
	}

	/**
	 * Getter of earned credits
	 * @return earned credits
	 */
	public int getCreditsEarned() {return creditsEarned;}

	/**
	 * Method used to dispatch the messages with visitor
	 * @param visitor visitor logic with the logic of the different messages
	 */
	@Override
	public void accept(MessageVisitor visitor) throws IOException {
		visitor.visit(this);
	}

	/**
	 * Method used to handle different messages in the controller
	 * @see Controller.Controller
	 * @param visitor object visitor
	 * @return object which contains the next state with messages
	 */
	@Override
	public NextGameStateAndMessages accept(MessageVisitorProgresser visitor) {
		return visitor.visit(this);
	}

	/**
	 * Accept method used to check whether said message should be saved as the "lastMessage" addressed to the client
	 * in case of reconnection with resilience
	 * @param visitor visitor object
	 * @return next game state and messages object
	 */
	@Override
	public boolean accept(MessageVisitorChecker visitor) {
		return visitor.visit(this);
	}
}
