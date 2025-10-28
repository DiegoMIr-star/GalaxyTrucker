package Connections.Messages;

import model.NextGameStateAndMessages;
import model.ShipDashboard;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Message sent to the client in order to notify players positions and turns
 */
public class PositionsAndTurnsMessage extends Message{

	/**
	 * List of different players
	 */
	ArrayList<ShipDashboard> players;

	/**
	 * True if the positions can be printed
	 */
	boolean printPositions;

	/**
	 * Constructor of the message
	 * @param players current list of players
	 * @param printPositions true if the positions can be printed
	 */
	public PositionsAndTurnsMessage(ArrayList<ShipDashboard> players, boolean printPositions) {
		super("Server",MessageKind.POSITION);
		this.players = players;
		this.printPositions = printPositions;
	}

	/**
	 * Getter of the list of players
	 * @return list of players
	 */
	public ArrayList<ShipDashboard> getPlayers() {return players;}

	/**
	 * Getter of the boolean for printing positions
	 * @return true if the positions can be printed
	 */
	public boolean getPrintPositions() {return printPositions;}

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
