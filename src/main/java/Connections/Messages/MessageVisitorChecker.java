package Connections.Messages;

/**
 * Visitor interface used to dispatch the different messages and return a next game state
 * and messages object, which contains the next game and the messages
 */
public interface MessageVisitorChecker {
	boolean visit(AvailableSmallDeckMessage message);
	boolean visit(BeginMessage message);
	boolean visit(CardActivationRequestMessage message);
	boolean visit(CardDrawnMessage message);
	boolean visit(ClaimRewardChoiceMessage message);
	boolean visit(CreditsEarnedMessage message);
	boolean visit(DiscardStocksMessage message);
	boolean visit(DrawCardRequestMessage message);
	boolean visit(DrawComponentRequestMessage message);
	boolean visit(DrawComponentResponseMessage message);
	boolean visit(DrawSpecificComponentRequestMessage message);
	boolean visit(DynamicFirePowerMessage message);
	boolean visit(DynamicMotorPowerMessage message);
	boolean visit(EndGameMessage message);
	boolean visit(FreePlanetsResponseMessage message);
	boolean visit(AvailableSmallDecksRequestMessage message);
	boolean visit(GameStateMessage message);
	boolean visit(GenericMessage message);
	boolean visit(LogRequestMessage message);
	boolean visit(LogResponseMessage message);
	boolean visit(PickRevealedComponentRequestMessage message);
	boolean visit(PlaceShipRequestMessage message);
	boolean visit(PlanetLandRequestMessage message);
	boolean visit(PlayersNumRequestMessage message);
	boolean visit(PlayersNumResponseMessage message);
	boolean visit(PlayersPositionsMessage message);
	boolean visit(PositionsAndTurnsMessage message);
	boolean visit(PremadeShipComponentRequest message);
	boolean visit(PremadeShipComponentResponse message);
	boolean visit(ProjectileTrajectoryMessage message);
	boolean visit(ReturnComponentRequestMessage message);
	boolean visit(ReturnSmallDeckMessage message);
	boolean visit(RevealedComponentsMessage message);
	boolean visit(SmallDeckRequestMessage message);
	boolean visit(SmallDeckResponseMessage message);
	boolean visit(StartTimerRequestMessage message);
	boolean visit(StocksToAddMessage message);
	boolean visit(TimerExpiredMessage message);
	boolean visit(TimerStartedMessage message);
	boolean visit(TurnMessage message);
	boolean visit(UpdateClientShipMessage message);
	boolean visit(UpdateServerShipMessage message);
	boolean visit(WaitingPartnersMessage message);
	boolean visit(WinnersMessage message);
}
