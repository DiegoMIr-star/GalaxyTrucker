package Controller;

import Connections.Messages.Message;
import Controller.State.*;

import java.io.IOException;

/**
 * Visitor class used to dispatch the controller calls according to the current state for socket,
 * RMI calls directly the controller methods without coming into this class
 */
public class ControllerStateVisitor implements GameStateVisitor{

    /**
     * Current controller
     */
    Controller controller;

    /**
     * Current message
     */
    Message message;

    /**
     * Class constructor, it initializes the attributes
     * @param controller current controller
     * @param message current message
     */
    public ControllerStateVisitor(Controller controller, Message message) {
        this.controller = controller;
        this.message = message;
    }

    /**
     * It calls directly the handle initialization in the controller
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(InitializationState state) throws IOException {controller.handleInitializationState(message,false);}

    /**
     * It calls directly the handle add and rearrange stocks in the controller
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(AddAndRearrangeStocks state) throws IOException {controller.handleAddAndRearrangeStocks(message,false);}

    /**
     * It calls directly the handle card drawing in the controller
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(CardDrawing state) throws IOException {controller.handleCardDrawing(message,false);}

    /**
     * It calls directly the handle claim reward choice in the controller
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(ClaimRewardChoice state) throws IOException {controller.handleClaimRewardChoice(message,false);}

    /**
     * It calls directly the handle docking choice in the controller
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(DockingChoice state) throws IOException {controller.handleDockingChoice(message,false);}

    /**
     * It calls directly the handle end game in the controller
     * @param state current state
     */
    @Override
    public void visit(EndGame state) {controller.handleEndGame(message,false);}

    /**
     * It calls directly the handle firepower choice in the controller
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(FirePowerChoice state) throws IOException {controller.handleFirePowerChoice(message,false);}

    /**
     * It calls directly the handle give up crew choice in the controller
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(GiveUpCrewChoice state) throws IOException {controller.handleGiveUpCrewChoice(message,false);}

    /**
     * It calls directly the handle manage projectile in the controller
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(ManageProjectile state) throws IOException {controller.handleManageProjectile(message,false);}

    /**
     * It calls directly the handle motor power choice in the controller
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(MotorPowerChoice state) throws IOException {
        controller.handleMotorPowerChoice(message,false);
    }

    /**
     * It calls directly the handle planets landing choice in the controller
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(PLANETS_LandingChoice state) throws IOException {controller.handlePLANETS_LandingChoice(message,false);}

    /**
     * It calls directly the handle ship construction in the controller
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(ShipConstructionState state) throws IOException {controller.handleShipConstructionState(message,false);}

    /**
     * It calls directly the handle to be fixed and fixing ship in the controller
     * @param state current state
     * @throws IOException input output exception thrown
     */
    @Override
    public void visit(ToBeFixedAndFixingShips state) throws IOException {
        controller.setBeginning(false);
        controller.handleToBeFixedAndFixingShips(message,false);
    }

    /**
     * Waiting for players is handled in the controller class and it doesn't need to be dispatched
     * @param state current state
     */
    @Override
    public void visit(WaitingForPlayers state) throws IOException {
        controller.handlePlayerNumChoiceStolen(message);
    }
}
