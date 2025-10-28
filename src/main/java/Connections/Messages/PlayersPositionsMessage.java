package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the client in order to notify the current positions of the players
 */
public class PlayersPositionsMessage extends Message implements Serializable {

    /**
     * List of turns
     */
    private final int[] turns;

    /**
     * List of positions
     */
    private final int[] positions;

    /**
     * Constructor of the message: it initializes the attributes
     * @param turns current turns
     * @param positions current positions
     */
    public PlayersPositionsMessage(int[] turns, int[] positions) {
        super("Server", MessageKind.POSITION);
        this.turns = turns;
        this.positions = positions;
    }

    /**
     * Getter of turn
     * @return current turns
     */
    public int[] getTurns() {return turns;}

    /**
     * Getter of positions
     * @return current positions
     */
    public int[] getPositions() {return positions;}

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
