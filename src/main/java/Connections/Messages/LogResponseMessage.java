package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the client in order to signal if the connection is established or not,
 * it shows also the nickname status
 */
public class LogResponseMessage extends Message implements Serializable {

    /**
     * True if the connection is established
     */
    private final boolean isConnected;

    /**
     * True if the nickname is not used
     */
    private final boolean nicknameStatus;

    /**
     * Number of players in the game
     */
    private int playerNumber;

    /**
     * Message constructor, used to initialize the attributes
     * @param isConnectedToServer true if the connection is established
     * @param nicknameStatus true if the nickname is not used
     * @param playerNumber number of players in the game
     */
    public LogResponseMessage(boolean isConnectedToServer,boolean nicknameStatus, int playerNumber) {
        // in this case the nickname of the message sender id the server
        super("Server", MessageKind.LOG_RESPONSE);
        //if it's connected, it means that the nickname is unique
        this.isConnected=isConnectedToServer;
        this.nicknameStatus=nicknameStatus;
        this.playerNumber=playerNumber;
    }

    /**
     * Method used to print the message
     * @return string for printing
     */
    @Override
    public String toString() {return "Log in Response Message: Connection of " + getNickname() + " to the server is " + isConnected ;}

    /**
     * Getter of isConnected
     * @return true if the connection is established
     */
    public boolean isConnected() {return isConnected;}

    /**
     * Getter of the nickname status
     * @return true if the nickname is not used
     */
    public boolean getNicknameStatus(){return nicknameStatus;}

    /**
     * Getter of players number
     * @return amount of players
     */
    public int getPlayerNumber(){return playerNumber;}

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
