package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the server in order to claim a reward
 */
public class ClaimRewardChoiceMessage extends Message implements Serializable {

	/**
	 * Player choice, true if the player wants to claim the reward
	 */
	private final boolean choice;

	/**
	 * Message constructor, used to initialize the attribute
	 * @param nickname player nickname
	 * @param choice player choice
	 */
	public ClaimRewardChoiceMessage(String nickname, boolean choice) {
		super(nickname, MessageKind.CLAIM_REWARD_CHOICE);
		this.choice = choice;
	}

	/**
	 * Method used to get the attribute
	 * @return player choice
	 */
	@Override
	public boolean isClaimed() {return choice;}

	/**
	 * Method used to dispatch the messages with visitor
	 * @param visitor visitor logic with the logic of the different messages
	 */
	@Override
	public void accept(MessageVisitor visitor) throws IOException {visitor.visit(this);}

	/**
	 * Method used to handle different messages in the controller
	 * @see Controller.Controller
	 * @param visitor object visitor
	 * @return object which contains the next state with messages
	 */
	@Override
	public NextGameStateAndMessages accept(MessageVisitorProgresser visitor) {return visitor.visit(this);}

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
