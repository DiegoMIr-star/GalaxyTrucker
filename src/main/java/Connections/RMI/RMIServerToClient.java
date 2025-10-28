package Connections.RMI;

import Connections.Messages.*;

import java.io.IOException;

/**
 * Visitor class used from the server to call specific client methods according to the type
 * of message for the RMI connection
 */
public class RMIServerToClient implements MessageVisitor {

	/**
	 * Current client
	 */
	private final RemoteClientRMI client;

	/**
	 * Visitor constructor used to initialize the attribute
	 * @param client current client
	 */
	public RMIServerToClient(RemoteClientRMI client) {this.client = client;}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current available small deck message
	 */
	@Override
	public void visit(AvailableSmallDeckMessage message) throws IOException {
			client.availableSmallDeckMessage(message);

	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current begin message
	 */
	@Override
	public void visit(BeginMessage message) throws IOException {
			client.beginMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current card activation request message
	 */
	@Override
	public void visit(CardActivationRequestMessage message) throws IOException {
			client.cardActivationRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current card drawn message
	 */
	@Override
	public void visit(CardDrawnMessage message) throws IOException {
			client.cardDrawnMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current claim reward choice message
	 */
	@Override
	public void visit(ClaimRewardChoiceMessage message) throws IOException {
			client.claimRewardChoiceMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current credits earned message
	 */
	@Override
	public void visit(CreditsEarnedMessage message) throws IOException {
			client.creditsEarnedMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current discard stocks message
	 */
	@Override
	public void visit(DiscardStocksMessage message) throws IOException {
			client.discardStocksMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current draw card request message
	 */
	@Override
	public void visit(DrawCardRequestMessage message) throws IOException {
			client.drawCardRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current draw component request message
	 */
	@Override
	public void visit(DrawComponentRequestMessage message) throws IOException {
			client.drawComponentRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current draw component response message
	 */
	@Override
	public void visit(DrawComponentResponseMessage message) throws IOException {
			client.drawComponentResponseMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current draw specific component request message
	 */
	@Override
	public void visit(DrawSpecificComponentRequestMessage message) throws IOException {
			client.drawSpecificComponentRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current dynamic firepower message
	 */
	@Override
	public void visit(DynamicFirePowerMessage message) throws IOException {
			client.dynamicFirePowerMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current dynamic motor power message
	 */
	@Override
	public void visit(DynamicMotorPowerMessage message) throws IOException {
			client.dynamicMotorPowerMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current end game message
	 */
	@Override
	public void visit(EndGameMessage message) throws IOException {
			client.endGameMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current free planets response message
	 */
	@Override
	public void visit(FreePlanetsResponseMessage message) throws IOException {
			client.freePlanetsResponseMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current available small decks request message
	 */
	@Override
	public void visit(AvailableSmallDecksRequestMessage message) throws IOException {
			client.freeSmallDecksRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current game state message
	 */
	@Override
	public void visit(GameStateMessage message) throws IOException {
			client.gameStateMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current generic message
	 */
	@Override
	public void visit(GenericMessage message) throws IOException {
			client.genericMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current log request message
	 */
	@Override
	public void visit(LogRequestMessage message) throws IOException {
			client.logRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current log response message
	 */
	@Override
	public void visit(LogResponseMessage message) throws IOException {
			client.logResponseMessage(message);
	}

	@Override
	public void visit(NotifyActionCompleted message) throws IOException {
		client.notifyActionCompleted(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current pick revealed component request message
	 */
	@Override
	public void visit(PickRevealedComponentRequestMessage message) throws IOException {
			client.pickRevealedComponentRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current place ship request message
	 */
	@Override
	public void visit(PlaceShipRequestMessage message) throws IOException {
			client.placeShipRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current planet land request message
	 */
	@Override
	public void visit(PlanetLandRequestMessage message) throws IOException {
			client.planetLandRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current players number request message
	 */
	@Override
	public void visit(PlayersNumRequestMessage message) throws IOException {
			client.playersNumRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current players number response message
	 */
	@Override
	public void visit(PlayersNumResponseMessage message) throws IOException {
			client.playersNumResponseMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current players positions message
	 */
	@Override
	public void visit(PlayersPositionsMessage message) throws IOException {
			client.playersPositionMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current positions and turns message
	 */
	@Override
	public void visit(PositionsAndTurnsMessage message) throws IOException {
			client.positionsAndTurnsMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current pre-made ship components request message
	 */
	@Override
	public void visit(PremadeShipComponentRequest message) throws IOException {
			client.premadeShipComponentRequest(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current pre-made ship component response message
	 */
	@Override
	public void visit(PremadeShipComponentResponse message) throws IOException {
			client.premadeShipComponentResponse(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current projectile trajectory message
	 */
	@Override
	public void visit(ProjectileTrajectoryMessage message) throws IOException {
			client.projectileTrajectoryMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current return component request message
	 */
	@Override
	public void visit(ReturnComponentRequestMessage message) throws IOException {
			client.returnComponentRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current return small deck message
	 */
	@Override
	public void visit(ReturnSmallDeckMessage message) throws IOException {
			client.returnSmallDeckMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current revealed components message
	 */
	@Override
	public void visit(RevealedComponentsMessage message) throws IOException {
			client.revealedComponentsMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current small deck request message
	 */
	@Override
	public void visit(SmallDeckRequestMessage message) throws IOException {
			client.smallDeckRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current small deck response message
	 */
	@Override
	public void visit(SmallDeckResponseMessage message) throws IOException {
			client.smallDeckResponseMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current start timer request message
	 */
	@Override
	public void visit(StartTimerRequestMessage message) throws IOException {
			client.startTimerRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current stocks to add message
	 */
	@Override
	public void visit(StocksToAddMessage message) throws IOException {
			client.stocksToAddMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current timer expired message
	 */
	@Override
	public void visit(TimerExpiredMessage message) throws IOException {
			client.timerExpiredMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current timer started message
	 */
	@Override
	public void visit(TimerStartedMessage message) throws IOException {
			client.timerStartedMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current turn message
	 */
	@Override
	public void visit(TurnMessage message) throws IOException {
			client.turnMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current update client ship message
	 */
	@Override
	public void visit(UpdateClientShipMessage message) throws IOException {
			client.updateClientShipMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current update server ship message
	 */
	@Override
	public void visit(UpdateServerShipMessage message) throws IOException {
			client.updateServerShipMessage(message);
	}

	@Override
	public void visit(WaitForOthersTurns message) throws IOException {
		client.waitForOthersTurns(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current waiting partners message
	 */
	@Override
	public void visit(WaitingPartnersMessage message) throws IOException {
			client.waitingPartnersMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current winners message
	 */
	@Override
	public void visit(WinnersMessage message) throws IOException {
			client.winnersMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current resilience request message
	 */
	@Override
	public void visit(ResilienceRequestMessage message) throws IOException {
		client.resilienceRequestMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current resilience response message
	 */
	@Override
	public void visit(ResilienceResponseMessage message) throws IOException {
			client.resilienceResponseMessage(message);
	}

	/**
	 * Method used to expose the related client method according to the message
	 * @param message current resume game message
	 */
	@Override
	public void visit(ResumeGameMessage message) throws IOException {
		client.resumeGame(message);
	}

	/**
	 * Method used to print the current IO exception
	 * @param e current IO exception
	 */
	private void printProblem(IOException e){e.printStackTrace();}
}
