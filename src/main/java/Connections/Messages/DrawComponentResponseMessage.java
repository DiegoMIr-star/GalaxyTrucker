package Connections.Messages;

import model.DifferentShipComponents.ShipComponent;
import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the client in order to give a requested component
 */
public class DrawComponentResponseMessage extends Message implements Serializable {

    /**
     * Current ship component
     */
    private final ShipComponent component;

    /**
     * Message constructor used to initialize the attribute
     * @param component current component to give
     */
    public DrawComponentResponseMessage(ShipComponent component) {
        super("Server", MessageKind.DRAW_COMP_RESPONSE);
        this.component = component;
    }

    /**
     * Getter of the component
     * @return current component
     */
    public ShipComponent getComponent() {return component;}

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
