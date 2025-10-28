package Connections.RMI;

import Connections.Messages.*;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Visitor class used from the client to call specific server methods according to the type
 * of message for the RMI connection
 */
public class RMIMessageSender implements MessageVisitor {

    /**
     * Current client
     */
    private final RemoteClientRMI client;

    /**
     * Visitor constructor, used to initialize the attribute
     * @param client current client
     */
    public RMIMessageSender(RemoteClientRMI client) {this.client = client;}

    /**
     * Method used to expose the related server method according to the message
     * @param message current available small deck message
     */
    @Override
    public void visit(AvailableSmallDeckMessage message) {
        try{
            client.getRemoteReference().availableSmallDeckMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current begin message
     */
    @Override
    public void visit(BeginMessage message) {
        try{
            client.getRemoteReference().beginMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current card activation request message
     */
    @Override
    public void visit(CardActivationRequestMessage message) {
        try{
            client.getRemoteReference().cardActivationRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current card drawn message
     */
    @Override
    public void visit(CardDrawnMessage message) {
        try{
            client.getRemoteReference().cardDrawnMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current claim reward choice message
     */
    @Override
    public void visit(ClaimRewardChoiceMessage message) {
        try{
            client.getRemoteReference().claimRewardChoiceMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current credits earned message
     */
    @Override
    public void visit(CreditsEarnedMessage message) {
        try{
            client.getRemoteReference().creditsEarnedMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current discard stocks message
     */
    @Override
    public void visit(DiscardStocksMessage message) {
        try{
            client.getRemoteReference().discardStocksMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current draw card request message
     */
    @Override
    public void visit(DrawCardRequestMessage message) {
        try{
            client.getRemoteReference().drawCardRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current draw component request message
     */
    @Override
    public void visit(DrawComponentRequestMessage message) {
        try{
            client.getRemoteReference().drawComponentRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current draw component response message
     */
    @Override
    public void visit(DrawComponentResponseMessage message) {
        try{
            client.getRemoteReference().drawComponentResponseMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current draw specific component request message
     */
    @Override
    public void visit(DrawSpecificComponentRequestMessage message) {
        try{
            client.getRemoteReference().drawSpecificComponentRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current dynamic firepower message
     */
    @Override
    public void visit(DynamicFirePowerMessage message) {
        try{
            client.getRemoteReference().dynamicFirePowerMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current dynamic motor power message
     */
    @Override
    public void visit(DynamicMotorPowerMessage message) {
        try{
            client.getRemoteReference().dynamicMotorPowerMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current end game message
     */
    @Override
    public void visit(EndGameMessage message) {
        try{
            client.getRemoteReference().endGameMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current free planet response message
     */
    @Override
    public void visit(FreePlanetsResponseMessage message) {
        try{
            client.getRemoteReference().freePlanetsResponseMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current available small decks request message
     */
    @Override
    public void visit(AvailableSmallDecksRequestMessage message) {
        try{
            client.getRemoteReference().freeSmallDecksRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current game state message
     */
    @Override
    public void visit(GameStateMessage message) {
        try{
            client.getRemoteReference().gameStateMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current generic message
     */
    @Override
    public void visit(GenericMessage message) {
        try{
            client.getRemoteReference().genericMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current log request message
     */
    @Override
    public void visit(LogRequestMessage message) {
        try{
            client.getRemoteReference().logRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current log response message
     */
    @Override
    public void visit(LogResponseMessage message) {
        try{
            client.getRemoteReference().logResponseMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    @Override
    public void visit(NotifyActionCompleted message) throws IOException {
        try{
            client.getRemoteReference().notifyActionCompleted(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current pick revealed component request message
     */
    @Override
    public void visit(PickRevealedComponentRequestMessage message) {
        try{
            client.getRemoteReference().pickRevealedComponentRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current place ship request message
     */
    @Override
    public void visit(PlaceShipRequestMessage message) {
        try{
            client.getRemoteReference().placeShipRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current planet land request message
     */
    @Override
    public void visit(PlanetLandRequestMessage message) {
        try{
            client.getRemoteReference().planetLandRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current players number request message
     */
    @Override
    public void visit(PlayersNumRequestMessage message) {
        try{
            client.getRemoteReference().playersNumRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current player number response message
     */
    @Override
    public void visit(PlayersNumResponseMessage message) {
        try{
            client.getRemoteReference().playersNumResponseMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current players positions message
     */
    @Override
    public void visit(PlayersPositionsMessage message) {
        try{
            client.getRemoteReference().playersPositionMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current positions and turn message
     */
    @Override
    public void visit(PositionsAndTurnsMessage message) {
        try{
            client.getRemoteReference().positionsAndTurnsMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current pre-made ship component message
     */
    @Override
    public void visit(PremadeShipComponentRequest message) {
        try{
            client.getRemoteReference().premadeShipComponentRequest(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current pre-made ship component message
     */
    @Override
    public void visit(PremadeShipComponentResponse message) {
        try{
            client.getRemoteReference().premadeShipComponentResponse(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current projectile trajectory message
     */
    @Override
    public void visit(ProjectileTrajectoryMessage message) {
        try{
            client.getRemoteReference().projectileTrajectoryMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current return component request message
     */
    @Override
    public void visit(ReturnComponentRequestMessage message) {
        try{
            client.getRemoteReference().returnComponentRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current return small deck message
     */
    @Override
    public void visit(ReturnSmallDeckMessage message) {
        try{
            client.getRemoteReference().returnSmallDeckMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current revealed component message
     */
    @Override
    public void visit(RevealedComponentsMessage message) {
        try{
            client.getRemoteReference().revealedComponentsMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current small deck request message
     */
    @Override
    public void visit(SmallDeckRequestMessage message) {
        try{
            client.getRemoteReference().smallDeckRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current small deck response message
     */
    @Override
    public void visit(SmallDeckResponseMessage message) {
        try{
            client.getRemoteReference().smallDeckResponseMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current start timer request message
     */
    @Override
    public void visit(StartTimerRequestMessage message) {
        try{
            client.getRemoteReference().startTimerRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current stocks to add message
     */
    @Override
    public void visit(StocksToAddMessage message) {
        try{
            client.getRemoteReference().stocksToAddMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current timer expired message
     */
    @Override
    public void visit(TimerExpiredMessage message) {
        try{
            client.getRemoteReference().timerExpiredMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current timer started message
     */
    @Override
    public void visit(TimerStartedMessage message) {
        try{
            client.getRemoteReference().timerStartedMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current turn message
     */
    @Override
    public void visit(TurnMessage message) {
        try{
            client.getRemoteReference().turnMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current update client ship message
     */
    @Override
    public void visit(UpdateClientShipMessage message) {
        try{
            client.getRemoteReference().updateClientShipMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current update server ship message
     */
    @Override
    public void visit(UpdateServerShipMessage message) {
        try{
            client.getRemoteReference().updateServerShipMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    @Override
    public void visit(WaitForOthersTurns message) {
        try{
            client.getRemoteReference().waitForOthersTurns(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current waiting partners message
     */
    @Override
    public void visit(WaitingPartnersMessage message) {
        try{
            client.getRemoteReference().waitingPartnersMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current winners message
     */
    @Override
    public void visit(WinnersMessage message) {
        try{
            client.getRemoteReference().winnersMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current resilience request message
     */
    @Override
    public void visit(ResilienceRequestMessage message) {
        try{
            client.getRemoteReference().resilienceRequestMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current resilience response message
     */
    @Override
    public void visit(ResilienceResponseMessage message) {
        try{
            client.getRemoteReference().resilienceResponseMessage(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to expose the related server method according to the message
     * @param message current resume game message
     */
    @Override
    public void visit(ResumeGameMessage message) throws IOException {
        try{
            client.getRemoteReference().resumeGame(message);
        }
        catch (RemoteException e) {
            printProblem(e);
        }
    }

    /**
     * Method used to print the current remote exception
     * @param e current remote exception
     */
    private void printProblem(RemoteException e){e.printStackTrace();}
}
