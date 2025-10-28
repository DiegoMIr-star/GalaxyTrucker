package Controller.State;

import java.io.IOException;

/**
 * Visitor interface for the different states
 */
public interface GameStateVisitor {
    void visit(InitializationState state) throws IOException;
    void visit(AddAndRearrangeStocks state) throws IOException;
    void visit(CardDrawing state) throws IOException;
    void visit(ClaimRewardChoice state) throws IOException;
    void visit(DockingChoice state) throws IOException;
    void visit(EndGame state);
    void visit(FirePowerChoice state) throws IOException;
    void visit(GiveUpCrewChoice state) throws IOException;
    void visit(ManageProjectile state) throws IOException;
    void visit(MotorPowerChoice state) throws IOException;
    void visit(PLANETS_LandingChoice state) throws IOException;
    void visit(ShipConstructionState state) throws IOException;
    void visit(ToBeFixedAndFixingShips state) throws IOException;
    void visit(WaitingForPlayers state) throws IOException;

}
