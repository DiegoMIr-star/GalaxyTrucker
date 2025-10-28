package Connections.Messages;

import java.io.IOException;

/**
 * Visitor interface used to dispatch the messages
 */
public interface MessageVisitor {
    void visit(AvailableSmallDeckMessage message) throws IOException ;
    void visit(BeginMessage message) throws IOException ;
    void visit(CardActivationRequestMessage message) throws IOException ;
    void visit(CardDrawnMessage message) throws IOException ;
    void visit(ClaimRewardChoiceMessage message) throws IOException ;
    void visit(CreditsEarnedMessage message) throws IOException ;
    void visit(DiscardStocksMessage message) throws IOException ;
    void visit(DrawCardRequestMessage message) throws IOException ;
    void visit(DrawComponentRequestMessage message) throws IOException ;
    void visit(DrawComponentResponseMessage message) throws IOException ;
    void visit(DrawSpecificComponentRequestMessage message) throws IOException ;
    void visit(DynamicFirePowerMessage message) throws IOException ;
    void visit(DynamicMotorPowerMessage message) throws IOException ;
    void visit(EndGameMessage message) throws IOException ;
    void visit(FreePlanetsResponseMessage message) throws IOException ;
    void visit(AvailableSmallDecksRequestMessage message) throws IOException ;
    void visit(GameStateMessage message) throws IOException ;
    void visit(GenericMessage message) throws IOException ;
    void visit(LogRequestMessage message) throws IOException ;
    void visit(LogResponseMessage message) throws IOException ;
    void visit(NotifyActionCompleted message) throws IOException ;
    void visit(PickRevealedComponentRequestMessage message) throws IOException ;
    void visit(PlaceShipRequestMessage message) throws IOException ;
    void visit(PlanetLandRequestMessage message) throws IOException ;
    void visit(PlayersNumRequestMessage message) throws IOException ;
    void visit(PlayersNumResponseMessage message) throws IOException ;
    void visit(PlayersPositionsMessage message) throws IOException ;
    void visit(PositionsAndTurnsMessage message) throws IOException ;
    void visit(PremadeShipComponentRequest message) throws IOException ;
    void visit(PremadeShipComponentResponse message) throws IOException;
    void visit(ProjectileTrajectoryMessage message) throws IOException ;
    void visit(ReturnComponentRequestMessage message) throws IOException ;
    void visit(ReturnSmallDeckMessage message) throws IOException ;
    void visit(RevealedComponentsMessage message) throws IOException ;
    void visit(SmallDeckRequestMessage message) throws IOException ;
    void visit(SmallDeckResponseMessage message) throws IOException ;
    void visit(StartTimerRequestMessage message) throws IOException ;
    void visit(StocksToAddMessage message) throws IOException ;
    void visit(TimerExpiredMessage message) throws IOException ;
    void visit(TimerStartedMessage message) throws IOException ;
    void visit(TurnMessage message) throws IOException ;
    void visit(UpdateClientShipMessage message) throws IOException ;
    void visit(UpdateServerShipMessage message) throws IOException ;
    void visit(WaitForOthersTurns message) throws IOException;
    void visit(WaitingPartnersMessage message) throws IOException ;
    void visit(WinnersMessage message) throws IOException ;
    void visit(ResilienceRequestMessage message) throws IOException;
    void visit(ResilienceResponseMessage message) throws IOException ;
    void visit(ResumeGameMessage message) throws IOException;
}
