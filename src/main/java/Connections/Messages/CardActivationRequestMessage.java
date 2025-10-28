package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the server in order to activate a card
 */
public class CardActivationRequestMessage extends Message implements Serializable {

    /**
     * True if the client wants to activate the card
     */
    private final boolean yes;

    /**
     * Message constructor, used to initialize the attribute
     * @param nickname player nickname
     * @param yes player choice
     */
    public CardActivationRequestMessage(String nickname, boolean yes) {
        super(nickname, MessageKind.CARD_ACTIVATION_REQUEST);
        this.yes = yes;
    }

    /**
     * Getter of yes
     * @return true if the client wants to activate the card
     */
    public boolean isYes() {return yes;}

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
