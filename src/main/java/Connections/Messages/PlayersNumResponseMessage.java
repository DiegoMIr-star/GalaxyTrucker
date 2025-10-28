package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the server in order to signal the amount of players in a game
 */
public class PlayersNumResponseMessage extends Message implements Serializable {

    /**
     * Players numbers
     */
    private final int numPlayersRequested;

    /**
     * Constructor of the message: it initializes the attribute
     * @param nickname player nickname
     * @param numPlayersRequested number of the players
     */
    public PlayersNumResponseMessage(String nickname, int numPlayersRequested) {
        super(nickname, MessageKind.PLAYERS_NUM_RESPONSE);
        this.numPlayersRequested = numPlayersRequested;
    }

    /**
     * Getter of the amount of players
     * @return amount of players
     */
    public int getNumPlayersRequested() {return numPlayersRequested;}

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
