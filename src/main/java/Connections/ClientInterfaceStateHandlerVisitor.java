package Connections;

import Controller.State.*;
import View.UI;

import java.util.concurrent.ExecutorService;

/**
 * Visitor class used in the client interface in order to handle the different states
 * @see ClientInterface
 */
public class ClientInterfaceStateHandlerVisitor implements GameStateVisitor {

	/**
	 * Current UI interface used
	 */
	private final UI ui;

	/**
	 * Current client interface
	 */
	private final ClientInterface clientInterface;

	/**
	 * Executor for sequential operations
	 */
	private final ExecutorService sequentialExecutor;

	/**
	 * Constructor of the class: it initializes all the attributes
	 * @param ui current UI interface
	 * @param clientInterface current client interface
	 * @param sequentialExecutor current sequential executor
	 */
	public ClientInterfaceStateHandlerVisitor(UI ui, ClientInterface clientInterface, ExecutorService sequentialExecutor) {
		this.ui=ui;
		this.clientInterface=clientInterface;
		this.sequentialExecutor=sequentialExecutor;
	}

	/**
	 * Visit for Initialization: no operation
	 * @param state Initialization state
	 */
	@Override
	public void visit(InitializationState state) {}

	/**
	 * Visit for add and rearrange stock: no operation
	 * @param state Add and rearrange stock state
	 */
	@Override
	public void visit(AddAndRearrangeStocks state) {}

	/**
	 * Visit for CardDrawing: it calls the method for visualization into the UI
	 * @param state CardDrawing state
	 */
	@Override
	public void visit(CardDrawing state) {
		sequentialExecutor.submit(() -> {
			try {
				ui.drawCard();
			} catch (Throwable t) {
				clientInterface.printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Visit for ClaimReward: it calls the method for visualization into the UI
	 * @param state ClaimReward state
	 */
	@Override
	public void visit(ClaimRewardChoice state) {
		sequentialExecutor.submit(() -> {
			try {
				ui.claimReward(clientInterface.getCurCard());
			} catch (Throwable t) {
				clientInterface.printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Visit for waiting for players: no operation
	 * @param state Waiting for players stock state
	 */
	@Override
	public void visit(WaitingForPlayers state) {}

	/**
	 * Visit for ToBeFixedAndFixingShips: it calls the method for visualization into the UI
	 * @param state ToBeFixedAndFixingShips state
	 */
	@Override
	public void visit(ToBeFixedAndFixingShips state) {
		sequentialExecutor.submit(() -> {
			try{
				ui.fixShip(clientInterface.ship);
			//	clientInterface.ship.connectLifeSupports();
				ui.connectLifeSupportsHireCrewInitializeAttributesAndSendShip(clientInterface.ship.getShip());
			//	clientInterface.ship.initializeShipAttributesFromComponents();

			/*	try {
					clientInterface.sendShip();
				} catch (IOException e) {
					throw new RuntimeException("Something went wrong while sending the finished ship to the server.");
				}*/
			}catch (Throwable t) {
				clientInterface.printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Visit for ShipsConstructionState: it calls the method for visualization into the UI
	 * @param state ShipsConstructionState state
	 */
	@Override
	public void visit(ShipConstructionState state) {
		sequentialExecutor.submit(() -> {
			try{
				ui.drawShipComponentOrSmallDeck();
			}catch (Throwable t) {
				clientInterface.printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Visit for PLANETS_LandingChoice: it calls the method for visualization into the UI
	 * @param state PLANETS_LandingChoice state
	 */
	@Override
	public void visit(PLANETS_LandingChoice state) {
		sequentialExecutor.submit(() -> {
			try{
				ui.planetsState();
			}catch (Throwable t) {
				clientInterface.printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Visit for MotorPowerChoice: it calls the method for visualization into the UI
	 * @param state MotorPowerChoice state
	 */
	@Override
	public void visit(MotorPowerChoice state) {
		sequentialExecutor.submit(() -> {
			try{
				ui.askDoubleMotor();
			}catch (Throwable t) {
				clientInterface.printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Visit for ManageProjectile: no operation
	 * @param state ManageProjectile state
	 */
	@Override
	public void visit(ManageProjectile state) {}

	/**
	 * Visit for GiveUpCrewChoice: it calls the method for visualization into the UI
	 * @param state GiveUpCrewChoice state
	 */
	@Override
	public void visit(GiveUpCrewChoice state) {
		sequentialExecutor.submit(() -> {
			try{
				ui.askGiveUpCrew(clientInterface.getCardCrewLoss(clientInterface.getCurCard()));
			}catch (Throwable t) {
				clientInterface.printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Visit for FirePowerChoice: it calls the method for visualization into the UI
	 * @param state FirePowerChoice state
	 */
	@Override
	public void visit(FirePowerChoice state) {
		sequentialExecutor.submit(() -> {
			try{
				ui.askDoubleCannon(clientInterface.ship.getShip());
			}catch (Throwable t) {
				clientInterface.printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Visit for EndGame: no operation
	 * @param state EndGame state
	 */
	@Override
	public void visit(EndGame state) {}

	/**
	 * Visit for DockingChoice: it calls the method for visualization into the UI
	 * @param state DockingChoice state
	 */
	@Override
	public void visit(DockingChoice state) {
		sequentialExecutor.submit(() -> {
			try{
				ui.askDocking(clientInterface.getCurCard());
			}catch (Throwable t) {
				clientInterface.printThreadUncheckedExceptions(t);
			}
		});
	}
}
