package Connections.Messages;

import Controller.State.GameState;
import model.NextGameStateAndMessages;

import java.io.IOException;

/**
 * Message sent to the client in order to update the game state
 */
public class GameStateMessage extends Message{

    /**
     * Current game state
     */
    private final GameState gameState;

    /**
     * Message constructor, used to initialize the game state
     * @param gameState current game state
     */
    public GameStateMessage(GameState gameState){
        super("Server",MessageKind.STATE);
        this.gameState = gameState;
    }

    /**
     * Getter of the game state
     * @return current game state
     */
    public GameState getGameState(){ return gameState;}

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
