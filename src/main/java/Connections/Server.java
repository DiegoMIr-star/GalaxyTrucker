package Connections;

import Connections.Messages.Message;
import Connections.Messages.NotifyActionCompleted;
import Connections.Messages.ResilienceResponseMessage;
import Controller.Controller;
import Controller.State.GameState;
import Controller.State.ShipConstructionState;
import View.ColorManagement.ConsoleColor;
import View.VirtualView;
import Controller.Persistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * Class that can be adapted in case of rmi or socket connection
 */
public abstract class Server {


	/**
	 * It contains a map that links clients with their nicknames
	 */
	//        vvvvvv
	protected static final Map<ClientHandler, String> clients = Collections.synchronizedMap(new HashMap<>());

	/**
	 * Constructor used to initialise the Controller.getInstance() also with persistence
	 */
	public Server() {
		Persistence persistence = Persistence.getInstance();

		if (!Controller.isInitialized() && persistence.directoryWithFileExists()) {
			try {
				Controller loaded = persistence.load();
				Controller.setInstance(loaded);
				Controller.getInstance().disableResilience();
				Controller.getInstance().searchPlayersForPersistence();
			} catch (Exception e) {
				System.out.println("There's a problem in loading a previous game...");
				System.out.println("The players will start with a new one...");
			}
		}

		if (!Controller.isInitialized()) {
			Controller.setInstance(Controller.getInstance());
		}
	}

	/**
	 * Method to obtain the nickname from the client interface in the map
	 * @param client current client to find
	 * @return nickname of the client
	 */
	public String getNicknameFromMap(ClientHandler client){return clients.get(client);}

