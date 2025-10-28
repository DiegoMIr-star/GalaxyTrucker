package Connections.RMI;

import Connections.Messages.*;
import Connections.Server;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * RMI server implementation
 */
public class ServerImpl extends UnicastRemoteObject implements ServerRMI {

    /**
     * Current server
     */
    private final Server server;

    /**
     * Current port
     */
    private final int port=1099;

    /**
     * Class constructor, used to initialize the attributes
     * @param server current server
     * @throws RemoteException remote problem of communication between server and client
     */
    public ServerImpl(Server server) throws RemoteException {
        super();
        this.server = server;
    }

    /**
     * Method used to register the server
     * @throws RemoteException remote problem of communication between server and client
     * @throws AlreadyBoundException thrown if there's still a binding with the register
     */
    public void start() throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(port);
        registry.bind("Server", this);
        System.out.println("Server RMI started");
    }

    /**
     * It creates a ClientHandlerRmi for a client and registers the client in the server
     * @param client current RMI client
     * @param nickname player nickname
     * @throws IOException input output exception thrown
     */
    @Override
    public void addClient(RemoteClientRMI client, String nickname) throws IOException {
        ClientHandlerRmi handlerRmi = new ClientHandlerRmi(this, client);
        server.addClient(handlerRmi, nickname);
        System.out.println("Server RMI added client " + client.toString());
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message availableSmallDeckMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void availableSmallDeckMessage(AvailableSmallDeckMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message beginMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void beginMessage(BeginMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message cardActivationRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void cardActivationRequestMessage(CardActivationRequestMessage message) throws RemoteException {
        try {
            server.handleMessageDockingRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message cardDrawnMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void cardDrawnMessage(CardDrawnMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message claimRewardChoiceMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void claimRewardChoiceMessage(ClaimRewardChoiceMessage message) throws RemoteException {
        try {
            server.handleMessageClaimRewardRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message creditsEarnedMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void creditsEarnedMessage(CreditsEarnedMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message discardStocksMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void discardStocksMessage(DiscardStocksMessage message) throws RemoteException {
        try {
            server.handleMessageAddAndRearrangeStocksRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message drawCardRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void drawCardRequestMessage(DrawCardRequestMessage message) throws RemoteException {
        try {
            server.handleMessageCardDrawingRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message drawComponentRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void drawComponentRequestMessage(DrawComponentRequestMessage message) throws RemoteException {
        try {
            server.handleMessageShipConstructionRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message drawComponentResponseMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void drawComponentResponseMessage(DrawComponentResponseMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message drawSpecificComponentRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void drawSpecificComponentRequestMessage(DrawSpecificComponentRequestMessage message) throws RemoteException {
        try {
            server.handleMessageShipConstructionRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message dynamicFirePowerMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void dynamicFirePowerMessage(DynamicFirePowerMessage message) throws RemoteException {
        try {
            server.handleMessageFirePowerRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message dynamicMotorPowerMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void dynamicMotorPowerMessage(DynamicMotorPowerMessage message) throws RemoteException {
        try {
            server.handleMessageMotorPowerRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message endGameMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void endGameMessage(EndGameMessage message) throws RemoteException {
        try {
            server.handleMessageEndGame(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message freePlanetsResponseMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void freePlanetsResponseMessage(FreePlanetsResponseMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message availableSmallDecksRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void freeSmallDecksRequestMessage(AvailableSmallDecksRequestMessage message) throws RemoteException {
        try {
            server.handleMessageShipConstructionRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message gameStateMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void gameStateMessage(GameStateMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message genericMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void genericMessage(GenericMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message logRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void logRequestMessage(LogRequestMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message logResponseMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void logResponseMessage(LogResponseMessage message) throws RemoteException {}

    @Override
    public void notifyActionCompleted(NotifyActionCompleted message) throws RemoteException {
        server.handleMessageNotifyActionCompletedRMI(message);
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message pickRevealedComponentRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void pickRevealedComponentRequestMessage(PickRevealedComponentRequestMessage message) throws RemoteException {
        try {
            server.handleMessageShipConstructionRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message placeShipRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void placeShipRequestMessage(PlaceShipRequestMessage message) throws RemoteException {
        try {
            server.getState().accept(new SecondStateVisitorServerImplementation(server,message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message planetLandRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void planetLandRequestMessage(PlanetLandRequestMessage message) throws RemoteException {
        try {
            server.handleMessagePLANETS_LandingRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message playersNumRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void playersNumRequestMessage(PlayersNumRequestMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message playersNumResponseMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void playersNumResponseMessage(PlayersNumResponseMessage message) throws RemoteException {
        try {
            server.handleMessageInitializationRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message playersPositionsMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void playersPositionMessage(PlayersPositionsMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message positionsAndTurnsMessage
     */
    @Override
    public void positionsAndTurnsMessage(PositionsAndTurnsMessage message){}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message premadeShipComponentRequest
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void premadeShipComponentRequest(PremadeShipComponentRequest message) throws RemoteException {
        try {
            server.handleMessageShipConstructionRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message premadeShipComponentResponse
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void premadeShipComponentResponse(PremadeShipComponentResponse message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message projectileTrajectoryMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void projectileTrajectoryMessage(ProjectileTrajectoryMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message returnComponentRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void returnComponentRequestMessage(ReturnComponentRequestMessage message) throws RemoteException {
        try {
            server.handleMessageShipConstructionRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message returnSmallDeckMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void returnSmallDeckMessage(ReturnSmallDeckMessage message) throws RemoteException {
        try {
            server.handleMessageShipConstructionRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message revealedComponentsMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void revealedComponentsMessage(RevealedComponentsMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message smallDeckRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void smallDeckRequestMessage(SmallDeckRequestMessage message) throws RemoteException {
        try {
            server.handleMessageShipConstructionRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message smallDeckResponseMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void smallDeckResponseMessage(SmallDeckResponseMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message startTimerRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void startTimerRequestMessage(StartTimerRequestMessage message) throws RemoteException {
        try {
            server.handleMessageShipConstructionRMI(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message stocksToAddMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void stocksToAddMessage(StocksToAddMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message timerExpiredMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void timerExpiredMessage(TimerExpiredMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message timerStartedMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void timerStartedMessage(TimerStartedMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message turnMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void turnMessage(TurnMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message updateClientShipMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void updateClientShipMessage(UpdateClientShipMessage message) throws RemoteException {}

    @Override
    public void waitForOthersTurns(WaitForOthersTurns message) {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message waitingPartnersMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void waitingPartnersMessage(WaitingPartnersMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message updateServerShipMessage
     */
    @Override
    public void updateServerShipMessage(UpdateServerShipMessage message) {
        try {
            server.getState().accept(new StateHandlerVisitorServerImplementation(server,message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message winnersMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void winnersMessage(WinnersMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message resilienceRequestMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void resilienceRequestMessage(ResilienceRequestMessage message) throws RemoteException {}

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message resilienceResponseMessage
     * @throws RemoteException remote problem of communication between server and client
     */
    @Override
    public void resilienceResponseMessage(ResilienceResponseMessage message) throws RemoteException {
        try {
            server.handleResilience(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to have a different RMI call in order to call directly the controller
     * @param message resumeGameMessage
     */
    @Override
    public void resumeGame(ResumeGameMessage message) {}

    /**
     * Getter of the current server
     * @return current server
     */
    public Server getServer() {return server;}
}
