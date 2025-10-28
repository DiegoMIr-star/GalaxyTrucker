package Connections.Messages;

import model.NextGameStateAndMessages;
import model.ShipDashboard;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the server in order to place the ship on the board
 */
public class PlaceShipRequestMessage extends Message implements Serializable {

    /**
     * Player ship
     */
    ShipDashboard ship;

    /**
     * Message constructor, it initializes the attribute
     * @param nickname player nickname
     * @param ship player ship
     */
    public PlaceShipRequestMessage(String nickname, ShipDashboard ship) {
        super(nickname, MessageKind.PLACE_SHIP_REQUEST);
        this.ship = ship;
    }

    /**
     * Getter of the ship
     * @return current ship
     */
    public ShipDashboard getShip() {return ship;}

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
