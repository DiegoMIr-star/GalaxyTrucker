package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;

/**
 * Message sen to the client in order to signal if the timer is restarted
 */
public class TimerStartedMessage extends Message {

	/**
	 * Amount of seconds for the timer
	 */
	private final int seconds;

	/**
	 * True if the timer is the last one
	 */
	boolean isLast;

	/**
	 * Constructor method used to initialize the attributes of the class
	 * @param seconds amount of seconds
	 * @param isLast true if the timer is the last one
	 */
	public TimerStartedMessage(int seconds, boolean isLast) {
		super("Server", MessageKind.TIMER_STARTED);
		this.seconds = seconds;
		this.isLast = isLast;
	}

	/**
	 * Getter of the second
	 * @return amount of seconds
	 */
	public int getSeconds() {return seconds;}

	/**
	 * Getter of isLast
	 * @return true if the timer is the last one
	 */
	public boolean isLast(){return isLast;}

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
