package Connections.Messages;

import model.NextGameStateAndMessages;
import model.ShipDashboard;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the client in order to send the updated ship
 */
public class UpdateClientShipMessage extends Message implements Serializable {

	/**
	 * Updated ship
	 */
	ShipDashboard updatedShip;

	/**
	 * Message constructor, it initializes the updated ship
	 * @param updatedShip current updated ship
	 */
	public UpdateClientShipMessage(ShipDashboard updatedShip) {
		super("Server", MessageKind.UPDATED_SHIP);
		this.updatedShip = updatedShip;
	}

	/**
	 * Getter of the updated ship
	 * @return updated ship
	 */
	public ShipDashboard getUpdatedShip() {return updatedShip;}

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
