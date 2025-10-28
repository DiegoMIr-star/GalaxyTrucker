package Controller;

import Connections.Messages.*;
import Controller.State.*;
import Loader.ObjectsCard;
import Loader.Loader;
import Loader.ObjectsComponent;
import View.ColorManagement.ConsoleColor;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import model.*;
import model.Cards.*;
import model.Cards.CardVisitorProgresser.*;
import model.DifferentShipComponents.ShipComponent;
import View.VirtualView;
import model.exceptions.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.*;

/**
 * Game controller class: it contains the entire logic of the game, it uses model classes
 * in order to handle a game with different players
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Controller implements Serializable {
	/**
	 * SINGLE instance of controller
	 */
	private static Controller instance;

	/**
	 * Current game state
	 */
	private GameState state;

	/**
	 * Current persistence, used to save the current game
	 *
	 * @see Persistence
	 */
	private transient Persistence persistence;

	/**
	 * Current game
	 */
	private Game game;

	/**
	 * List of players ship dashboard
	 *
	 * @see ShipDashboard
	 */
	private ArrayList<ShipDashboard> players;

	/**
	 * List of players ship dashboard that have actually been built
	 *
	 * @see ShipDashboard
	 */
	private ArrayList<ShipDashboard> playersBuilt;

	/**
	 * Boolean array used to signal fixed ships per player
	 */
	private ConcurrentHashMap<String, Boolean> fixedShips;

	/**
	 * List of game cards
	 */
	private final ArrayList<Card> cards;

	/**
	 * List of ship components
	 */
	private final ArrayList<ShipComponent> shipComponents;

	/**
	 * Map of virtual-views assigned to each player
	 */
	private transient ConcurrentHashMap<String, VirtualView> virtualViews;

	/**
	 * Map of virtual-views assigned to each player between one game and the other
	 */
	private transient ConcurrentHashMap<String, VirtualView> newGameVirtualViews;

	/**
	 * Map used to signal for each player who is active
	 */
	private final ConcurrentHashMap<String, Boolean> resilientActivation;

	/**
	 * List of eliminated ship dashboards
	 */
	private final ArrayList<ShipDashboard> eliminatedPlayers;

	/**
	 * Map of virtual-views assigned to each eliminated player
	 */
	private transient ConcurrentHashMap<String, VirtualView> eliminatedVirtualViews;

	/**
	 * Amount of current players
	 */
	private int numPlayers;

	/**
	 * True if the game has surpassed the part of ship construction state
	 */
	private boolean beginning;

	/**
	 * Current card
	 */
	private Card curCard;

	/**
	 * Index of the current player, saved to send the message
	 */
	private int senderIndex;

	/**
	 * True if the number of players is reached
	 */
	private boolean blockGame;

	/**
	 * List of players nickname
	 */
	private ArrayList<String> allPlayers = new ArrayList<>();

	//**********************************************
	//Resilience

	/**
	 * Scheduler executor, used to activate a task
	 */
	private transient ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


	//private transient ScheduledExecutorService scheduler2 = Executors.newSingleThreadScheduledExecutor();

	/**
	 * Task used to handle resilience
	 */
	private transient ScheduledFuture<?> taskHandle;

	/**
	 * True if resilience is already activated
	 */
	private boolean activation = false;

	/**
	 * List of nicknames of the lost players
	 */
	private ArrayList<String> lostPlayers = new ArrayList<>();

	/**
	 * List of lost ship dashboards
	 */
	private final ConcurrentHashMap<String, ShipDashboard> lostPlayersShip = new ConcurrentHashMap<>();

	/**
	 * List of ships saved during the construction
	 */
	private final ArrayList<ShipDashboard> constructionShip = new ArrayList<>();

	/**
	 * Map of the last messages sent, in order to revive lost players
	 */
	private final ConcurrentHashMap<String, Message> lastMessages;

	/**
	 * Boolean that tells whether the game that is currently being created is after another game
	 * https://i.redd.it/qxp3o00bz9e31.jpg
	 */
	private boolean weAreInTheEndGameNow = false;

	/**
	 * Boolean that tells whether the controller was recently reset
	 */
	private boolean controllerWasReset = false;

	/**
	 * IDs of the central cabin
	 */
	private final int[] centralCabinID;

	/**
	 * The positions of the players at the start of the turn
	 */
	private final ConcurrentHashMap<String, Integer> turnStartPositions = new ConcurrentHashMap<>();

	/**
	 * getter of the controller instance
	 *
	 * @return the controller instance
	 */
	public static Controller getInstance() {
		if (instance == null) {
			try {
				instance = new Controller();
			} catch (IOException e) {
				throw new RuntimeException("There was a problem loading the Controller.getInstance().\n" + e.getMessage());
			}
		}
		return instance;
	}

	/**
	 * Lets us know whether the controller has been initialized
	 * @return if the instance has been initialized
	 */
	public static boolean isInitialized() {
		return instance != null;
	}

	// Add a method to forcibly replace the instance (ONLY for persistence loading)

	/**
	 * set the instance of controller
	 *
	 * @param loadedController the controller loaded with persistance
	 */
	public static void setInstance(Controller loadedController) {
		if (instance == null) {
			instance = loadedController;
		} else {
			System.out.println("Skipping setInstance: Controller already initialized");
		}
	}

	public ArrayList<ShipDashboard> getPlayersBuilt(){
		return playersBuilt;
	}

	private void printShip(ShipDashboard p) {
		for (int y = 0; y < p.getShip().length; y++) {
			for (int x = 0; x < p.getShip()[y].length; x++) {
				System.out.print(p.getShip()[y][x] + "   ");
			}
			System.out.println();
		}
	}

	/**
	 * Method used to send a resilience message to see if the players are active.
	 */
	private void resilienceMessage() {

		resilientActivation.replaceAll((_, _) -> false);

		if (state instanceof ShipConstructionState) {
			for (ShipDashboard p : constructionShip) {
				lostPlayersShip.put(p.getNickname(), p);
			}
		} else {
			if (players != null) {
				for (ShipDashboard p : players) {
					lostPlayersShip.put(p.getNickname(), p);
				}
			}
		}
		for (Map.Entry<String, VirtualView> entry : virtualViews.entrySet()) {
			entry.getValue().sendResilience();
		}
	}

	/**
	 * It removes the failed player from the list of current players view and
	 * it invokes {@link #removePlayerByNickname(String)} to handle the structures for lost players
	 *
	 * @param nickname failed player nickname
	 */
	public void removeFailedClient(String nickname) {
		virtualViews.remove(nickname);
		removePlayerByNickname(nickname);
	}

	/**
	 * It removes the lost player from the list of current players and it saves them in
	 * the lost list
	 *
	 * @param nickname lost player nickname
	 */
	private void removePlayerByNickname(String nickname) {
		lostPlayers.add(nickname);
		if (players != null) {
			Iterator<ShipDashboard> iterator = players.iterator();
			while (iterator.hasNext()) {
				ShipDashboard ship = iterator.next();
				if (Objects.equals(ship.getNickname(), nickname)) {
					lostPlayersShip.put(nickname, ship);
					iterator.remove();
				}
			}
		}
	}

	/**
	 * It begins the task handle and it sends every 10 seconds the message to check the
	 * presence of the players
	 */
	public void startResilienceIfNotActive() {
		if (!activation) {
			taskHandle = scheduler.scheduleAtFixedRate(this::resilienceMessage, 0, 4, TimeUnit.SECONDS);
			activation = true;
			System.out.println("Resilience task activated.");
		} else {
			System.out.println("Resilience task is already activated...");
		}
	}

	/**
	 * Method used to disable resilience after the loading of a previous game
	 */
	public void disableResilience() {
		this.activation = false;
	}

	/**
	 * It cancels the task in order to avoid sending messages for resilience
	 */
	public void stopResilience() {
		if (activation && taskHandle != null) {
			taskHandle.cancel(true);
			activation = false;
			System.out.println("Resilience task stopped.");
		} else {
			System.out.println("Resilience task not active...");
		}
	}

	/**
	 * Setter of resilient activation, it sets the activation for a player if the player
	 * has sent the resilient response
	 *
	 * @param nickname player nickname
	 * @param ship     player ship
	 */
	public void setResilience(String nickname, ShipDashboard ship) {
		this.resilientActivation.put(nickname, true);
		if (state instanceof ShipConstructionState) {
			constructionShip.add(ship);
		}
	}

	/**
	 * It checks if a player is not active
	 *
	 * @param nickname player nickname
	 * @return true if the player is lost
	 */
	public boolean isLost(String nickname) {
		return lostPlayers.contains(nickname);
	}

	/**
	 * It resumes the lost players in the virtual view, when the lost players want to rejoin
	 * the game
	 *
	 * @param nickname player nickname
	 * @param view     player view
	 */
	public void insertLost(String nickname, VirtualView view) {
		ShipDashboard resumedShip = null;
		this.virtualViews.put(nickname, view);
		lostPlayers.remove(nickname);
		if (lostPlayersShip.get(nickname) != null) {
			resumedShip = lostPlayersShip.get(nickname);
		}
		if (resumedShip != null) {
			lostPlayersShip.remove(nickname);
			addOrReplacePlayerShip(resumedShip);
			ConcurrentHashMap<String, Integer> turnCurrentPositions = new ConcurrentHashMap<>();
			//back up the actual current positions
			for(ShipDashboard p : players) {
				turnCurrentPositions.put(p.getNickname(), p.getPosition());
			}
			//change the players positions to the ones they had at the start of the turn
			for(ShipDashboard p : players) {
				if(turnStartPositions.containsKey(p.getNickname()))
					p.setPosition(turnStartPositions.get(p.getNickname()));
			}
			//sort the players based on those positions, to make sure the lost one that was just inserted is in the right place
			players.sort((p1, p2) -> p2.getPosition() - p1.getPosition());
			//change the positions back to their original ones, so the card execution doesn't get messed up
			for(ShipDashboard p : players) {
				if(turnCurrentPositions.containsKey(p.getNickname()))
					p.setPosition(turnCurrentPositions.get(p.getNickname()));
			}
		}
		this.resilientActivation.put(nickname, true);
		ShipDashboard shipToSend = null;
		if (players != null) {
			for (ShipDashboard ship : players) {
				if (ship.getNickname().equals(nickname)) {
					shipToSend = ship.clone();
				}
			}
		}
		if (state instanceof ShipConstructionState) {
			for (int i = 0; i < playersBuilt.size(); i++) {
				if (playersBuilt.get(i).getNickname().equals(nickname)) {
					shipToSend = playersBuilt.get(i);
				}
			}
		}
		GameState currentState = state.clone();
		Card sendCard = null;
		if (curCard != null) {
			sendCard = curCard.clone();
		}
		Message lastMessage = null;
		if (lastMessages.containsKey(nickname)) {
			lastMessage = lastMessages.get(nickname);
		}
		view.sendResume(shipToSend, currentState, sendCard, lastMessage);


		if (state instanceof ShipConstructionState) {
			view.sendRevealedComponents(game.getRevealedShipComponents());
			view.sendAvailableSmallDecks(getAvailableDecksIndexes());
			if (game.isTimerRunning())
				view.timerStarted(game.getTimer().getCurRoundSeconds(), game.getTimer().curRoundIsLast());
			else try {
				view.timerExpired(game.getTimer().curRoundIsLast());
			} catch (IOException e) {
				throw new RuntimeException("Problem while communicating to clients timer expiration.");
			}
		} else if (state instanceof WaitingForPlayers) {
			try {
				manageLogin(nickname, view);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * It sets the transient methods after a server disconnection, it's used for persistence.
	 * It restarts the resilience server messages
	 */
	public void searchPlayersForPersistence() {
		this.lostPlayers = (ArrayList<String>) this.allPlayers.clone();
		for (String nickname : lostPlayers) {
			resilientActivation.put(nickname, false);
		}
		persistence = Persistence.getInstance();
		virtualViews = new ConcurrentHashMap<>();
		eliminatedVirtualViews = new ConcurrentHashMap<>();
		scheduler = Executors.newSingleThreadScheduledExecutor();
		//scheduler2 = Executors.newSingleThreadScheduledExecutor();
		this.game.setTimerPersistence();
		//loneliness();
		startResilienceIfNotActive();
	}

	/**
	 * Adds the player to the players list if it doesn't already contain a ship with the same nickname associated to it
	 * @param player the player to add
	 */
	private void addOrReplacePlayerShip(ShipDashboard player){
		for(ShipDashboard p : players){
			if(p.getNickname().equals(player.getNickname())){
				players.remove(p);
				break;
			}
		}
		players.add(player);
	}

	/**
	 * It adds the last message for a player
	 *
	 * @param nickname player nickname
	 * @param message  player last message
	 */
	private void putLastMessage(String nickname, Message message) {
		if (message != null) {
			lastMessages.put(nickname, message);
		} else {
			lastMessages.remove(nickname);
		}
	}

	/*

	private transient ScheduledFuture<?> lonelinessTask;


	public void loneliness() {
		Runnable task = () -> {
			if (virtualViews.keySet().size()==1) {
				this.setGameState(new EndGame());
				lostPlayers.clear();
				this.stopLoneliness();
				sendWinners();
			}
		};
		lonelinessTask = scheduler2.scheduleAtFixedRate(task, 0, 15, TimeUnit.SECONDS);
	}


	public void stopLoneliness() {
		if (lonelinessTask != null) {
			lonelinessTask.cancel(false);
		}
	}
	*/
	//**********************************************

	/**
	 * Controller constructor, it initializes the different attributes, in particular loading
	 * the ship components and the cards from the json files
	 *
	 * @throws IOException input output exception thrown
	 */
	Controller() throws IOException {
		lastMessages = new ConcurrentHashMap<>();
		centralCabinID = new int[4];
		centralCabinID[0] = 32;
		centralCabinID[1] = 33;
		centralCabinID[2] = 51;
		centralCabinID[3] = 60;

		ObjectsCard objectsCard = Loader.loadCards();
		this.cards = objectsCard.getCards();
		ObjectsComponent objectsComponent = Loader.loadComponents();
		this.shipComponents = objectsComponent.getShipComponents();
		resilientActivation = new ConcurrentHashMap<>();
		eliminatedPlayers = new ArrayList<>();
		resetController();
		virtualViews = new ConcurrentHashMap<>();
	}

	/**
	 * It checks if the game is blocked
	 *
	 * @return true if the game is blocked
	 */
	public boolean isGameBlocked() {
		return this.blockGame;
	}

	/**
	 * Getter of the current state
	 *
	 * @return current state
	 */
	public GameState getState() {
		return this.state;
	}

	/**
	 * It initializes the persistence
	 *
	 * @param persistence current persistence
	 */
	public void addPersistence(Persistence persistence) {
		this.persistence = persistence;
	}

	/**
	 * After the connection, which takes place in the client, the client updates the view
	 * of the player with the ASK_NICKNAME message. The answer triggers the server, which updates
	 * the state of the Controller, which triggers the starting of the state machine
	 *
	 * @param nickname    player nickname
	 * @param virtualView player virtual view
	 * @throws IOException input output exception thrown
	 */
	public void manageLogin(String nickname, VirtualView virtualView) throws IOException {
		startResilienceIfNotActive();
		if (weAreInTheEndGameNow) {
			if (!controllerWasReset)
				resetController();
			this.newGameVirtualViews.put(nickname, virtualView);
		} else
			this.virtualViews.put(nickname, virtualView);

		ShipDashboard ship;
		if (!weAreInTheEndGameNow)
			ship = new ShipDashboard(centralCabinID[virtualViews.size() - 1]);
		else
			ship = new ShipDashboard(centralCabinID[newGameVirtualViews.size() - 1]);

		ship.setNickname(nickname);
		if (players == null)
			players = new ArrayList<>();
		addOrReplacePlayerShip(ship);



		this.resilientActivation.put(nickname, true);
		//if the player is the first, ask for the number of players
		if (!weAreInTheEndGameNow) {
			manageLoginFromVirtualViews(nickname, virtualViews);
		} else {
			manageLoginFromVirtualViews(nickname, newGameVirtualViews);
		}
	}

	/**
	 * After the connection, which takes place in the client, the client updates the view
	 * of the player with the ASK_NICKNAME message. The answer triggers the server, which updates
	 * the state of the Controller, which triggers the starting of the state machine
	 *
	 * @param nickname          player nickname
	 * @param givenVirtualViews the list of virtual views to consider
	 * @throws IOException input output exception thrown
	 */
	private void manageLoginFromVirtualViews(String nickname, ConcurrentHashMap<String, VirtualView> givenVirtualViews) throws IOException {
		if (numPlayers == -1) {
			changeGameStateAndNotifyClients(new InitializationState());
			//notify turn not needed, this message serves that purpose as well
			putLastMessage(nickname, new PlayersNumRequestMessage());
			givenVirtualViews.get(nickname).sendNumPlayersRequest();
		} else {
			changeGameStateAndNotifyClientsInVirtualViews(new InitializationState(), givenVirtualViews);
			for (VirtualView view : givenVirtualViews.values()) {
				view.notifyTurn();
			}
			checkWaitingForPlayers();
		}
	}

	/**
	 * It checks if a player is in the virtual view map
	 *
	 * @param nickname player nickname
	 * @return true if the player is in the virtual view map
	 */
	public boolean checkNickname(String nickname) {
		if (weAreInTheEndGameNow)
			return newGameVirtualViews.containsKey(nickname);
		else
			return virtualViews.containsKey(nickname);
	}

	/**
	 * This method removes the player and his virtualView from the controller.
	 * This is not a reversible action
	 *
	 * @param nickname player nickname
	 */
	public void disconnectPlayer(String nickname) {
		if (virtualViews.containsKey(nickname)) {
			this.virtualViews.remove(nickname);

		}
		if (players != null)
			players.removeIf(p -> p.getNickname().equals(nickname));
	}

	/**
	 * It sets the new game state and it notifies it to the clients
	 *
	 * @param newGameState current game state
	 */
	void changeGameStateAndNotifyClients(GameState newGameState) {
		changeGameStateAndNotifyClientsInVirtualViews(newGameState, virtualViews);
		changeGameStateAndNotifyClientsInVirtualViews(newGameState, eliminatedVirtualViews);
	}

	/**
	 * It sets the new game state and it notifies it to the clients
	 *
	 * @param newGameState current game state
	 */
	void changeGameStateAndNotifyClientsInVirtualViews(GameState newGameState, ConcurrentHashMap<String, VirtualView> givenVirtualViews) {
		this.state = newGameState;

		for (VirtualView view : givenVirtualViews.values()) {
			view.notifyNewGameState(this.state);
		}
	}

	/**
	 * It handles the next game state and messages object, which contains the next state and
	 * the messages to send after the effect of a card
	 *
	 * @param nextGameStateAndMessages current next game state and messages object
	 * @see NextGameStateAndMessages
	 */
	private void changeGameStateAndSendMessagesToClients(NextGameStateAndMessages nextGameStateAndMessages) {
		if (nextGameStateAndMessages == null)
			return;

		Map<String, ArrayList<Message>> messages = nextGameStateAndMessages.getPlayerMessages();


		if (nextGameStateAndMessages.getPlayers() != null)
			this.players = nextGameStateAndMessages.getPlayers();

		if (nextGameStateAndMessages.getNextGameState() != null) {
			changeGameStateAndNotifyClients(nextGameStateAndMessages.getNextGameState());
		}
		//else: the game state has to remain unchanged and the clients don't need to be notified

		// if there are messages, they are referred to the previous state, so they have to be sent immediately
		// The TurnMessage for CardDrawing isn't set in the NextGameStateAndMessages object
		for (String nickname : messages.keySet()) {
			if (messages.get(nickname) != null && !messages.get(nickname).isEmpty()) {
				int j = 0;
				try {
					for (j = 0; j < messages.get(nickname).size(); j++) {
						if (messages.get(nickname).get(j).accept(new MessageVisitorCheckerResilience()) &&
								messages.get(nickname).get(j) != null){
							putLastMessage(nickname, messages.get(nickname).get(j));
						}

						VirtualView view = virtualViews.get(nickname);
						if (view != null)
							view.sendPremadeMessage(messages.get(nickname).get(j));
						else
							eliminatedVirtualViews.get(nickname).sendPremadeMessage(messages.get(nickname).get(j));
					}
				} catch (IOException e) {
					throw new RuntimeException(e + "\nFailed to send message of type " +
							messages.get(nickname).get(j).getKind() + " to player with nickname " +
							nickname);
				}
			}
		}

		if (nextGameStateAndMessages.getNextGameState() instanceof CardDrawing) {
			try {
				endCardAndCheckForfeit();
				if (!virtualViews.isEmpty()) {
					putLastMessage(players.getFirst().getNickname(), new TurnMessage());
					for (int i = 1; i < players.size(); i++)
						putLastMessage(players.get(i).getNickname(), new WaitForOthersTurns());
					virtualViews.get(players.getFirst().getNickname()).notifyTurn();
				} else {
					//since every player has been eliminated, send the last draw card instruction to whoever the first view
					// is, it doesn't matter. We can't use players.getFirst() because players is empty as well
					for (VirtualView view : eliminatedVirtualViews.values()) {
						view.notifyTurn();
						break;
					}
				}
			} catch (IOException e) {
				System.out.println("Error in updating the players to eliminate");
			}

		}
	}

	/**
	 * Method used to find the index of the player, who has the same message nickname
	 *
	 * @param message current message
	 * @return player index
	 */
	private int findSender(Message message) {
		int senderIndex = -1;
		if (players == null) {
			return 0;
		} else {
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).getNickname().equals(message.getNickname())) {
					senderIndex = i;
					break;
				}
			}
			if (senderIndex == -1 && !weAreInTheEndGameNow) {
				ArrayList<String> names = new ArrayList<>();
				for (ShipDashboard p : players) {
					names.add(p.getNickname());
				}
				throw new NoSuchPlayerException("There is no player with the nickname " + message.getNickname() + " found in the message. The players are:\n" + names);
			}
			return senderIndex;
		}
	}

	/**
	 * It searches the sender index for the first part of ship construction, when the list of
	 * players is not ready. After the construction it calls the previous methode
	 *
	 * @param message current message
	 * @see #findSender(Message)
	 */
	private void updateSenderIndex(Message message) {
		if (beginning) {
			int j = 0;
			ArrayList<String> nicknames = new ArrayList<>(virtualViews.keySet());
			for (String name : nicknames) {
				if (name.equals(message.getNickname())) {
					senderIndex = j;
					break;
				}
				j++;
			}
		} else senderIndex = findSender(message);
	}

	/**
	 * Setter of beginning
	 *
	 * @param value true if the game has surpassed the ship construction
	 */
	public void setBeginning(boolean value) {
		this.beginning = value;
	}

	/**
	 * Method used during socket connection in order to handle and dispatch the different methods,
	 * RMI calls directly controller methods
	 *
	 * @param message current message to handle
	 * @throws IOException input output exception thrown
	 */
	public void handleMessageController(Message message) throws IOException {
		updateSenderIndex(message);
		boolean ignoreCardRequest = (message instanceof DrawCardRequestMessage) && !(state instanceof CardDrawing);

		if (message instanceof ResilienceResponseMessage newMessage) {
			setResilience(newMessage.getNickname(), newMessage.getShip());
			return;
		}

		if (message instanceof NotifyActionCompleted msg) {
			handleActionCompletedNotification(msg);
		} else if(ignoreCardRequest) {
			return;
		}
		else if (!state.checkMessage(new ControllerStateCheckerVisitor(this, message))) {
			System.out.println("Unexpected message of type " + message.getKind().name() + " while game state was " + state + ".");
		}

		if(!(state instanceof ShipConstructionState || state instanceof   ToBeFixedAndFixingShips)) {
			for(String nickname : lostPlayers) {
				putLastMessage(nickname, new WaitForOthersTurns());
			}
		}

		state.accept(new ControllerStateVisitor(this, message));
		//else
		//notify all the clientHandlers of the state change
	}

	/**
	 * Method used to signal to every player the current position of the players
	 *
	 * @param printPositions true if the positions can be printed
	 */
	private void sendPositionsAndTurns(boolean printPositions) {
		for (VirtualView view : virtualViews.values()) {
			view.sendPositionsAndTurns(players, printPositions);
		}
	}

	/**
	 * Methode used to eliminate a player from the game and put them in the list of eliminated
	 * players
	 *
	 * @param playerIndex player index
	 */
	private void eliminatePlayer(int playerIndex) {
		eliminatedVirtualViews.put(players.get(playerIndex).getNickname(), virtualViews.get(players.get(playerIndex).getNickname()));

		virtualViews.remove(players.get(playerIndex).getNickname());


		eliminatedPlayers.add(players.get(playerIndex));
		players.remove(players.get(playerIndex));
	}

	/**
	 * The players get separated in "newPlayers", if they haven't
	 * been eliminated, and "eliminatedPlayers" otherwise.
	 * The hypothesis is that the indexes are sorted from minimum to maximum
	 *
	 * @param playersToRemoveIndex list of players indexes to remove
	 */
	private void eliminatePlayers(ArrayList<Integer> playersToRemoveIndex) {
		ArrayList<ShipDashboard> newPlayers = new ArrayList<>();
		int playerCursor = 0;
		int i = 0;
		while (i < players.size()) {
			if (playerCursor < playersToRemoveIndex.size() && i == playersToRemoveIndex.get(playerCursor)) {
				String playerNickname = players.get(i).getNickname();
				eliminatedVirtualViews.put(playerNickname, virtualViews.get(playerNickname));
				virtualViews.remove(playerNickname);
				eliminatedPlayers.add(players.get(i));
				playerCursor++;
			} else {
				newPlayers.add(players.get(i));
			}
			i++;
		}
		players = newPlayers;
	}

	/**
	 * Methode used after the effect of a card, in order to update positions and check
	 * possible players to remove
	 *
	 * @ input output exception thrown
	 */
	private void endCardAndCheckForfeit() throws IOException {
		//we check here for eliminations due to 0 motor power in open space
		if (virtualViews.isEmpty()) {
			sendWinners();
			return;
		}

		game.updatePositions(players);


		//management of forfeit for insufficient crew
		ArrayList<Integer> playersToRemoveIndex = game.checkForfeitForInsufficientCrew(players);
		//notify the players that they can't continue the game
		for (Integer i : playersToRemoveIndex) {
			putLastMessage(players.get(i).getNickname(), new GenericMessage("You don't have enough crew mates to continue! Your trip ends here :("));
			virtualViews.get(players.get(i).getNickname()).sendGenericMessage("You don't have enough crew mates to continue! Your trip ends here :(");
		}
		//removing the eliminated players from the VirtualViews and updating VirtualPlayers
		eliminatePlayers(playersToRemoveIndex);
		//we check here for the eliminations that might just now have been applied
		if (virtualViews.isEmpty())
			sendWinners();
		if (players.isEmpty()) return;

		//management of forfeit due to being doubled by the leader
		playersToRemoveIndex = game.checkForfeitForDoubledPosition(players);
		//notify the players that they can't continue the game
		for (Integer i : playersToRemoveIndex) {
			putLastMessage(players.get(i).getNickname(), new GenericMessage("You have been doubled by the leader! Your trip ends here :("));
			virtualViews.get(players.get(i).getNickname()).sendGenericMessage("You have been doubled by the leader! Your trip ends here :(");
		}
		//removing the eliminated players from the VirtualViews and updating VirtualPlayers
		eliminatePlayers(playersToRemoveIndex);

		//we check here for the eliminations that might just now have been applied
		if (virtualViews.isEmpty())
			sendWinners();
	}

	/**
	 * It checks if a certain player is the last one
	 *
	 * @param playerIndex current player index
	 * @return true if the current player is the last one
	 */
	private Boolean isLastPlayer(int playerIndex) {
		return (playerIndex == this.players.size() - 1);
	}

	/**
	 * It checks if there are free planets or if the player is the last one, in this case
	 * it's called the check forfeit methode, otherwise it's the turn of the next player
	 *
	 * @param planetsCard current planets card
	 * @throws IOException input output exception thrown
	 */
	private void PLANETS_checkIfCardEndsOrNotifyNextPlayer(Planets planetsCard) throws IOException {
		if (planetsCard.getFreePlanets() == null || isLastPlayer(senderIndex)) {
			endCardAndCheckForfeit();
			changeGameStateAndNotifyClients(new CardDrawing());
			putLastMessage(players.getFirst().getNickname(), new TurnMessage());
			virtualViews.get(players.getFirst().getNickname()).notifyTurn();
		}
		//otherwise it is the turn of the next player
		else {
			changeGameStateAndNotifyClients(new PLANETS_LandingChoice());
			//notify turn not needed, this message serves that purpose as well
			putLastMessage(players.get(senderIndex + 1).getNickname(), new FreePlanetsResponseMessage(planetsCard.getFreePlanets()));
			virtualViews.get(players.get(senderIndex + 1).getNickname()).sendFreePlanetsIndex(planetsCard.getFreePlanets());
		}
		persistence.save(this);
	}

	/**
	 * It handles the firepower choice of the cards which require it like Smuggler, Pirates,
	 * Slavers and Combat Zone. The different cards are dispatched and handled inside the
	 * visitor object
	 *
	 * @param message        current message
	 * @param RMI_connection true if the methode is called directly from an RMI connection,
	 *                       in this case the sender index is updated inside the methode
	 * @see CardVisProg_dynamicFirePower
	 */
	public void handleFirePowerChoice(Message message, boolean RMI_connection) {
		if (RMI_connection) updateSenderIndex(message);
		NextGameStateAndMessages nextGameStateAndMessages;
		switch (message) {
			case DynamicFirePowerMessage dynamicFirePowerMessage:
				double staticFirePower = players.get(senderIndex).getStaticFirePower();
				double dynamicFirePower = dynamicFirePowerMessage.getDynamicFirePower();
				double firePower = staticFirePower + dynamicFirePower;
				players.get(senderIndex).updateShip(dynamicFirePowerMessage.getUpdatedShip());

				CardVisProg_dynamicFirePower firePower_visitor = new CardVisProg_dynamicFirePower(game, players, message, new ArrayList<>(virtualViews.keySet()), staticFirePower, dynamicFirePower, firePower);
				nextGameStateAndMessages = curCard.apply(firePower_visitor);
				changeGameStateAndSendMessagesToClients(nextGameStateAndMessages);

				break;
			case UpdateServerShipMessage updateServerShipMessage:
				players.get(senderIndex).updateShip(updateServerShipMessage.getUpdatedShip().getShip());
				break;
			case NotifyActionCompleted _:
				break;
			default:
				throw new UnexpectedMessageException("The message received of type " + message.getKind() + " was not expected in the current state: " + this.state);
		}
		persistence.save(this);
	}

	/**
	 * It handles the claim reward choice of the different enemy cards, the effect of the card
	 * is handled by a visitor object which contains the logic
	 *
	 * @param message        current message
	 * @param RMI_connection true if the methode is called directly from an RMI connection,
	 *                       in this case the sender index is updated inside the methode
	 * @see CardVisProg_claimRewardChoice
	 */
	public void handleClaimRewardChoice(Message message, boolean RMI_connection) {
		if (RMI_connection) updateSenderIndex(message);
		NextGameStateAndMessages nextGameStateAndMessages;
		CardVisProg_claimRewardChoice claimReward_visitor = new CardVisProg_claimRewardChoice(game, players, message, new ArrayList<>(virtualViews.keySet()));
		nextGameStateAndMessages = curCard.apply(claimReward_visitor);
		changeGameStateAndSendMessagesToClients(nextGameStateAndMessages);
		persistence.save(this);
	}

	/**
	 * Methode used to return some stocks in case of a discard stocks message and to update
	 * a player ship after the update server ship message
	 *
	 * @param message        current message
	 * @param RMI_connection true if the methode is called directly from an RMI connection,
	 *                       in this case the sender index is updated inside the methode
	 * @throws IOException input output exception thrown
	 * @see UpdateServerShipMessage
	 * @see DiscardStocksMessage
	 */
	public void handleAddAndRearrangeStocks(Message message, boolean RMI_connection) throws IOException {
		if (RMI_connection) updateSenderIndex(message);
		switch (message) {
			case UpdateServerShipMessage updateServerShipMessage:
				players.get(senderIndex).updateShip(updateServerShipMessage.getUpdatedShip().getShip());
				if (Objects.requireNonNull(curCard) instanceof Planets planetsCard) {
					if (planetsCard.getFreePlanets() != null && !isLastPlayer(senderIndex)) {
						changeGameStateAndNotifyClients(new PLANETS_LandingChoice());
						//notify turn not needed, this message serves that purpose as well
						putLastMessage(players.get(senderIndex + 1).getNickname(), new FreePlanetsResponseMessage(planetsCard.getFreePlanets()));
						virtualViews.get(players.get(senderIndex + 1).getNickname()).sendFreePlanetsIndex(planetsCard.getFreePlanets());
						break;
					}
					//else
				}
				//else
				endCardAndCheckForfeit();
				changeGameStateAndNotifyClients(new CardDrawing());
				putLastMessage(players.getFirst().getNickname(), new TurnMessage());
				virtualViews.get(players.getFirst().getNickname()).notifyTurn();
				break;

			case DiscardStocksMessage discardStocksMessage:
				game.returnStocks(discardStocksMessage.getStocksToDiscard());
				break;
			case NotifyActionCompleted _:
				break;
			default:
				throw new UnexpectedMessageException("The received message's type " + message.getKind() + " was unexpected when state was " + this.state);
		}
		persistence.save(this);
	}

	/**
	 * Methode used to handle the give up choice according to the cards which require it,
	 * the different card effects are handled using a visitor object
	 *
	 * @param message        current message
	 * @param RMI_connection true if the methode is called directly from an RMI connection,
	 *                       in this case the sender index is updated inside the methode
	 */
	public void handleGiveUpCrewChoice(Message message, boolean RMI_connection) {
		if (RMI_connection) updateSenderIndex(message);
		NextGameStateAndMessages nextGameStateAndMessages = null;
		if (message instanceof UpdateServerShipMessage) {
			CardVisProg_giveUpCrewChoice crewChoice_visitor = new CardVisProg_giveUpCrewChoice(game, players, message, new ArrayList<>(virtualViews.keySet()));
			nextGameStateAndMessages = curCard.apply(crewChoice_visitor);
		}
		changeGameStateAndSendMessagesToClients(nextGameStateAndMessages);
		persistence.save(this);
	}

	/**
	 * Methode used to handle manage projectile according to the cards which require it,
	 * the different card effects are handled using a visitor object
	 *
	 * @param message        current message
	 * @param RMI_connection true if the methode is called directly from an RMI connection,
	 *                       in this case the sender index is updated inside the methode
	 */
	public void handleManageProjectile(Message message, boolean RMI_connection) {
		if (RMI_connection) updateSenderIndex(message);
		if (Objects.requireNonNull(message) instanceof UpdateServerShipMessage updateServerShipMessage) {
			players.get(senderIndex).updateShip(updateServerShipMessage.getUpdatedShip().getShip());

			NextGameStateAndMessages nextGameStateAndMessages;
			CardVisProg_manageProjectile manageProjectile_visitor = new CardVisProg_manageProjectile(game, players, updateServerShipMessage, new ArrayList<>(virtualViews.keySet()));
			nextGameStateAndMessages = curCard.apply(manageProjectile_visitor);
			changeGameStateAndSendMessagesToClients(nextGameStateAndMessages);

		} else if (message.getKind() != MessageKind.NOTIFY_ACTION_COMPLETED) {
			throw new WrongCardException("The type message received was not expected in the current state: " + this.state);
		}
		persistence.save(this);
	}

	/**
	 * In case of a planet land request, it applies the effect of the current planets card,
	 * if there are no available planets, it's called the methode to check if the card ends
	 *
	 * @param message        current message
	 * @param RMI_connection true if the methode is called directly from an RMI connection,
	 *                       in this case the sender index is updated inside the methode
	 * @throws IOException input output exception thrown
	 * @see #PLANETS_checkIfCardEndsOrNotifyNextPlayer(Planets)
	 */
	public void handlePLANETS_LandingChoice(Message message, boolean RMI_connection) throws IOException {
		if (RMI_connection) updateSenderIndex(message);
		if (message instanceof PlanetLandRequestMessage planetLandRequestMessage && curCard instanceof Planets planetsCard) {
			int planetId = planetLandRequestMessage.getPlanetId();
			//the player doesn't want to land on any planet
			if (planetId <= -1) {
				//if there aren't free planets anymore, or the currPlayer is the last one, the card ends
				PLANETS_checkIfCardEndsOrNotifyNextPlayer(planetsCard);
			} else {
				planetsCard.land(planetId, players.get(senderIndex));
				changeGameStateAndNotifyClients(new AddAndRearrangeStocks());
				//notify turn not needed, this message serves that purpose as well
				putLastMessage(players.get(senderIndex).getNickname(), new StocksToAddMessage(planetsCard.getStocks(planetId)));
				virtualViews.get(players.get(senderIndex).getNickname()).sendStocksToAdd(planetsCard.getStocks(planetId));
			}
		} else if (!message.getKind().equals(MessageKind.PLANET_LAND_REQUEST) && message.getKind() != MessageKind.NOTIFY_ACTION_COMPLETED) {
			throw new WrongCardException("The card wasn't a Planets type when the state was PLANETS_LandingChoice.");
		}
		persistence.save(this);
	}

	/**
	 * It applies the docking choice for abandoned ship and abandoned station according
	 * to the visitor pattern
	 *
	 * @param message        current message
	 * @param RMI_connection true if the methode is called directly from an RMI connection,
	 *                       in this case the sender index is updated inside the methode
	 * @see CardVisProg_dockingChoice
	 */
	public void handleDockingChoice(Message message, boolean RMI_connection) {
		if (RMI_connection) updateSenderIndex(message);
		if (message instanceof CardActivationRequestMessage) {
			NextGameStateAndMessages nextGameStateAndMessages;
			CardVisProg_dockingChoice dockingChoice_visitor = new CardVisProg_dockingChoice(game, players, message, new ArrayList<>(virtualViews.keySet()));
			nextGameStateAndMessages = curCard.apply(dockingChoice_visitor);
			changeGameStateAndSendMessagesToClients(nextGameStateAndMessages);
			persistence.save(this);
		}
	}

	/**
	 * It handles the motor power choice for open space and combat zone, using the visitor
	 * pattern. In case of an update server ship, it updates the ship
	 *
	 * @param message        current message
	 * @param RMI_connection true if the methode is called directly from an RMI connection,
	 *                       in this case the sender index is updated inside the methode
	 */
	public void handleMotorPowerChoice(Message message, boolean RMI_connection) {
		if (RMI_connection) updateSenderIndex(message);
		switch (message) {
			case DynamicMotorPowerMessage dynamicMotorPowerMessage:
				int staticMotorPower = players.get(senderIndex).getStaticMotorPower();
				int dynamicMotorPower = dynamicMotorPowerMessage.getDynMotorPower();
				int motorPower = staticMotorPower + dynamicMotorPower;
				NextGameStateAndMessages nextGameStateAndMessages;
				CardVisProg_dynamicMotorPower dynamicMotorPower_visitor =
						new CardVisProg_dynamicMotorPower(game, players, message, new ArrayList<>(virtualViews.keySet()),
								//callback if motor power is 0
								staticMotorPower, dynamicMotorPower, motorPower, this::eliminatePlayer);

				nextGameStateAndMessages = curCard.apply(dynamicMotorPower_visitor);
				changeGameStateAndSendMessagesToClients(nextGameStateAndMessages);

				break;
			case UpdateServerShipMessage updateServerShipMessage:
				players.get(senderIndex).updateShip(updateServerShipMessage.getUpdatedShip().getShip());
				break;
			case NotifyActionCompleted _:
				break;
			default:
				throw new WrongCardException("The message of type " + message.getKind() + " received was not expected in the current state: " + this.state);
		}
		persistence.save(this);
	}

	/**
	 * The number of players requested is saved in the game, when it's valid.
	 * If the number is not valid, the server notifies the client, who will be asked to
	 * insert another number
	 *
	 * @param message        current message
	 * @param RMI_connection true if the methode is called directly from an RMI connection,
	 *                       in this case the sender index is updated inside the methode
	 * @throws InvalidNumPlayersRequestedException thrown when the number of players is not allowed
	 * @throws IOException                         input output exception thrown
	 */
	public void handleInitializationState(Message message, boolean RMI_connection) throws InvalidNumPlayersRequestedException, IOException {
		if (RMI_connection) updateSenderIndex(message);

		if (message instanceof PlayersNumResponseMessage currMessage) {
			int numPlayersRequested = currMessage.getNumPlayersRequested();
			if (this.numPlayers == -1) {

				this.numPlayers = numPlayersRequested;
				fixedShips = new ConcurrentHashMap<>();
				System.out.println("Number of players for this game: " + numPlayersRequested);
				putLastMessage(currMessage.getNickname(),
						new GenericMessage("\nExpected players: " + numPlayers + "\nCurrent players: " + virtualViews.size()));
				checkWaitingForPlayers();
			}
			//theoretically not necessary
			else {
				virtualViews.get(message.getNickname()).sendGenericMessage(
						"A player quicker than you has stolen your right to choose the number of players and picked " + numPlayers + ".");
				checkWaitingForPlayers();
			}
		} else if (weAreInTheEndGameNow && message instanceof EndGameMessage) {

		} else
			System.out.println("Invalid message received: expected PlayersNumResponseMessage. Received: " + message.getKind());
		persistence.save(this);
	}

	/**
	 * When the timer is expired, all the clients are notified
	 *
	 * @param isLast true if the timer is the last one
	 */
	private void notifyClientsOnTimerExpired(boolean isLast) {
		for (VirtualView view : virtualViews.values()) {
			try {
				view.timerExpired(isLast);
			} catch (IOException e) {
				throw new RuntimeException("Problem while communicating to clients timer expiration.");
			}
		}
	}

	/**
	 * Method used to handle the case of more players receiving the number player request message,
	 * in order to notify the players that have received the question, who are not the first player, that they are not allowed to choose the number of players
	 *
	 * @param message is the current message received from the client
	 */
	public void handlePlayerNumChoiceStolen(Message message) {
		virtualViews.get(message.getNickname()).sendGenericMessage(
				"A player quicker than you has stolen your right to choose the number of players and picked " + numPlayers + ".");
	}

	/**
	 * The method is called by the server each time a new player
	 * logs in and updates the views of the clients
	 *
	 * @throws IOException input output exception thrown
	 */
	public void checkWaitingForPlayers() throws IOException {
		if (!weAreInTheEndGameNow) {
			if (virtualViews.size() < numPlayers) {
				changeGameStateAndNotifyClients(new WaitingForPlayers());
				for (VirtualView view : virtualViews.values()) {
					view.waitingPartners();
					view.sendGenericMessage("\nExpected players: " + numPlayers + "\nCurrent players: " + virtualViews.size());
				}
				persistence.save(this);
				return;
			}
		} else {
			if (newGameVirtualViews.size() < numPlayers) {
				changeGameStateAndNotifyClients(new WaitingForPlayers());
				for (VirtualView view : newGameVirtualViews.values()) {
					view.waitingPartners();
					view.sendGenericMessage("\nExpected players: " + numPlayers + "\nCurrent players: " + newGameVirtualViews.size());
				}
				persistence.save(this);
				return;
			}
		}
		//if the method didn't return early, so if virtualViews / newGameVirtualViews .size == numPlayers then vvvv
		//this.loneliness();
		if (weAreInTheEndGameNow) {
			virtualViews = newGameVirtualViews;
		}
		this.blockGame = true;
		for (VirtualView view : virtualViews.values()) {
			ArrayList<String> nicknames = new ArrayList<>(virtualViews.keySet());
			view.begin(nicknames);
		}
		allPlayers = new ArrayList<>(virtualViews.keySet());

		weAreInTheEndGameNow = false;
		controllerWasReset = false;
		changeGameStateAndNotifyClients(new ShipConstructionState());
		for (Map.Entry<String, Message> entry : lastMessages.entrySet()) {
			entry.setValue(new TurnMessage());
		}

		game.startFirstTimer(this::notifyClientsOnTimerExpired);
		ArrayList<Integer> availableCardPiles = getAvailableDecksIndexes();
		for (VirtualView view : virtualViews.values()) {
			view.sendAvailableSmallDecks(availableCardPiles);
			view.notifyTurn();
		}
		persistence.save(this);
	}

	/**
	 * Getter of the available deck indexes
	 *
	 * @return list of available deck indexes
	 */
	private ArrayList<Integer> getAvailableDecksIndexes() {
		ArrayList<Integer> freeDecks = new ArrayList<>();
		for (int i = 0; i < game.getDecks().size(); i++) {
			if (!game.getDecks().get(i).isUsed()) {
				freeDecks.add(i);
			}
		}

		return freeDecks;
	}

	/**
	 * It checks if the player has an associated ship
	 *
	 * @param nickname player nickname
	 * @return true if the player has an associated ship
	 */
	boolean didPlayerFinishShip(String nickname) {
		if (players == null) {
			return false;
		}

		for (ShipDashboard player : players) {
			if (player.getNickname().equals(nickname))
				return true;
		}

		return false;
	}

	/*when this state is called, the server waits for one of the players to request that the hourglass starts running.
	 * After the first request, the server updates the HourglassRound, and pass it to the controller's method. */

	/**
	 * Methode used to dispatch and handle the different messages for ship construction according
	 * to visitor pattern
	 *
	 * @param message        current message
	 * @param RMI_connection true if the methode is called directly from an RMI connection,
	 *                       in this case the sender index is updated inside the methode
	 * @throws UnexpectedMessageException thrown when it's received an unexpected message
	 */
	public void handleShipConstructionState(Message message, boolean RMI_connection) throws UnexpectedMessageException {
		if (RMI_connection) updateSenderIndex(message);
		int deckID;
		Deck selectedCardPile;
		ShipComponent requestedComponentById;
		//GameTimer timer = game.getTimer();


		NextGameStateAndMessages nextGameStateAndMessages;
		MsgVisProg_shipConstructionState shipConstruction_visitor =
				new MsgVisProg_shipConstructionState(game, players, playersBuilt, this::notifyClientsOnTimerExpired, numPlayers, new ArrayList<>(virtualViews.keySet()));


		nextGameStateAndMessages = message.accept(shipConstruction_visitor);

		changeGameStateAndSendMessagesToClients(nextGameStateAndMessages);
		persistence.save(this);
	}

	/**
	 * It checks if every ship is well fixed
	 *
	 * @return true if every ship is well fixed
	 */
	private boolean allShipsAreFixed() {
		for (String n : players.stream().map(ShipDashboard::getNickname).toList()) {
			if (fixedShips.get(n) == null || !fixedShips.get(n))
				return false;
		}

		return true;
	}

	/**
	 * Removes the last message sent to the client with nickname because the action related to it has been completed,
	 * and replaces it with the appropriate one to avoid repetition and errors
	 *
	 * @param message message from the client that has completed an action
	 */
	public void handleActionCompletedNotification(NotifyActionCompleted message) {
		putLastMessage(message.getNickname(), message.getReplacingMessage());
	}

	/**
	 * With the place ship request, the ship is stored in the list of player ships
	 * and then the state is updated, notifying the clients
	 *
	 * @param message        current message
	 * @param RMI_connection true if the methode is called directly from an RMI connection,
	 *                       in this case the sender index is updated inside the methode
	 * @throws IOException input output exception thrown
	 */
	public void handleToBeFixedAndFixingShips(Message message, boolean RMI_connection) throws IOException {
		if (RMI_connection) updateSenderIndex(message);


		if (message.getKind() == MessageKind.PLACE_SHIP_REQUEST) {
			PlaceShipRequestMessage currMessage = (PlaceShipRequestMessage) message;

			for (int i = 0; i < this.players.size(); i++) {
				if (message.getNickname().equals(this.players.get(i).getNickname())) {
					this.players.set(i, currMessage.getShip());
					fixedShips.put(message.getNickname(), true);
				}
			}

			if (allShipsAreFixed()) {
				game.updatePositions(players);
				changeGameStateAndNotifyClients(new CardDrawing());
				putLastMessage(players.getFirst().getNickname(), new TurnMessage());
				virtualViews.get(players.getFirst().getNickname()).notifyTurn();
			}

		} else if (message.getKind() != MessageKind.NOTIFY_ACTION_COMPLETED)
			System.out.println("Invalid message received: expected PlaceShipRequest. Received: " + message.getKind());
		persistence.save(this);
	}

	/**
	 * With the draw card request it's drawn a card. With no cards left, it's
	 * time to reach the last part of the game with winners classifies. With a card
	 * left it's time to apply the effect with visitor
	 *
	 * @param message        current message
	 * @param RMI_connection true if the methode is called directly from an RMI connection,
	 *                       in this case the sender index is updated inside the methode
	 */
	public void handleCardDrawing(Message message, boolean RMI_connection) {
		if (RMI_connection) updateSenderIndex(message);
		sendPositionsAndTurns(true);
		if (message.getKind() == MessageKind.DRAW_CARD_REQUEST) {
			turnStartPositions.clear();
			for(ShipDashboard p : players) {
				turnStartPositions.put(p.getNickname(), p.getPosition());
			}
			if (players.getFirst().getNickname().equals(message.getNickname())) {
				curCard = game.getTotalDeck().drawCard();
				//the deck is empty, so the game is finished
				if (curCard == null) {
					sendWinners();
				} else {
					for (VirtualView view : this.virtualViews.values()) {
						view.notifyNewCardDrawn(curCard);
					}
					NextGameStateAndMessages nextGameStateAndMessages;

					CardVisProg_cardDrawingState cardDrawingState_visitor = new CardVisProg_cardDrawingState(game, players, new ArrayList<>(virtualViews.keySet()));
					nextGameStateAndMessages = curCard.apply(cardDrawingState_visitor);
					changeGameStateAndSendMessagesToClients(nextGameStateAndMessages);
					persistence.save(this);
				}
			} else {
				virtualViews.get(message.getNickname()).sendGenericMessage("Only the player in front can draw a card!");
			}
		}
	}

	/**
	 * The methode creates the classify of winners, sending it to every player, also the
	 * ones who have been eliminated
	 */
	private void sendWinners() {
		//all players, both eliminated and not, will have their own points
		System.out.println("\nSending winners\n");
		ArrayList<ShipDashboard> allPlayers = new ArrayList<>();
		allPlayers.addAll(players);
		allPlayers.addAll(eliminatedPlayers);
		ArrayList<ShipDashboard> winners = game.endGame(allPlayers);

		for (VirtualView view : virtualViews.values()) {
			changeGameStateAndNotifyClients(new EndGame());
			view.sendWinners(winners);
		}
		for (VirtualView view : eliminatedVirtualViews.values()) {
			changeGameStateAndNotifyClients(new EndGame());
			view.sendWinners(winners);
		}
		blockGame = false;
		persistence.deleteSavedFile();
		//the view will display the points gained for each player
	}

	/**
	 * Method used to reset the controller at the end of a game
	 */
	private void resetController() {
		System.out.println(ConsoleColor.TEXT_BLUE + "CONTROLLER RESET" + ConsoleColor.RESET);
		controllerWasReset = true;
		this.state = new InitializationState();
		persistence = Persistence.getInstance();
		this.blockGame = false;
		beginning = true;
		numPlayers = -1;
		Deck gameDeck = new Deck((ArrayList<Card>) this.cards.clone(), 20, 20);
		Bank bank = new Bank(68, 40, 5, 10, 20, 20);
		game = new Game(bank, 24, gameDeck, shipComponents);
		game.initializeDecks();
		newGameVirtualViews = new ConcurrentHashMap<>();
		activation = false;
		resilientActivation.clear();
		eliminatedPlayers.clear();
		players = null;
		playersBuilt = new ArrayList<>();
		eliminatedVirtualViews = new ConcurrentHashMap<>();
		lastMessages.clear();
	}

	/**
	 * Method used to handle the end game message, putting the players in a new game if
	 * they want or making them reach the game over
	 *
	 * @param message        current message
	 * @param RMI_connection true if the methode is called directly from an RMI connection,
	 *                       in this case the sender index is updated inside the methode
	 */
	public void handleEndGame(Message message, boolean RMI_connection) {
		//not needed, causes exception after disconnection
		//	if(RMI_connection) updateSenderIndex(message);
		if (message.getKind() == MessageKind.END_GAME_REQUEST) {
			EndGameMessage endGameMessage = (EndGameMessage) message;
			int choice = endGameMessage.getChoice();
			if (!weAreInTheEndGameNow) {
				if (choice == 1) {
					stopResilience();
					disconnectPlayer(message.getNickname());
				}
				if (choice == 2) {
					resetController();
					weAreInTheEndGameNow = true;
				}
			}
			//else the player is put in another game, if present, or it gets created another game if there isn't one
		} else if (message.getKind() != MessageKind.NOTIFY_ACTION_COMPLETED)
			System.out.println("Invalid message received: expected PlaceShipRequest. Received: " + message.getKind());
	}

	//for testing

	/**
	 * Getter of the map of players with related virtual view, used for testing
	 *
	 * @return map of players with related virtual view
	 */
	public ConcurrentHashMap<String, VirtualView> getVirtualViews() {
		return virtualViews;
	}

	/**
	 * Getter of the current sender index, used for testing
	 *
	 * @return sender index
	 */
	public int getSenderIndex() {
		return senderIndex;
	}

	/**
	 * Setter of the block game, used for testing
	 *
	 * @param blockGame true if the game has to be blocked
	 */
	void setBlockGame(boolean blockGame) {
		this.blockGame = blockGame;
	}

	/**
	 * Getter of the list of lost players, used for testing
	 *
	 * @return list of lost players
	 */
	public ArrayList<String> getLostPlayers() {
		return lostPlayers;
	}

	/**
	 * Setter of the game state, used for testing
	 *
	 * @param state game state
	 */
	public void setGameState(GameState state) {
		this.state = state;
	}

	/**
	 * Getter of the game state
	 *
	 * @return game state
	 */
	public GameState getGameState() {
		return this.state;
	}

	/**
	 * Getter of the resilient activation, used for testing
	 *
	 * @return map of players nicknames with current activation status
	 */
	public Map<String, Boolean> getResilientActivation() {
		return resilientActivation;
	}

	/**
	 * Setter of the resilient activation, used for testing
	 *
	 * @param resilientActivation true if the resilience has started
	 */
	public void setResilientActivation(boolean resilientActivation) {
		this.activation = resilientActivation;
	}

	/**
	 * Getter of resilience activation
	 *
	 * @return true if resilience has started
	 */
	public boolean getActivation() {
		return activation;
	}

	/**
	 * Setter of the task handle for resilience, it's used for testing
	 *
	 * @param taskHandle current task handle
	 */
	public void setTaskHandle(ScheduledFuture taskHandle) {
		this.taskHandle = taskHandle;
	}

	/**
	 * Setter of the current card, used for testing
	 *
	 * @param curCard current card
	 */
	public void setCurCard(Card curCard) {
		this.curCard = curCard;
	}

	/**
	 * It searches a player to be eliminated for testing
	 *
	 * @param nickname player nickname
	 */
	public void eliminatePlayerTester(String nickname) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getNickname().equals(nickname)) {
				eliminatePlayer(i);
				return;
			}
		}
	}

	/**
	 * It calls directly the eliminate players method for testing
	 *
	 * @param indexes list of players indexes
	 * @see #eliminatePlayers(ArrayList)
	 */
	public void eliminatePlayersIndex(ArrayList<Integer> indexes) {
		eliminatePlayers(indexes);
	}

	/**
	 * Getter of persistence, used for testing
	 *
	 * @return current persistence
	 */
	public Persistence getPersistence() {
		return this.persistence;
	}

	/**
	 * Setter of persistence, used for testing
	 *
	 * @param persistence current persistence
	 */
	public void setPersistence(Persistence persistence) {
		this.persistence = persistence;
	}

	/**
	 * It calls directly the notify clients on timer expired for testing
	 *
	 * @param isLast true if it's the last timer
	 * @see #notifyClientsOnTimerExpired(boolean)
	 */
	public void notifyClientsTimerExpiredTest(boolean isLast) {
		notifyClientsOnTimerExpired(isLast);
	}

	/**
	 * Setter of the game, used for testing
	 *
	 * @param game current game
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * Setter of the list of current players: used for testing
	 *
	 * @param allPlayers list of current players
	 */
	public void setAllPlayers(ArrayList<String> allPlayers) {
		this.allPlayers = allPlayers;
	}

	/**
	 * Getter of lost player ships: used for testing
	 *
	 * @return list of lost player ships
	 */
	public ConcurrentHashMap<String, ShipDashboard> getLostPlayersShip() {
		return lostPlayersShip;
	}

	/**
	 * Getter of last message: used for testing
	 *
	 * @return map of strings and messages
	 */
	public ConcurrentHashMap<String, Message> getLastMessage() {
		return lastMessages;
	}

	/**
	 * It calls directly remove player by nickname for testing
	 *
	 * @param nickname of player to remove
	 * @see #removePlayerByNickname(String)
	 */
	public void removePlayerByNick(String nickname) {
		removePlayerByNickname(nickname);
	}

	/**
	 * It calls directly endCardAndCheckForfeit for testing
	 *
	 * @see #endCardAndCheckForfeit()
	 */
	public void testEndCardAndCheckForfeit() throws IOException {
		endCardAndCheckForfeit();
	}

	/**
	 * Getter of players array, used for testing
	 *
	 * @return players
	 */
	public ArrayList<ShipDashboard> getPlayers() {
		return players;
	}

	/**
	 * Setter of the list of players: used for testing
	 *
	 * @param players list of ShipDashboards
	 */
	public void setPlayers(ArrayList<ShipDashboard> players) {
		this.players = players;
	}

	/**
	 * Setter of the fixed list of players: used for testing
	 *
	 * @param fixedShips list of fixed ShipDashboards
	 */
	public void setFixedShips(ConcurrentHashMap<String, Boolean> fixedShips) {
		this.fixedShips = fixedShips;
	}

	/**
	 * Getter of the fixed list of players: used for testing
	 *
	 * @return fixedShips list of fixed ShipDashboards
	 */
	public ConcurrentHashMap<String, Boolean> getFixedShips() {
		return fixedShips;
	}

	/**
	 * Setter of the current card: used for testing
	 *
	 * @param card to set as current
	 */
	public void setCurdCard(Card card) {
		this.curCard = card;
	}

	/**
	 * Getter of the number of players: used for testing
	 *
	 * @return numPlayers that indicates the number of players
	 */
	public int getNumPlayers() {
		return numPlayers;
	}

	/**
	 * Setter of the number of players: used for testing
	 *
	 * @param numPlayers that indicates the number of players
	 */
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	/**
	 * It calls directly updateServerIndex for testing
	 *
	 * @see #updateSenderIndex(Message)
	 */
	public void updateServerIndexTest(Message message) {
		updateSenderIndex(message);
	}

	/**
	 * It calls directly findSeder for testing
	 *
	 * @see #findSender(Message)
	 */
	public int findSenderTest(Message message) {
		return findSender(message);
	}

	/**
	 * It calls directly allShipsAreFixed for testing
	 *
	 * @see #allShipsAreFixed()
	 */
	public boolean testAllShipsAreFixed() {
		return allShipsAreFixed();
	}

	/**
	 * It calls directly sendWinners for testing
	 *
	 * @see #sendWinners()
	 */
	public void sendWinnersTest() {
		sendWinners();
	}

	/**
	 * It calls directly the private method for testing
	 *
	 * @see #changeGameStateAndSendMessagesToClients(NextGameStateAndMessages)
	 */
	public void testChangeGameStateAndSendMessages(NextGameStateAndMessages state) {
		changeGameStateAndSendMessagesToClients(state);
	}

	/**
	 * It calls directly the private method for testing
	 *
	 * @see #PLANETS_checkIfCardEndsOrNotifyNextPlayer(Planets)
	 */
	public void testPLANETSCheckIfCardsEnd(Planets card) throws IOException {
		PLANETS_checkIfCardEndsOrNotifyNextPlayer(card);
	}

	/**
	 * Getter of the ShipDashboards in construction: used for testing
	 *
	 * @return the arrayList of ships.
	 */
	public ArrayList<ShipDashboard> getConstructionShip() {
		return constructionShip;
	}

	/**
	 * It calls directly the private method for testing
	 *
	 * @see #resilienceMessage()
	 */
	public void resilienceMexTest() {
		resilienceMessage();
	}

	/**
	 * Setter of the variable about being in "end Game": used for testing
	 *
	 * @param weAreInTheEndGameNow to set
	 */
	public void setWeAreInTheEndGameNow(boolean weAreInTheEndGameNow) {
		this.weAreInTheEndGameNow = weAreInTheEndGameNow;
	}

	/**
	 * Getter of the variable about being in "end Game": used for testing
	 *
	 * @return weAreInTheEndGameNow
	 */
	public boolean isWeAreInTheEndGameNow() {
		return weAreInTheEndGameNow;
	}

	/**
	 * Getter of new game views: used for testing
	 *
	 * @return newGameVirtualViews
	 */
	public ConcurrentHashMap<String, VirtualView> getNewGameVirtualViews() {
		return newGameVirtualViews;
	}

	/**
	 * Getter of eliminated views : used for testing
	 *
	 * @return eliminatedVirtualViews
	 */
	public ConcurrentHashMap<String, VirtualView> getEliminatedVirtualViews() {
		return eliminatedVirtualViews;
	}

}