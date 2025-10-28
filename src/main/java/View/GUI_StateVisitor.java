package View;

import Controller.State.*;
import View.Controllers.StateController;

/**
 * Class used to handle visitor states, updating the GUI with messages linked with each state
 */
public class GUI_StateVisitor implements GameStateVisitor {

    /**
     * Attribute used to store the state controller {@link StateController}
     */
    private final StateController controller;

    /**
     * Constructor that links the visitor with the state controller
     * @param stateController handles the GUI updates
     */
    public GUI_StateVisitor(StateController stateController) {
        this.controller = stateController;
    }

    /**
     * Method used during the initialization phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(InitializationState state) {
        controller.setText("Initialization!");
    }

    /**
     * Method used during the add and rearrange stocks phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(AddAndRearrangeStocks state) {
        controller.setText("Add and rearrange stocks!");    }

    /**
     * Method used during the card drawing phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(CardDrawing state) {
        controller.setText("Card drawing!");
    }

    /**
     * Method used during the claim reward phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(ClaimRewardChoice state) {
        controller.setText("Claim reward choice!");
    }

    /**
     * Method used during the docking phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(DockingChoice state) {
        controller.setText("Docking choice!");
    }

    /**
     * Method used during the end game phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(EndGame state) {
        controller.setText("End game!");
    }

    /**
     * Method used during the firepower choice phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(FirePowerChoice state) {
        controller.setText("Fire power choice!");
    }

    /**
     * Method used during the give up crew phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(GiveUpCrewChoice state) {
        controller.setText("Give up crew choice!");
    }

    /**
     * Method used during the manage projectile phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(ManageProjectile state) {
        controller.setText("Manage projectile!");
    }

    /**
     * Method used during the motor power choice phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(MotorPowerChoice state) {
        controller.setText("Motor power choice!");
    }

    /**
     * Method used during the Planets landing choice phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(PLANETS_LandingChoice state) {
        controller.setText("Planets landing choice!");
    }

    /**
     * Method used during the ship construction phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(ShipConstructionState state) {
        controller.setText("Ships construction state!");    }

    /**
     * Method used during ship fixing phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(ToBeFixedAndFixingShips state) {
        controller.setText("To be fixed and fixing ships!");
    }

    /**
     * Method used during the waiting for players phase to display the corresponding message
     * @param state is the current state
     */
    @Override
    public void visit(WaitingForPlayers state) {
        controller.setText("Waiting for players!");
    }
}
