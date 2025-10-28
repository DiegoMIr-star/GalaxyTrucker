package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;

/**
 * Message sent to the server in order to pick a specific component
 */
public class PickRevealedComponentRequestMessage extends Message {

	/**
	 * Component ID
	 */
	int componentIndex;

	/**
	 * Message constructor, it initializes the attribute
	 * @param nickname player nickname
	 * @param componentIndex current component to pick
	 */
	public PickRevealedComponentRequestMessage(String nickname, int componentIndex) {
		super(nickname, MessageKind.PICK_REVEALED_COMP_REQUEST);
		this.componentIndex = componentIndex;
	}

	/**
	 * Getter of the component ID to pick
	 * @return component ID
	 */
	public int getComponentIndex() {return componentIndex;}

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
