package Connections.Messages;

import model.NextGameStateAndMessages;

/**
 * Visitor interface used to dispatch the different messages and return a next game state
 * and messages object, which contains the next game and the messages
 */
public interface MessageVisitorProgresser {
	NextGameStateAndMessages visit(AvailableSmallDeckMessage message);
	NextGameStateAndMessages visit(BeginMessage message);
	NextGameStateAndMessages visit(CardActivationRequestMessage message);
	NextGameStateAndMessages visit(CardDrawnMessage message);
	NextGameStateAndMessages visit(ClaimRewardChoiceMessage message);
	NextGameStateAndMessages visit(CreditsEarnedMessage message);
	NextGameStateAndMessages visit(DiscardStocksMessage message);
	NextGameStateAndMessages visit(DrawCardRequestMessage message);
	NextGameStateAndMessages visit(DrawComponentRequestMessage message);
	NextGameStateAndMessages visit(DrawComponentResponseMessage message);
	NextGameStateAndMessages visit(DrawSpecificComponentRequestMessage message);
	NextGameStateAndMessages visit(DynamicFirePowerMessage message);
	NextGameStateAndMessages visit(DynamicMotorPowerMessage message);
	NextGameStateAndMessages visit(EndGameMessage message);
	NextGameStateAndMessages visit(FreePlanetsResponseMessage message);
	NextGameStateAndMessages visit(AvailableSmallDecksRequestMessage message);
	NextGameStateAndMessages visit(GameStateMessage message);
	NextGameStateAndMessages visit(GenericMessage message);
	NextGameStateAndMessages visit(LogRequestMessage message);
	NextGameStateAndMessages visit(LogResponseMessage message);
	NextGameStateAndMessages visit(PickRevealedComponentRequestMessage message);
	NextGameStateAndMessages visit(PlaceShipRequestMessage message);
	NextGameStateAndMessages visit(PlanetLandRequestMessage message);
	NextGameStateAndMessages visit(PlayersNumRequestMessage message);
	NextGameStateAndMessages visit(PlayersNumResponseMessage message);
	NextGameStateAndMessages visit(PlayersPositionsMessage message);
	NextGameStateAndMessages visit(PositionsAndTurnsMessage message);
	NextGameStateAndMessages visit(PremadeShipComponentRequest message);
	NextGameStateAndMessages visit(PremadeShipComponentResponse message);
	NextGameStateAndMessages visit(ProjectileTrajectoryMessage message);
	NextGameStateAndMessages visit(ReturnComponentRequestMessage message);
	NextGameStateAndMessages visit(ReturnSmallDeckMessage message);
	NextGameStateAndMessages visit(RevealedComponentsMessage message);
	NextGameStateAndMessages visit(SmallDeckRequestMessage message);
	NextGameStateAndMessages visit(SmallDeckResponseMessage message);
	NextGameStateAndMessages visit(StartTimerRequestMessage message);
	NextGameStateAndMessages visit(StocksToAddMessage message);
	NextGameStateAndMessages visit(TimerExpiredMessage message);
	NextGameStateAndMessages visit(TimerStartedMessage message);
	NextGameStateAndMessages visit(TurnMessage message);
	NextGameStateAndMessages visit(UpdateClientShipMessage message);
	NextGameStateAndMessages visit(UpdateServerShipMessage message);
	NextGameStateAndMessages visit(WaitingPartnersMessage message);
	NextGameStateAndMessages visit(WinnersMessage message);
}
