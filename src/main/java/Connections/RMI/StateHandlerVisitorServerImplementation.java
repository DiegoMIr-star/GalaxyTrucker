package Connections.RMI;

import Connections.Messages.Message;
import Connections.Server;
import Controller.State.*;

import java.io.IOException;

/**
 * Visitor class used to handle the different states for RMI, in order to call directly the
 * controller methods according to the current state
 */
public class StateHandlerVisitorServerImplementation implements GameStateVisitor {

    /**
     * Current server
     */
    private final Server server;

    /**
     * Current message
     */
    private final Message message;

    /**
     * Class constructor, used to initialize the attributes
     * @param server current server
     * @param message current message
     */
    public StateHandlerVisitorServerImplementation(Server server, Message message) {
        this.server = server;
        this.message = message;
    }

    /**
     * Visit method used to handle the initialization state: it's empty
     * @param state initialization state
     * @throws IOException input output exception
     */
    @Override
    public void visit(InitializationState state) throws IOException {}

    /**
     * Visit methode to call the handle message add and rearrange stocks for RMI
     * @param state current state
     * @throws IOException input output exception
     */
    @Override
    public void visit(AddAndRearrangeStocks state) throws IOException {server.handleMessageAddAndRearrangeStocksRMI(message);}

    /**
     * Visit method used to handle the cardDrawing state: it's empty
     * @param state cardDrawing state
     * @throws IOException input output exception
     */
    @Override
    public void visit(CardDrawing state) throws IOException {}

    /**
     * Visit method used to handle the claimRewardChoice state: it's empty
     * @param state claimRewardChoice state
     * @throws IOException input output exception
     */
    @Override
    public void visit(ClaimRewardChoice state) throws IOException {}

    /**
     * Visit method used to handle the dockingChoice state: it's empty
     * @param state dockingChoice state
     * @throws IOException input output exception
     */
    @Override
    public void visit(DockingChoice state) throws IOException {}

    /**
     * Visit method used to handle the endGame state: it's empty
     * @param state endGame state
     */
    @Override
    public void visit(EndGame state) {}

    /**
     * Visit methode to call the handle message firepower for RMI
     * @param state current state
     * @throws IOException input output exception
     */
    @Override
    public void visit(FirePowerChoice state) throws IOException {server.handleMessageFirePowerRMI(message);}

    /**
     * Visit methode to call the handle message give up crew for RMI
     * @param state current state
     * @throws IOException input output exception
     */
    @Override
    public void visit(GiveUpCrewChoice state) throws IOException {server.handleMessageGiveUpCrewRMI(message);}

    /**
     * Visit methode to call the handle message manage projectile for RMI
     * @param state current state
     * @throws IOException input output exception
     */
    @Override
    public void visit(ManageProjectile state) throws IOException {server.handleMessageManageProjectileRMI(message);}

    /**
     * Visit methode to call the handle message motor power for RMI
     * @param state current state
     * @throws IOException input output exception
     */
    @Override
    public void visit(MotorPowerChoice state) throws IOException {server.handleMessageMotorPowerRMI(message);}

    /**
     * Visit method used to handle the PLANETS_LandingChoice state: it's empty
     * @param state PLANETS_LandingChoice state
     * @throws IOException input output exception
     */
    @Override
    public void visit(PLANETS_LandingChoice state) throws IOException {}

    /**
     * Visit method used to handle the shipsConstructionState: it's empty
     * @param state shipsConstructionState
     * @throws IOException input output exception
     */
    @Override
    public void visit(ShipConstructionState state) throws IOException {}

    /**
     * Visit method used to handle the toBeFixedAndFixingShips state: it's empty
     * @param state toBeFixedAndFixingShips state
     * @throws IOException input output exception
     */
    @Override
    public void visit(ToBeFixedAndFixingShips state) throws IOException {}

    /**
     * Visit method used to handle the waitingForPlayers state: it's empty
     * @param state waitingForPlayers state
     */
    @Override
    public void visit(WaitingForPlayers state) {}
}
