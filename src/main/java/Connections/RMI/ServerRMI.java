package Connections.RMI;

import Connections.Messages.*;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote server RMI interface used to implement different calls according to the message kind
 */
public interface ServerRMI extends Remote {
    void addClient(RemoteClientRMI client, String nickname) throws IOException;
    void availableSmallDeckMessage(AvailableSmallDeckMessage message) throws RemoteException;
    void beginMessage(BeginMessage message) throws RemoteException;
    void cardActivationRequestMessage(CardActivationRequestMessage message) throws RemoteException;
    void cardDrawnMessage(CardDrawnMessage message) throws RemoteException;
    void claimRewardChoiceMessage(ClaimRewardChoiceMessage message) throws RemoteException;
    void creditsEarnedMessage(CreditsEarnedMessage message) throws RemoteException;
    void discardStocksMessage(DiscardStocksMessage message) throws RemoteException;
    void drawCardRequestMessage(DrawCardRequestMessage message) throws RemoteException;
    void drawComponentRequestMessage(DrawComponentRequestMessage message) throws RemoteException;
    void drawComponentResponseMessage(DrawComponentResponseMessage message) throws RemoteException;
    void drawSpecificComponentRequestMessage(DrawSpecificComponentRequestMessage message) throws RemoteException;
    void dynamicFirePowerMessage(DynamicFirePowerMessage message) throws RemoteException;
    void dynamicMotorPowerMessage(DynamicMotorPowerMessage message) throws RemoteException;
    void endGameMessage(EndGameMessage message) throws RemoteException;
    void freePlanetsResponseMessage(FreePlanetsResponseMessage message) throws RemoteException;
    void freeSmallDecksRequestMessage(AvailableSmallDecksRequestMessage message) throws RemoteException;
    void gameStateMessage(GameStateMessage message) throws RemoteException;
    void genericMessage(GenericMessage message) throws RemoteException;
    void logRequestMessage(LogRequestMessage message) throws RemoteException;
    void logResponseMessage(LogResponseMessage message) throws RemoteException;
    void notifyActionCompleted(NotifyActionCompleted message) throws RemoteException;
    void pickRevealedComponentRequestMessage(PickRevealedComponentRequestMessage message) throws RemoteException;
    void placeShipRequestMessage(PlaceShipRequestMessage message) throws RemoteException;
    void planetLandRequestMessage(PlanetLandRequestMessage message) throws RemoteException;
    void playersNumRequestMessage(PlayersNumRequestMessage message) throws RemoteException;
    void playersNumResponseMessage(PlayersNumResponseMessage message) throws RemoteException;
    void playersPositionMessage(PlayersPositionsMessage message) throws RemoteException;
    void positionsAndTurnsMessage(PositionsAndTurnsMessage message) throws RemoteException;
    void premadeShipComponentRequest(PremadeShipComponentRequest message) throws RemoteException;
    void premadeShipComponentResponse(PremadeShipComponentResponse message) throws RemoteException;
    void projectileTrajectoryMessage(ProjectileTrajectoryMessage message) throws RemoteException;
    void returnComponentRequestMessage(ReturnComponentRequestMessage message) throws RemoteException;
    void returnSmallDeckMessage(ReturnSmallDeckMessage message) throws RemoteException;
    void revealedComponentsMessage(RevealedComponentsMessage message) throws RemoteException;
    void smallDeckRequestMessage(SmallDeckRequestMessage message) throws RemoteException;
    void smallDeckResponseMessage(SmallDeckResponseMessage message) throws RemoteException;
    void startTimerRequestMessage(StartTimerRequestMessage message) throws RemoteException;
    void stocksToAddMessage(StocksToAddMessage message) throws RemoteException;
    void timerExpiredMessage(TimerExpiredMessage message) throws RemoteException;
    void timerStartedMessage(TimerStartedMessage message) throws RemoteException;
    void turnMessage(TurnMessage message) throws RemoteException;
    void updateClientShipMessage(UpdateClientShipMessage message) throws RemoteException;
    void waitForOthersTurns(WaitForOthersTurns message) throws RemoteException;
    void waitingPartnersMessage(WaitingPartnersMessage message) throws RemoteException;
    void updateServerShipMessage(UpdateServerShipMessage message) throws RemoteException;
    void winnersMessage(WinnersMessage message) throws RemoteException;
    void resilienceRequestMessage(ResilienceRequestMessage message) throws RemoteException;
    void resilienceResponseMessage(ResilienceResponseMessage message) throws RemoteException;
    void resumeGame(ResumeGameMessage message) throws RemoteException;
}
