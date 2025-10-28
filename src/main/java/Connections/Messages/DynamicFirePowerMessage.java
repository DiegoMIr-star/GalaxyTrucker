package Connections.Messages;

import model.DifferentShipComponents.ShipComponent;
import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the server in order to update the ship and the dynamic firepower
 */
public class DynamicFirePowerMessage extends Message implements Serializable {

    /**
     * Current dynamic firepower
     */
    private final double dynamicFirePower;

    /**
     * Current updated ship
     */
    private final ShipComponent[][] updatedShip;

    /**
     * Message constructor, it initializes all the attributes
     * @param nickname player nickname
     * @param dynamicFirePower current dynamic fire power
     * @param updatedShip current updated ship
     */
    public DynamicFirePowerMessage(String nickname, double dynamicFirePower, ShipComponent[][] updatedShip) {
        super(nickname, MessageKind.FIRE_POWER);
        this.dynamicFirePower = dynamicFirePower;
        this.updatedShip = updatedShip;
    }

    /**
     * Getter of the current dynamic firepower
     * @return current dynamic firepower
     */
    public double getDynamicFirePower() {return dynamicFirePower;}

    /**
     * Getter of the current updated ship
     * @return current updated ship
     */
    public ShipComponent[][] getUpdatedShip() {return updatedShip;}

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
