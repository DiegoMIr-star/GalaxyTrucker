package Connections.Messages;

import model.DifferentShipComponents.ShipComponent;
import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the server in order to return a component
 */
public class ReturnComponentRequestMessage extends Message implements Serializable {

    /**
     * Current component to return
     */
    ShipComponent componentToReturn;

    /**
     * Constructor of the message: it initializes the component to return
     * @param nickname player nickname
     * @param componentToLeave component to return
     */
    public ReturnComponentRequestMessage(String nickname, ShipComponent componentToLeave) {
        super(nickname, MessageKind.RETURN_COMP_REQUEST);
        this.componentToReturn = componentToLeave;
    }

    /**
     * Getter of the component to return
     * @return component to return
     */
    public ShipComponent getComponentToReturn() {return componentToReturn;}

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
