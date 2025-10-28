package Connections.RMI;

import Connections.Client;
import Connections.ClientInterface;
import Connections.Messages.*;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Class for representation of client with RMI connection
 */
public class ClientRMI extends Client implements RemoteClientRMI{

    /**
     * Current IP
     */
    private final String IP;

    /**
     * Current port
     */
    private final int port;

    /**
     * Current client manager
     */
    private final ClientInterface clientInterface;

    /**
     * RMI server used for communicating
     */
    private ServerRMI serverRMI;

    /**
     * Constructor initializes the IP and the port, associating also the right client interface,
     * it assigns also the related server by the registry
     * @param IP current IP
     * @param port current port
     * @param clientInterface current client interface
     * @throws RemoteException thrown if there are problem during RMI connection
     * @throws NotBoundException thrown when there's a not associated name for bounding
     */
    public ClientRMI(String IP,int port,ClientInterface clientInterface) throws RemoteException, NotBoundException {
        this.IP=IP;
        this.port=port;
        this.clientInterface=clientInterface;
        try{
            serverRMI= (ServerRMI) LocateRegistry.getRegistry(this.IP,this.port).lookup("Server");
        }
        catch(Exception e){
            throw new NotBoundException("Server not found");
        }
    }

    /**
     * It uses the reference to the server to send its own message
     * @param message current message
     * @throws RemoteException thrown if there are problem during RMI connection
     */
    @Override
    public void sendMessage(Message message) throws RemoteException {
        try{
            if(message.getKind().equals(MessageKind.LOG_REQUEST)){
                serverRMI.addClient(this, message.getNickname());
            }
            else message.accept(new RMIMessageSender(this));
        }
        catch(Exception e){
            System.out.println("Remote exception:");
            e.printStackTrace();
            throw new RemoteException("\n");
            //disconnect();
        }
    }

    /**
     * Getter of RMI server
     * @return current RMI server
     */
    @Override
    public ServerRMI getRemoteReference(){return serverRMI;}

    /**
     * Method used form the server to call directly the update small deck method in client interface
     * @see ClientInterface
     * @param message availableSmallDeckMessage
     */
    @Override
    public void availableSmallDeckMessage(AvailableSmallDeckMessage message) {clientInterface.updateSmallDecksCall(message.getDecksIndexes());}

