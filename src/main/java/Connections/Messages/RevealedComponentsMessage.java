package Connections.Messages;

import model.DifferentShipComponents.ShipComponent;
import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message sent to the client in order to show the list of revealed components
 */
public class RevealedComponentsMessage extends Message implements Serializable {

    /**
     * List of revealed ship components
     */
    private final ArrayList<ShipComponent> revealedComponents;

    /**
     * Constructor of the message, it initializes the list of revealed ship components
     * @param revealedComponents list of revealed ship components
     */
    public RevealedComponentsMessage(ArrayList<ShipComponent> revealedComponents) {
        super("Server", MessageKind.UNCOVERED_COMPONENTS);
        this.revealedComponents = revealedComponents;
    }

    /**
     * Getter of revealed ship components
     * @return list of revealed ship components
     */
    public ArrayList<ShipComponent> getRevealedComponents() {return revealedComponents;}

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
