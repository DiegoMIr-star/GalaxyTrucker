package Connections.RMI;

import Connections.Messages.*;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote client RMI interface used to implement different calls according to the message kind
 */
public interface RemoteClientRMI extends Remote {
    ServerRMI getRemoteReference() throws RemoteException;
    void availableSmallDeckMessage(AvailableSmallDeckMessage message) throws IOException;
    void beginMessage(BeginMessage message) throws IOException;
    void cardActivationRequestMessage(CardActivationRequestMessage message) throws IOException;
    void cardDrawnMessage(CardDrawnMessage message) throws IOException;
    void claimRewardChoiceMessage(ClaimRewardChoiceMessage message) throws IOException;
    void creditsEarnedMessage(CreditsEarnedMessage message) throws IOException;
    void discardStocksMessage(DiscardStocksMessage message) throws IOException;
    void drawCardRequestMessage(DrawCardRequestMessage message) throws IOException;
    void drawComponentRequestMessage(DrawComponentRequestMessage message) throws IOException;
    void drawComponentResponseMessage(DrawComponentResponseMessage message) throws IOException;
    void drawSpecificComponentRequestMessage(DrawSpecificComponentRequestMessage message) throws IOException;
    void dynamicFirePowerMessage(DynamicFirePowerMessage message) throws IOException;
    void dynamicMotorPowerMessage(DynamicMotorPowerMessage message) throws IOException;
    void endGameMessage(EndGameMessage message) throws IOException;
    void freePlanetsResponseMessage(FreePlanetsResponseMessage message) throws IOException;
    void freeSmallDecksRequestMessage(AvailableSmallDecksRequestMessage message) throws IOException;
    void gameStateMessage(GameStateMessage message) throws IOException;
    void genericMessage(GenericMessage message) throws IOException;
    void logRequestMessage(LogRequestMessage message) throws IOException;
    void logResponseMessage(LogResponseMessage message) throws IOException;
    void notifyActionCompleted(NotifyActionCompleted message) throws IOException;
    void pickRevealedComponentRequestMessage(PickRevealedComponentRequestMessage message) throws IOException;
    void placeShipRequestMessage(PlaceShipRequestMessage message) throws IOException;
    void planetLandRequestMessage(PlanetLandRequestMessage message) throws IOException;
    void playersNumRequestMessage(PlayersNumRequestMessage message) throws IOException;
    void playersNumResponseMessage(PlayersNumResponseMessage message) throws IOException;
    void playersPositionMessage(PlayersPositionsMessage message) throws IOException;
    void positionsAndTurnsMessage(PositionsAndTurnsMessage message) throws IOException;
    void premadeShipComponentRequest(PremadeShipComponentRequest message) throws IOException;
    void premadeShipComponentResponse(PremadeShipComponentResponse message) throws IOException;
    void projectileTrajectoryMessage(ProjectileTrajectoryMessage message) throws IOException;
    void returnComponentRequestMessage(ReturnComponentRequestMessage message) throws IOException;
    void returnSmallDeckMessage(ReturnSmallDeckMessage message) throws IOException;
    void revealedComponentsMessage(RevealedComponentsMessage message) throws IOException;
    void smallDeckRequestMessage(SmallDeckRequestMessage message) throws IOException;
    void smallDeckResponseMessage(SmallDeckResponseMessage message) throws IOException;
    void startTimerRequestMessage(StartTimerRequestMessage message) throws IOException;
    void stocksToAddMessage(StocksToAddMessage message) throws IOException;
    void timerExpiredMessage(TimerExpiredMessage message) throws IOException;
    void timerStartedMessage(TimerStartedMessage message) throws IOException;
    void turnMessage(TurnMessage message) throws IOException;
    void updateClientShipMessage(UpdateClientShipMessage message) throws IOException;
    void waitForOthersTurns(WaitForOthersTurns message) throws IOException;
    void waitingPartnersMessage(WaitingPartnersMessage message) throws IOException;
    void updateServerShipMessage(UpdateServerShipMessage message) throws IOException;
    void winnersMessage(WinnersMessage message) throws IOException;
    void resilienceRequestMessage(ResilienceRequestMessage message) throws IOException;
    void resilienceResponseMessage(ResilienceResponseMessage message) throws IOException;
    void resumeGame(ResumeGameMessage message) throws IOException;
}
