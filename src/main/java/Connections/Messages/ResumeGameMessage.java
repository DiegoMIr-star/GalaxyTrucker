package Connections.Messages;

import Controller.State.GameState;
import model.Cards.Card;
import model.NextGameStateAndMessages;
import model.ShipDashboard;

import java.io.IOException;

/**
 * Message used to resume a game when the client has disconnected
 */
public class ResumeGameMessage extends Message{

    /**
     * Current card after disconnection
     */
    private Card curCard;

    /**
     * Current game state after disconnection
     */
    private GameState gameState;

    /**
     * Current ship after disconnection
     */
    private ShipDashboard ship;

    /**
     * Last message sent before disconnection
     */
    private Message lastMessage;

    /**
     * Constructor of the message
     */
    public ResumeGameMessage() {super("Server", MessageKind.RESUME);}

    /**
     * Setter of the ship
     * @param ship current ship
     */
    public void setShip(ShipDashboard ship) {this.ship = ship;}

    /**
     * Setter of the game state
     * @param gameState current game state
     */
    public void setGameState(GameState gameState) {this.gameState = gameState;}

    /**
     * Setter of the current card
     * @param curCard current card
     */
    public void setCurCard(Card curCard) {this.curCard = curCard;}

    /**
     * Setter of the last message
     * @param lastMessage last message
     */
    public void setLastMessage(Message lastMessage) {this.lastMessage = lastMessage;}

    /**
     * Getter of the last message
     * @return last message
     */
    public Message getLastMessage() {return lastMessage;}

    /**
     * Getter of the current card
     * @return current card
     */
    public Card getCurCard() {return curCard;}

    /**
     * Getter of the current game state
     * @return current game state
     */
    public GameState getGameState() {return gameState;}

    /**
     * Getter of the current ship
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
