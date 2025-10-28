package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message sent to the client in order to give available small decks
 */
public class AvailableSmallDeckMessage extends Message implements Serializable {

    /**
     * List od small decks indexes
     */
    ArrayList<Integer> decksIndex;

    /**
     * Message constructor, used to initialize the attribute
     * @param decksIndex list of small decks indexes
     */
    public AvailableSmallDeckMessage(ArrayList<Integer> decksIndex) {
        super("Server", MessageKind.GET_AVAILABLE_SMALL_DECKS);
        this.decksIndex = decksIndex;
    }

    /**
     * Getter of the small decks indexes
     * @return list of small decks indexes
     */
    public ArrayList<Integer> getDecksIndexes() {return decksIndex;}

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
