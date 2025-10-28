package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the server in order to end the game
 */
public class EndGameMessage extends Message implements Serializable {

    /**
     * It's one for game over, two to begin another game
     */
    private final int choice;

    /**
     * Message constructor used to initialize the attribute
     * @param nickname player nickname
     * @param choice current choice
     */
    public EndGameMessage(String nickname, int choice) {
        super(nickname, MessageKind.END_GAME_REQUEST);
        this.choice = choice;
    }

    /**
     * Getter of the choice
     * @return current choice
     */
    public int getChoice() {return choice;}

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
