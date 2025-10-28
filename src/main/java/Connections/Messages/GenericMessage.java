package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;

/**
 * Message sent to the client with a generic message
 */
public class GenericMessage extends Message{

    /**
     * Generic message
     */
    private final String message;

    /**
     * Message constructor: it initializes the generic message
     * @param message generic message
     */
    public GenericMessage(String message){
        super("Server",MessageKind.GENERIC);
        this.message=message;
    }

    /**
     * Getter of the message
     * @return generic message
     */
    public String getMessage(){return message;}

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
