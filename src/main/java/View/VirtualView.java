package View;

import Connections.ClientHandler;
import Connections.Messages.*;
import Controller.State.GameState;
import model.Cards.Card;
import model.Deck;
import model.DifferentShipComponents.ShipComponent;
import model.Projectiles.Projectile;
import model.ShipDashboard;
import model.Stocks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Virtual view class used from the controller in order to send to a client the server messages:
 * the class is an implementation of view
 * @see View
 */
public class VirtualView implements View{

	/**
	 * Representation of the client, used in order to interact with them
	 */
	private final ClientHandler client;

	/**
	 * Class constructor, it initializes the client
	 * @param client current client
	 */
	public VirtualView(ClientHandler client, Consumer<String> onDisconnectionCallback, String nickname) {
		this.client = client;
		this.client.setOnDisconnect(onDisconnectionCallback);
		this.client.setNickname(nickname);
	}

	/**
	 * Method used to send a generic message
	 * @param message current generic message
	 */
	@Override
	public void sendGenericMessage(String message) {client.send(new GenericMessage(message));}

	/**
	 * Not used method in the virtual view
	 */
	@Override
	public void askingNickname() {}

	/**
	 * Method used to send a player number request message
	 */
	@Override
	public void numberOfPlayers() {client.send(new PlayersNumRequestMessage());}

	/**
	 * Method used to send a waiting partners message
	 */
	@Override
	public void waitingPartners() {client.send(new WaitingPartnersMessage());}

	/**
	 * Method used to send the beginning message
	 * @param players list of players, they will be printed in order to show to the players
	 *                their partners
	 */
	@Override
	public void begin(ArrayList<String> players) {client.send(new BeginMessage(players));}

	/**
	 * Method used to send a log response message
	 * @param isConnectedToServer true if the connection has been established
	 * @param nicknameStatus true if the nickname is legit
	 * @param playerNumber current amount of players
	 */
	public void loginResponse(boolean isConnectedToServer, boolean nicknameStatus, int playerNumber) {client.send(new LogResponseMessage(isConnectedToServer,nicknameStatus, playerNumber));}

	/**
	 * Method used to send a timer expired message
	 * @param isLast true if the timer has been expired
	 * @throws IOException input output exception thrown
	 */
	public void timerExpired(boolean isLast) throws IOException {client.send(new TimerExpiredMessage(isLast));}

	/**
	 * Method used to send a timer started message
	 * @param seconds amount of seconds
	 * @param isLast true if it's the last timer
	 */
	public void timerStarted(int seconds, boolean isLast) {client.send(new TimerStartedMessage(seconds, isLast));}


	/**
	 * Not used method
	 * @deprecated
	 */
	public void getFinishedShip() {}

	/**
	 * Method used to send a turn message to the client
	 * @throws IOException input output exception thrown
	 */
	@Override
	public void notifyTurn() throws IOException {client.send(new TurnMessage());}

	/**
	 * Method used to send a game state message to the client
	 * @param newGameState current game state
	 */
	@Override
	public void notifyNewGameState(GameState newGameState) {client.send(new GameStateMessage(newGameState));}

	/**
	 * Method used to send a card drawn message
	 * @param card current drawn card
	 */
	@Override
	public void notifyNewCardDrawn(Card card) {client.send(new CardDrawnMessage(card));}

	/**
	 * Method used to send a projectile trajectory message
	 * @deprecated
	 * @param projectile current projectile
	 * @param trajectory current trajectory
	 */
	public void sendProjectileAndTrajectory(Projectile projectile, int trajectory) {client.send(new ProjectileTrajectoryMessage(projectile, trajectory));}

	/**
	 * Method used to send a credit earned message
	 * @deprecated
	 * @param creditsEarned amount of earned credits
	 */
	public void notifyCreditsEarned(int creditsEarned) {client.send(new CreditsEarnedMessage(creditsEarned));}

	/**
	 * Method used to initialize the ship and send an update client ship message
	 * @deprecated
	 * @param ship current ship
	 */
	public void sendUpdatedShipToClient(ShipDashboard ship) {
		ship.initializeShipAttributesFromComponents();
		client.send(new UpdateClientShipMessage(ship));
	}

	/**
	 * Method used to send a stocks to add message
	 * @param stocks current stocks
	 */
	public void sendStocksToAdd(Stocks stocks) {client.send(new StocksToAddMessage(stocks));}

	/**
	 * Method used to send a player number request message
	 */
	public void sendNumPlayersRequest() {client.send(new PlayersNumRequestMessage());}

	/**
	 * Method used to send a free planets response message
	 * @param freePlanetsIndex list of current free planets indexes
	 */
	public void sendFreePlanetsIndex (ArrayList<Integer> freePlanetsIndex) {client.send(new FreePlanetsResponseMessage(freePlanetsIndex));}

	/**
	 * Not used method
	 */
	@Override
	public void winners(ArrayList<ShipDashboard> winners, String nickname) {}

	/**
	 * Method used to send a winner message
	 * @param winners current list of winners
	 */
	public void sendWinners(ArrayList<ShipDashboard> winners) {client.send(new WinnersMessage(winners));}

	/**
	 * Method used to send a positions and turns message
	 * @param players current list of players
	 * @param printPositions true if the positions has to be printed
	 */
	public void sendPositionsAndTurns(ArrayList<ShipDashboard> players, boolean printPositions) {client.send(new PositionsAndTurnsMessage(players, printPositions));}

	/**
	 * Method used to send a resilience request message
	 */
	public void sendResilience() {client.send(new ResilienceRequestMessage());}

	/**
	 * Method used to set and send a resume game message
	 * @param ship current player ship
	 * @param gameState current game state
	 * @param curCard current card
	 * @param lastMessage current last message
	 */
	public void sendResume(ShipDashboard ship, GameState gameState, Card curCard, Message lastMessage) {
		ResumeGameMessage message = new ResumeGameMessage();
		message.setCurCard(curCard);
		message.setShip(ship);
		message.setGameState(gameState);
		message.setLastMessage(lastMessage);
		client.send(message);
	}

	/**
	 * Method used to send a draw component response message
	 * @deprecated
	 * @param component current component
	 */
	public void sendComponent(ShipComponent component) {
		Message newMsg = new DrawComponentResponseMessage(component);
		client.send(newMsg);
	}

	/**
	 * Method used to send a pre-made ship component response message
	 * @deprecated
	 * @param component current component
	 */
	public void sendPremadeShipComponent(ShipComponent component) {
		Message newMsg = new PremadeShipComponentResponse(component);
		client.send(newMsg);
	}

	/**
	 * Method used to send an available small deck message
	 * @param freeSmallDecks list ao free small decks indexes
	 */
	public void sendAvailableSmallDecks(ArrayList<Integer> freeSmallDecks) {client.send(new AvailableSmallDeckMessage(freeSmallDecks));}

	/**
	 * Method used to send a small deck response message
	 * @deprecated
	 * @param deck current deck
	 */
	public void sendSmallDeck(Deck deck, int deckIndex) {client.send(new SmallDeckResponseMessage(deck, deckIndex));}

	/**
	 * Method used to send a revealed components message
	 * @param uncoveredComponents list of current revealed components
	 */
    public void sendRevealedComponents(ArrayList<ShipComponent> uncoveredComponents) {client.send(new RevealedComponentsMessage(uncoveredComponents));}

	/**
	 * Method used to send a pre-made message
	 * @param premadeMessage current pre-made message
	 * @throws IOException input output exception thrown
	 */
	public void sendPremadeMessage(Message premadeMessage) throws IOException {client.send(premadeMessage);}
}
