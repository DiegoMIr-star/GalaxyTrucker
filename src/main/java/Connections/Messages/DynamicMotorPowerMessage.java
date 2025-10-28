package Connections.Messages;

import model.DifferentShipComponents.ShipComponent;
import model.NextGameStateAndMessages;

import java.io.IOException;
import java.io.Serializable;

/**
 * Message sent to the server in order to update the dynamic motor power and the updated ship
 */
public class DynamicMotorPowerMessage extends Message implements Serializable {

    /**
     * Current dynamic motor power
     */
    private final int dynMotorPower;

    /**
     * Current updated ship
     */
    private final ShipComponent[][] updatedShip;

    /**
     * Message constructor, it initializes all the attributes
     * @param nickname player nickname
     * @param dynMotorPower current dynamic motor power
     * @param updatedShip current updated ship
     */
    public DynamicMotorPowerMessage(String nickname, int dynMotorPower, ShipComponent[][] updatedShip) {
        super(nickname, MessageKind.MOTOR_POWER);
        this.dynMotorPower = dynMotorPower;
        this.updatedShip = updatedShip;
    }

    /**
     * Getter of dynamic motor power
     * @return current dynamic motor power
     */
    public int getDynMotorPower() {return dynMotorPower;}

    /**
     * Getter of the current updated ship
     * @return updated ship
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
