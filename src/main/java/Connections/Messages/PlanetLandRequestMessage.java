package Connections.Messages;

import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the server in order to land on a specific planet
 */
public class PlanetLandRequestMessage extends Message implements Serializable {

    /**
     * Planet ID
     */
    private final int planetId;

    /**
     * Constructor of the message: it initializes the attribute
     * @param nickname players nickname
     * @param planetId current planet ID
     */
    public PlanetLandRequestMessage(String nickname, int planetId) {
        super(nickname, MessageKind.PLANET_LAND_REQUEST);
        this.planetId = planetId;
    }

    /**
     * Getter of the planet ID
     * @return current planet ID
     */
    public int getPlanetId() {return planetId;}

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
