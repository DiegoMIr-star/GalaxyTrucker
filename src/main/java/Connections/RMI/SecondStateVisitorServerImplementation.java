package Connections.RMI;

import Connections.Messages.Message;
import Connections.Server;
import Controller.State.*;

import java.io.IOException;

/**
 * Visitor class, used to dispatch the state in the server implementation for RMI
 */
public class SecondStateVisitorServerImplementation implements GameStateVisitor {

    /**
     * Current server
     */
    private final Server server;

    /**
     * Current received message
     */
    private final Message message;

    /**
     * Class constructor, used to initialize the attributes
     * @param server current server
     * @param message current message
     */
    public SecondStateVisitorServerImplementation(Server server, Message message) {
        this.server = server;
        this.message = message;
    }

    /**
     * Visit methode, used to handle initialization state: it's empty
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(InitializationState state) throws IOException {}

    /**
     * Visit methode, used to handle addAndRearrangeStocks state: it's empty
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(AddAndRearrangeStocks state) throws IOException {}

    /**
     * Visit methode, used to handle cardDrawing state: it's empty
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(CardDrawing state) throws IOException {}

    /**
     * Visit methode, used to handle claimRewardChoice state: it's empty
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(ClaimRewardChoice state) throws IOException {}

    /**
     * Visit methode, used to handle dockingChoice state: it's empty
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(DockingChoice state) throws IOException {}

    /**
     * Visit methode, used to handle endGame state: it's empty
     * @param state current state
     */
    @Override
    public void visit(EndGame state) {}

    /**
     * Visit methode, used to handle firePowerChoice state: it's empty
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(FirePowerChoice state) throws IOException {}

    /**
     * Visit methode, used to handle giveUpCrewChoice state: it's empty
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(GiveUpCrewChoice state) throws IOException {}

    /**
     * Visit methode, used to handle manageProjectile state: it's empty
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(ManageProjectile state) throws IOException {}

    /**
     * Visit methode, used to handle motorPowerChoice state: it's empty
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(MotorPowerChoice state) throws IOException {}

    /**
     * Visit methode, used to handle PLANETS_LandingChoice state: it's empty
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(PLANETS_LandingChoice state) throws IOException {}

    /**
     * Visit methode, used to handle shipsConstructionState state: it handles the message with a direct
     * server call for RMI
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(ShipConstructionState state) throws IOException {server.handleMessageShipConstructionRMI(message);}

    /**
     * Visit methode, used to handle toBeFixedAndFixingShips state: it handles the message with a direct
     * server call for RMI
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(ToBeFixedAndFixingShips state) throws IOException {server.handleMessageToBeFixedRMI(message);}

    /**
     * Visit methode, used to handle waitingForPlayers state: it's empty
     * @param state current state
     */
    @Override
    public void visit(WaitingForPlayers state) {}
}
