package Connections.Messages;

import model.NextGameStateAndMessages;
import model.ShipDashboard;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message sent in the end of the game, in order to show the list of winners
 */
public class WinnersMessage extends Message implements Serializable {

    /**
     * List of ship dashboards of winners
     */
    ArrayList<ShipDashboard> winners;

    /**
     * Constructor of the winners message
     * @param winners current list of winners
     */
    public WinnersMessage(ArrayList<ShipDashboard> winners) {
        super("Server", MessageKind.WINNERS_MESSAGE);
        this.winners = winners;
    }

    /**
     * Getter of winners
     * @return current list of winners
     */
    public ArrayList<ShipDashboard> getWinners() {return winners;}

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
