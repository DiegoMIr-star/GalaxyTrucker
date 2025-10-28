package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message sent to the client with the indexes of free planets
 */
public class FreePlanetsResponseMessage extends Message implements Serializable {

    /**
     * List of free planets indexes
     */
    private final ArrayList<Integer> freePlanetsIndexes;

    /**
     * Message constructor, used to initialize the indexes
     * @param freePlanetsIndexes list of free planets indexes
     */
    public FreePlanetsResponseMessage(ArrayList<Integer> freePlanetsIndexes) {
        super("Server", MessageKind.FREE_PLANETS_RESPONSE);
        this.freePlanetsIndexes = freePlanetsIndexes;
    }

    /**
     * Getter of the free planets indexes
     * @return list of free planets indexes
     */
    public ArrayList<Integer> getFreePlanetsIndexes() {return freePlanetsIndexes;}

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
