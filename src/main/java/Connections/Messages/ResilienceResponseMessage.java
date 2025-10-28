package Connections.Messages;

import model.NextGameStateAndMessages;
import model.ShipDashboard;

import java.io.IOException;

/**
 * Message sent to the server used to advice your own presence during the game
 */
public class ResilienceResponseMessage extends Message{

    /**
     * Current ship, used to resume the ship after a disconnection in the construction
     */
    private ShipDashboard ship = new ShipDashboard();

    /**
     * Constructor of the message
     * @param nickname player nickname
     */
    public ResilienceResponseMessage(String nickname) {super(nickname, MessageKind.RES_RESP);}

    /**
     * Setter of the current ship
     * @param ship current ship
     */
    public void setShip(ShipDashboard ship){this.ship=ship;}

    /**
     * Getter of the ship
     * @return current ship
     */
    public ShipDashboard getShip(){return this.ship;}

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
    public NextGameStateAndMessages accept(MessageVisitorProgresser visitor) {return null;}

    /**
     * Accept method used to check whether said message should be saved as the "lastMessage" addressed to the client
     * in case of reconnection with resilience
     * @param visitor visitor object
     * @return next game state and messages object
     */
    @Override
    public boolean accept(MessageVisitorChecker visitor) {
        return false;
    }
}
