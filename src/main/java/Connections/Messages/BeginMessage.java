package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Message sent to the client in order to begin the game
 */
public class BeginMessage extends Message {

    /**
     * List of players, used to show the players
     */
    private final ArrayList<String> players;

    /**
     * Message constructor, it initializes the list of players
     * @param players list of players
     */
    public BeginMessage(ArrayList<String> players) {
        super("Server",MessageKind.BEGIN);
        this.players = players;
    }

    /**
     * Players getter
     * @return list of players
     */
    public ArrayList<String> getPlayers() { return players;}

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
