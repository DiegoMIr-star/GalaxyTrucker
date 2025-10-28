package Connections;

import Connections.Messages.*;

import java.io.IOException;

/**
 * Visitor class used in order to dispatch the different messages: it's used for socket and it calls
 * all different methods on client interface
 * @see ClientInterface
 */
public class ClientMessageHandlerVisitor implements MessageVisitor {

    /**
     * Current client interface
     */
    private final ClientInterface clientInterface;

    /**
     * Visitor constructor, used to initialize the current client interface
     * @param clientInterface current client interface
     */
    public ClientMessageHandlerVisitor(ClientInterface clientInterface) {this.clientInterface = clientInterface;}

    /**
     * Method used from the server to call directly the update small deck method in client interface
     * @see ClientInterface
     * @param message availableSmallDeckMessage
     */
    @Override
    public void visit(AvailableSmallDeckMessage message) {clientInterface.updateSmallDecksCall(message.getDecksIndexes());}

    /**
     * Method used from the server to call directly the beginning method in client interface
     * @see ClientInterface
     * @param message beginMessage
     */
    @Override
    public void visit(BeginMessage message) {clientInterface.beginCall(message.getPlayers());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message cardActivationRequestMessage
     */
    @Override
    public void visit(CardActivationRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the card drawn method in client interface
     * @see ClientInterface
     * @param message cardDrawnMessage
     */
    @Override
    public void visit(CardDrawnMessage message) {clientInterface.cardDrawnCall(message.getDrawnCard());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message claimRewardChoiceMessage
     */
    @Override
    public void visit(ClaimRewardChoiceMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the credits earned method in client interface
     * @see ClientInterface
     * @param message creditsEarnedMessage
     */
    @Override
    public void visit(CreditsEarnedMessage message) {clientInterface.creditsEarnedCall(message.getCreditsEarned());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message discardStocksMessage
     */
    @Override
    public void visit(DiscardStocksMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message drawCardRequestMessage
     */
    @Override
    public void visit(DrawCardRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message drawComponentRequestMessage
     */
    @Override
    public void visit(DrawComponentRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the draw component method in client interface
     * @see ClientInterface
     * @param message drawComponentResponseMessage
     */
    @Override
    public void visit(DrawComponentResponseMessage message) {clientInterface.drawCompnentCall(message.getComponent());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message drawSpecificComponentRequestMessage
     */
    @Override
    public void visit(DrawSpecificComponentRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message dynamicFirePowerMessage
     */
    @Override
    public void visit(DynamicFirePowerMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message dynamicMotorPowerMessage
     */
    @Override
    public void visit(DynamicMotorPowerMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message endGameMessage
     */
    @Override
    public void visit(EndGameMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the free planets method in client interface
     * @see ClientInterface
     * @param message freePlanetsResponseMessage
     */
    @Override
    public void visit(FreePlanetsResponseMessage message) {clientInterface.freePlanetsCall(message.getFreePlanetsIndexes());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message availableSmallDecksRequestMessage
     */
    @Override
    public void visit(AvailableSmallDecksRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the game state method in client interface
     * @see ClientInterface
     * @param message gameStateMessage
     */
    @Override
    public void visit(GameStateMessage message) {clientInterface.gameStateCall(message.getGameState());}

    /**
     * Method used from the server to call directly the generic method in client interface
     * @see ClientInterface
     * @param message genericMessage
     */
    @Override
    public void visit(GenericMessage message) {clientInterface.genericCall(message.getMessage());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message logRequestMessage
     */
    @Override
    public void visit(LogRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the log response method in client interface
     * @see ClientInterface
     * @param message logResponseMessage
     */
    @Override
    public void visit(LogResponseMessage message) {clientInterface.logResponseCall(message);}

    @Override
    public void visit(NotifyActionCompleted message) throws IOException {
        clientInterface.incorrectCall(message.getKind());
    }

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message pickRevealedComponentRequestMessage
     */
    @Override
    public void visit(PickRevealedComponentRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message placeShipRequestMessage
     */
    @Override
    public void visit(PlaceShipRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message planetLandRequestMessage
     */
    @Override
    public void visit(PlanetLandRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the players number method in client interface
     * @see ClientInterface
     * @param message playersNumRequestMessage
     */
    @Override
    public void visit(PlayersNumRequestMessage message) {clientInterface.playersNumCall();}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message playersNumResponseMessage
     */
    @Override
    public void visit(PlayersNumResponseMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message playersPositionsMessage
     */
    @Override
    public void visit(PlayersPositionsMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the position and turns method in client interface
     * @see ClientInterface
     * @param message positionsAndTurnsMessage
     */
    @Override
    public void visit(PositionsAndTurnsMessage message) {clientInterface.positionsAndTurnsCall(message);}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message premadeShipComponentRequest
     */
    @Override
    public void visit(PremadeShipComponentRequest message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the pre-made ship component method in client interface
     * @see ClientInterface
     * @param message premadeShipComponentResponse
     */
    @Override
    public void visit(PremadeShipComponentResponse message) throws IOException {clientInterface.premadeShipComponentCall(message.getComponent());}

    /**
     * Method used from the server to call directly the projectile trajectory method in client interface
     * @see ClientInterface
     * @param message projectileTrajectoryMessage
     */
    @Override
    public void visit(ProjectileTrajectoryMessage message) {clientInterface.projectileTrajectoryCall(message.getProjectile(), message.getTrajectory());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message returnComponentRequestMessage
     */
    @Override
    public void visit(ReturnComponentRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message returnSmallDeckMessage
     */
    @Override
    public void visit(ReturnSmallDeckMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the revealed component method in client interface
     * @see ClientInterface
     * @param message revealedComponentsMessage
     */
    @Override
    public void visit(RevealedComponentsMessage message) {clientInterface.revealedComponentsCall(message.getRevealedComponents());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message smallDeckRequestMessage
     */
    @Override
    public void visit(SmallDeckRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the small deck method in client interface
     * @see ClientInterface
     * @param message smallDeckResponseMessage
     */
    @Override
    public void visit(SmallDeckResponseMessage message) {clientInterface.smallDeckResponseCall(message.getDeck(), message.getDeckIndex());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message startTimerRequestMessage
     */
    @Override
    public void visit(StartTimerRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the stocks to add method in client interface
     * @see ClientInterface
     * @param message stocksToAddMessage
     */
    @Override
    public void visit(StocksToAddMessage message) {clientInterface.stocksToAddCall(message.getStocks());}

    /**
     * Method used from the server to call directly the timer expired method in client interface
     * @see ClientInterface
     * @param message timerExpiredMessage
     */
    @Override
    public void visit(TimerExpiredMessage message) {clientInterface.timerExpiredCall(message.isLast());}

    /**
     * Method used from the server to call directly the timer started method in client interface
     * @see ClientInterface
     * @param message timerStartedMessage
     */
    @Override
    public void visit(TimerStartedMessage message) {clientInterface.timerStartedCall(message.isLast());}

    /**
     * Method used from the server to call directly the turn method in client interface
     * @see ClientInterface
     * @param message turnMessage
     */
    @Override
    public void visit(TurnMessage message) {clientInterface.turnCall();}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message updateClientShipMessage
     */
    @Override
    public void visit(UpdateClientShipMessage message) {clientInterface.ship.updateShip(message.getUpdatedShip().getShip());}

    /**
     * Method used from the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message updateServerShipMessage
     */
    @Override
    public void visit(UpdateServerShipMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used from the server to call directly the waiting method in client interface
     * @see ClientInterface
     * @param message waitingPartnersMessage
     */
    @Override
    public void visit(WaitForOthersTurns message) {clientInterface.waitingTurnsCall();}

    /**
     * Method used from the server to call directly the waiting method in client interface
     * @see ClientInterface
     * @param message waitingPartnersMessage
     */
    @Override
    public void visit(WaitingPartnersMessage message) {clientInterface.waitingCall();}

    /**
     * Method used from the server to call directly the winners method in client interface
     * @see ClientInterface
     * @param message winnersMessage
     */
    @Override
    public void visit(WinnersMessage message) {clientInterface.winnersCall(message.getWinners());}

    /**
     * Method used from the server to call directly the client resilience method in client interface
     * @see ClientInterface
     * @param message resilienceRequestMessage
     */
    @Override
    public void visit(ResilienceRequestMessage message) throws IOException {clientInterface.clientResilienceCall();}

    /**
     * Method with no action: server RMI doesn't call for a resilience response, but only for requests
     * @param message resilienceResponseMessage
     */
    @Override
    public void visit(ResilienceResponseMessage message) {}

    /**
     * Method used from the server to call directly the resume method in client interface
     * @see ClientInterface
     * @param message resumeGameMessage
     */
    @Override
    public void visit(ResumeGameMessage message) throws IOException {clientInterface.resumeCall(message.getShip(),message.getCurCard(),message.getGameState(), message.getLastMessage());}
}
