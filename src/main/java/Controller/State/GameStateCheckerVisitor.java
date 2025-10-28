package Controller.State;

import java.io.IOException;

/**
 * Visitor interface for the different states: methods return a boolean
 */
public interface GameStateCheckerVisitor {
	boolean visit(InitializationState state) throws IOException;
	boolean visit(AddAndRearrangeStocks state) throws IOException;
	boolean visit(CardDrawing state) throws IOException;
	boolean visit(ClaimRewardChoice state) throws IOException;
	boolean visit(DockingChoice state) throws IOException;
	boolean visit(EndGame state);
	boolean visit(FirePowerChoice state) throws IOException;
	boolean visit(GiveUpCrewChoice state) throws IOException;
	boolean visit(ManageProjectile state) throws IOException;
	boolean visit(MotorPowerChoice state) throws IOException;
	boolean visit(PLANETS_LandingChoice state) throws IOException;
	boolean visit(ShipConstructionState state) throws IOException;
	boolean visit(ToBeFixedAndFixingShips state) throws IOException;
	boolean visit(WaitingForPlayers state);
}