	/**
	 * Method to obtain clients from their nicknames in the map
	 * @param nickname nickname of the player
	 * @return current client
	 */
	public ClientHandler getClientFromMap(String nickname){
		for(Map.Entry<ClientHandler, String> entry : clients.entrySet()){
			if(entry.getValue().equals(nickname)){
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * Method used to add a client in a game
	 * @param client current client
	 * @param nickname nickname of the player
	 * @throws IOException input output exception
	 */
	public void addClient(ClientHandler client, String nickname) throws IOException {

		VirtualView view = new VirtualView(client, Controller.getInstance()::removeFailedClient, nickname);

		System.out.println("Adding new client");
		if (Controller.getInstance().isLost(nickname)) {

			for (Map.Entry<ClientHandler, String> entry : clients.entrySet()) {
				if (entry.getValue().equals(nickname)) {
					clients.remove(entry.getKey());
					break;
				}
			}

			clients.put(client, nickname);



			Controller.getInstance().insertLost(nickname, view);

		}
		else if (!Controller.getInstance().isGameBlocked()) {
			if (Controller.getInstance().checkNickname(nickname)) view.loginResponse(true, false, -1);
			else {

				clients.put(client, nickname);

				view.loginResponse(true, true, clients.size());
				Controller.getInstance().manageLogin(nickname, view);
			}
		}
		else {
			if (Controller.getInstance().checkNickname(nickname)) {
				view.sendGenericMessage(
						"You are trying to log in as a player that is still connected. Try again.");
				view.loginResponse(true, false, -1);
			}
			else {
				view.sendGenericMessage(
						"As a new player, you cannot connect to the ongoing game!");
			}
		}

	}

	/**
	 * Method used to remove Clients
	 * @param client current client
	 */
	public void disconnectClient(ClientHandler client){
		System.out.println(getNicknameFromMap(client)+ " disconnected");
		Controller.getInstance().disconnectPlayer(getNicknameFromMap(client));

		clients.remove(client);

	}

	/**
	 * Getter of the Controller.getInstance()
	 * @return current Controller.getInstance()
	 */
	public Controller getController() {return Controller.getInstance();}

	/**
	 * Method to move messages from clients to Controller.getInstance(),
	 * it's abstract because each subtype do it differently
	 * @param message current message
	 * @throws IOException input output exception thrown
	 */
	public abstract void handleMessage(Message message) throws IOException;

	/**
	 * Getter of the current state in the Controller.getInstance()
	 * @return current state
	 */
	public GameState getState(){return Controller.getInstance().getState();}

	/**
	 * Method used to expose the handle initialization of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 * @throws IOException input output exception thrown
	 */
	public void handleMessageInitializationRMI(Message message) throws IOException {
		if(!(Controller.getInstance().getGameState() instanceof ShipConstructionState))
			Controller.getInstance().handleInitializationState(message,true);
		else
			Controller.getInstance().handlePlayerNumChoiceStolen(message);
	}

	/**
	 * Method used to expose the handle add and rearrange stocks of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 * @throws IOException input output exception thrown
	 */
	public void handleMessageAddAndRearrangeStocksRMI(Message message) throws IOException {Controller.getInstance().handleAddAndRearrangeStocks(message,true);}

	/**
	 * Method used to expose the handle card drawing of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 * @throws IOException input output exception thrown
	 */
	public void handleMessageCardDrawingRMI(Message message) throws IOException {Controller.getInstance().handleCardDrawing(message,true);}

	/**
	 * Method used to expose the handle claim reward of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 * @throws IOException input output exception thrown
	 */
	public void handleMessageClaimRewardRMI(Message message) throws IOException {Controller.getInstance().handleClaimRewardChoice(message,true);}

	/**
	 * Method used to expose the handle docking of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 * @throws IOException input output exception thrown
	 */
	public void handleMessageDockingRMI(Message message) throws IOException {Controller.getInstance().handleDockingChoice(message,true);}

	/**
	 * Method used to expose the handle end game of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 * @throws IOException input output exception thrown
	 */
	public void handleMessageEndGame(Message message) throws IOException {
		ClientHandler clientToDisconnect = null;
		for(ClientHandler client : clients.keySet()){
			if(clients.get(client).equals(message.getNickname())){
				clientToDisconnect = client;
			}
		}
		disconnectClient(clientToDisconnect);
		Controller.getInstance().handleEndGame(message,true);
	}

	/**
	 * Method used to expose the handle fire power of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 * @throws IOException input output exception thrown
	 */
	public void handleMessageFirePowerRMI(Message message) throws IOException {Controller.getInstance().handleFirePowerChoice(message,true);}

	/**
	 * Method used to expose the handle give up crew of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 */
	public void handleMessageGiveUpCrewRMI(Message message) {Controller.getInstance().handleGiveUpCrewChoice(message,true);}

	/**
	 * Method used to expose the handle manage projectile of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 */
	public void handleMessageManageProjectileRMI(Message message) {Controller.getInstance().handleManageProjectile(message,true);}

	/**
	 * Method used to expose the handle motor power of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 * @throws IOException input output exception thrown
	 */
	public void handleMessageMotorPowerRMI(Message message) throws IOException {Controller.getInstance().handleMotorPowerChoice(message,true);}

	/**
	 * Method used to expose the handle planets landing of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 * @throws IOException input output exception thrown
	 */
	public void handleMessagePLANETS_LandingRMI(Message message) throws IOException {Controller.getInstance().handlePLANETS_LandingChoice(message,true);}

	/**
	 * Method used to expose the handle ship construction of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 * @throws IOException input output exception thrown
	 */
	public void handleMessageShipConstructionRMI(Message message) throws IOException {Controller.getInstance().handleShipConstructionState(message,true);}

	public void handleMessageNotifyActionCompletedRMI(NotifyActionCompleted message) {
		Controller.getInstance().handleActionCompletedNotification(message);
	}

	/**
	 * Method used to expose the handle to be fixed of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 * @throws IOException input output exception thrown
	 */
	public void handleMessageToBeFixedRMI(Message message) throws IOException {
		Controller.getInstance().setBeginning(false);
		Controller.getInstance().handleToBeFixedAndFixingShips(message,true);
	}

	/**
	 * Method used to expose the handle resilience of Controller.getInstance() for RMI connection
	 * @param message current message to manage
	 * @throws IOException input output exception thrown
	 */
	public void handleResilience(Message message) throws IOException {
		ResilienceResponseMessage newMessage = (ResilienceResponseMessage) message;
		Controller.getInstance().setResilience(newMessage.getNickname(),newMessage.getShip());
	}
}