    /**
     * Method used form the server to call directly the beginning method in client interface
     * @see ClientInterface
     * @param message beginMessage
     */
    @Override
    public void beginMessage(BeginMessage message) {clientInterface.beginCall(message.getPlayers());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message cardActivationRequestMessage
     */
    @Override
    public void cardActivationRequestMessage(CardActivationRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the card drawn method in client interface
     * @see ClientInterface
     * @param message cardDrawnMessage
     */
    @Override
    public void cardDrawnMessage(CardDrawnMessage message) {clientInterface.cardDrawnCall(message.getDrawnCard());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message claimRewardChoiceMessage
     */
    @Override
    public void claimRewardChoiceMessage(ClaimRewardChoiceMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the credits earned method in client interface
     * @see ClientInterface
     * @param message creditsEarnedMessage
     */
    @Override
    public void creditsEarnedMessage(CreditsEarnedMessage message) {clientInterface.creditsEarnedCall(message.getCreditsEarned());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message discardStocksMessage
     */
    @Override
    public void discardStocksMessage(DiscardStocksMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message drawCardRequestMessage
     */
    @Override
    public void drawCardRequestMessage(DrawCardRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message drawComponentRequestMessage
     */
    @Override
    public void drawComponentRequestMessage(DrawComponentRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the draw component method in client interface
     * @see ClientInterface
     * @param message drawComponentResponseMessage
     */
    @Override
    public void drawComponentResponseMessage(DrawComponentResponseMessage message) {clientInterface.drawCompnentCall(message.getComponent());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message drawSpecificComponentRequestMessage
     */
    @Override
    public void drawSpecificComponentRequestMessage(DrawSpecificComponentRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message dynamicFirePowerMessage
     */
    @Override
    public void dynamicFirePowerMessage(DynamicFirePowerMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message dynamicMotorPowerMessage
     */
    @Override
    public void dynamicMotorPowerMessage(DynamicMotorPowerMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message endGameMessage
     */
    @Override
    public void endGameMessage(EndGameMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the free planets method in client interface
     * @see ClientInterface
     * @param message freePlanetsResponseMessage
     */
    @Override
    public void freePlanetsResponseMessage(FreePlanetsResponseMessage message) {clientInterface.freePlanetsCall(message.getFreePlanetsIndexes());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message availableSmallDecksRequestMessage
     */
    @Override
    public void freeSmallDecksRequestMessage(AvailableSmallDecksRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the game state method in client interface
     * @see ClientInterface
     * @param message gameStateMessage
     */
    @Override
    public void gameStateMessage(GameStateMessage message) {clientInterface.gameStateCall(message.getGameState());}

    /**
     * Method used form the server to call directly the generic method in client interface
     * @see ClientInterface
     * @param message genericMessage
     */
    @Override
    public void genericMessage(GenericMessage message) {clientInterface.genericCall(message.getMessage());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message logRequestMessage
     */
    @Override
    public void logRequestMessage(LogRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the log response method in client interface
     * @see ClientInterface
     * @param message logResponseMessage
     */
    @Override
    public void logResponseMessage(LogResponseMessage message) {clientInterface.logResponseCall(message);}

    @Override
    public void notifyActionCompleted(NotifyActionCompleted message) {
        clientInterface.incorrectCall(message.getKind());
    }

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message pickRevealedComponentRequestMessage
     */
    @Override
    public void pickRevealedComponentRequestMessage(PickRevealedComponentRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message placeShipRequestMessage
     */
    @Override
    public void placeShipRequestMessage(PlaceShipRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message planetLandRequestMessage
     */
    @Override
    public void planetLandRequestMessage(PlanetLandRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the players number method in client interface
     * @see ClientInterface
     * @param message playersNumRequestMessage
     */
    @Override
    public void playersNumRequestMessage(PlayersNumRequestMessage message) {clientInterface.playersNumCall();}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message playersNumResponseMessage
     */
    @Override
    public void playersNumResponseMessage(PlayersNumResponseMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message playersPositionsMessage
     */
    @Override
    public void playersPositionMessage(PlayersPositionsMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the position and turns method in client interface
     * @see ClientInterface
     * @param message positionsAndTurnsMessage
     */
    @Override
    public void positionsAndTurnsMessage(PositionsAndTurnsMessage message) {clientInterface.positionsAndTurnsCall(message);}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message premadeShipComponentRequest
     */
    @Override
    public void premadeShipComponentRequest(PremadeShipComponentRequest message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the pre-made ship component method in client interface
     * @see ClientInterface
     * @param message premadeShipComponentResponse
     */
    @Override
    public void premadeShipComponentResponse(PremadeShipComponentResponse message) {clientInterface.premadeShipComponentCall(message.getComponent());}

    /**
     * Method used form the server to call directly the projectile trajectory method in client interface
     * @see ClientInterface
     * @param message projectileTrajectoryMessage
     */
    @Override
    public void projectileTrajectoryMessage(ProjectileTrajectoryMessage message) {clientInterface.projectileTrajectoryCall(message.getProjectile(), message.getTrajectory());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message returnComponentRequestMessage
     */
    @Override
    public void returnComponentRequestMessage(ReturnComponentRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message returnSmallDeckMessage
     */
    @Override
    public void returnSmallDeckMessage(ReturnSmallDeckMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the revealed component method in client interface
     * @see ClientInterface
     * @param message revealedComponentsMessage
     */
    @Override
    public void revealedComponentsMessage(RevealedComponentsMessage message) {clientInterface.revealedComponentsCall(message.getRevealedComponents());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message smallDeckRequestMessage
     */
    @Override
    public void smallDeckRequestMessage(SmallDeckRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the small deck method in client interface
     * @see ClientInterface
     * @param message smallDeckResponseMessage
     */
    @Override
    public void smallDeckResponseMessage(SmallDeckResponseMessage message) {clientInterface.smallDeckResponseCall(message.getDeck(), message.getDeckIndex());}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message startTimerRequestMessage
     */
    @Override
    public void startTimerRequestMessage(StartTimerRequestMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the stocks to add method in client interface
     * @see ClientInterface
     * @param message stocksToAddMessage
     */
    @Override
    public void stocksToAddMessage(StocksToAddMessage message) {clientInterface.stocksToAddCall(message.getStocks());}

    /**
     * Method used form the server to call directly the timer expired method in client interface
     * @see ClientInterface
     * @param message timerExpiredMessage
     */
    @Override
    public void timerExpiredMessage(TimerExpiredMessage message) {clientInterface.timerExpiredCall(message.isLast());}

    /**
     * Method used form the server to call directly the timer started method in client interface
     * @see ClientInterface
     * @param message timerStartedMessage
     */
    @Override
    public void timerStartedMessage(TimerStartedMessage message) {clientInterface.timerStartedCall(message.isLast());}

    /**
     * Method used form the server to call directly the turn method in client interface
     * @see ClientInterface
     * @param message turnMessage
     */
    @Override
    public void turnMessage(TurnMessage message) {clientInterface.turnCall();}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message updateClientShipMessage
     */
    @Override
    public void updateClientShipMessage(UpdateClientShipMessage message) {clientInterface.ship.updateShip(message.getUpdatedShip().getShip());}

    @Override
    public void waitForOthersTurns(WaitForOthersTurns message) {
        clientInterface.waitingTurnsCall();
    }

    /**
     * Method used form the server to call directly the waiting method in client interface
     * @see ClientInterface
     * @param message waitingPartnersMessage
     */
    @Override
    public void waitingPartnersMessage(WaitingPartnersMessage message) {clientInterface.waitingCall();}

    /**
     * Method used form the server to call directly the incorrect method in client interface
     * @see ClientInterface
     * @param message updateServerShipMessage
     */
    @Override
    public void updateServerShipMessage(UpdateServerShipMessage message) {clientInterface.incorrectCall(message.getKind());}

    /**
     * Method used form the server to call directly the winners method in client interface
     * @see ClientInterface
     * @param message winnersMessage
     */
    @Override
    public void winnersMessage(WinnersMessage message) {clientInterface.winnersCall(message.getWinners());}

    /**
     * Method used form the server to call directly the client resilience method in client interface
     * @see ClientInterface
     * @param message resilienceRequestMessage
     */
    @Override
    public void resilienceRequestMessage(ResilienceRequestMessage message) {clientInterface.clientResilienceCall();}

    /**
     * Method with no action: server RMI doesn't call for a resilience response, but only for requests
     * @param message resilienceResponseMessage
     */
    @Override
    public void resilienceResponseMessage(ResilienceResponseMessage message) {}

    /**
     * Method used form the server to call directly the resume method in client interface
     * @see ClientInterface
     * @param message resumeGameMessage
     */
    @Override
    public void resumeGame(ResumeGameMessage message) {clientInterface.resumeCall(message.getShip(), message.getCurCard(), message.getGameState(), message.getLastMessage());}

    /**
     * Method used for disconnection
     */
    @Override
    public void disconnect(){
        if(serverRMI!=null){
            serverRMI=null;
            System.out.println("Server disconnected.");
        }
    }
}
