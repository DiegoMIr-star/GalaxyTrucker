package Connections.Messages;

import model.Deck;
import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the client with the current small deck
 */
public class SmallDeckResponseMessage extends Message implements Serializable {

    /**
     * Current small deck
     */
    private final Deck deck;
    private int deckIndex;

    /**
     * Constructor used to initialize the small deck
     * @param deck current small deck
     */
    public SmallDeckResponseMessage(Deck deck, int deckIndex) {
        super("Server", MessageKind.GET_SMALL_DECK);
        this.deck = deck;
        this.deckIndex = deckIndex;
    }

    /**
     * Getter of the deck
     * @return current deck
     */
    public Deck getDeck() {return deck;}

    /**
     * Getter of the deck index
     * @return current deck index
     */
    public int getDeckIndex() {return deckIndex;}

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
