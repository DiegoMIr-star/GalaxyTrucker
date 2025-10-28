package Connections.Messages;

import model.Cards.Card;
import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the client in order to give a drawn card
 */
public class CardDrawnMessage extends Message implements Serializable {

	/**
	 * Current drawn card
	 */
	private final Card drawnCard;

	/**
	 * Message constructor, used to initialize the attribute
	 * @param drawnCard current drawn card
	 */
	public CardDrawnMessage(Card drawnCard){
		super("Server",MessageKind.CARD_DRAWN_RESPONSE);
		this.drawnCard = drawnCard;
	}

	/**
	 * Card getter
	 * @return drawn card
	 */
	public Card getDrawnCard(){ return drawnCard;}

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
