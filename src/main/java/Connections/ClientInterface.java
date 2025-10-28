package Connections;

import Connections.Messages.*;
import Connections.RMI.ClientRMI;
import Connections.Socket.ClientSocket;
import Controller.State.GameState;
import View.ColorManagement.ConsoleColor;
import View.TUI;
import View.UI;
import model.Cards.*;
import model.Deck;
import model.DifferentShipComponents.ShipComponent;
import model.DifferentShipComponents.UnavailableSlot;
import model.Projectiles.Projectile;
import model.ShipDashboard;
import model.Stocks;
import model.exceptions.*;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class used as a client manager, it handles the messages and the states in the TUI
 * @see View.TUI
 */
public class ClientInterface {

	/**
	 * Current interface of the client: TUI or GUI
	 */
	private final UI ui;

	/**
	 * Current client connection
	 */
	private Client client;

	/**
	 * Nickname of the player
	 */
	private String nickname;

	/**
	 * Current state of the game
	 */
	private GameState gameState;

	/**
	 * Current card of the game
	 */
	private Card curCard;

	/**
	 * Current ship dashboard of the player
	 */
	public ShipDashboard ship;

	/**
	 * List of the components which are revealed
	 */
	private ArrayList<ShipComponent> revealedComponents = new ArrayList<>();

	/**
	 * List of indexes of the small decks
	 */
	private ArrayList<Integer> smallDecksIndexes = new ArrayList<>();

	/**
	 * Index of the small picked deck
	 */
	int pickedSmallDeckIndex;

	/**
	 * Boolean to express if the timer can be restarted
	 */
	volatile boolean canRestartTimer = false;

	/**
	 * Boolean to express if the current timer is the last one
	 */
	boolean timerIsLast = false;

	/**
	 * Boolean to express if the current timer is NOT running (true)
	 */
	boolean timerIsPaused = false;

	/**
	 * Boolean used to understand if the first component is placed
	 */
	boolean firstComponentPlaced = false;

	/**
	 * Parallel executor used in order to do multiple immediately regardless of other ongoing tasks
	 */
	private final ExecutorService parallelExecutor = Executors.newCachedThreadPool();

	/**
	 * Sequential executor used in order to do multiple tasks in sequence
	 */
	private final ExecutorService sequentialExecutor = Executors.newSingleThreadExecutor();
	private final int[][] premadeShipIDs;
	private final int[][] premadeShipOrientations;
	private final int[][] wrongPremadeShipIDs;
	private final int[][] wrongPremadeShipOrientations;
	boolean premadeShipWrong = false;
	int premadeShipX, premadeShipY;

	/**
	 * IDs of the central cabin
	 */
	private final int[] centralCabinID;

	/**
	 * Boolean signifying whether the player has signaled the ship as completed or not
	 */
	boolean shipPlaced = false;

	/**
	 * Boolean signifying whether the player is currently viewing and selecting a revealed ship component
	 */
	boolean viewingRevealedComponents = false;

	/**
	 * Constructor of the class: it's used to combine an UI interface to the class
	 * @see UI
	 * @see View.TUI
	 * @see View.GUI
	 * @param view current UI interface
	 */
	public ClientInterface(UI view){
		this.ui =view;
		premadeShipX = 0;
		premadeShipY = 0;
		premadeShipIDs = new int[][]{
				{-1, -1, 100, -1, 102, -1, -1,},
				{-1, 127, 45, 143, 48, 141, -1},
				{112, 150, 12, 33, 64, 47, 108},
				{114, 35, 30, 82, 152, 10, 133},
				{78, 84, 87, -1, 94, 95, 99}};
		premadeShipOrientations = new int[][]{
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 2, 2, 3, 1, 0},
				{1, 1, 3, 0, 2, 3, 3},
				{1, 0, 3, 0, 3, 2, 3},
				{0, 0, 0, 0, 0, 0, 0}};

		wrongPremadeShipIDs = new int[][]{
				{-1, -1, 100, -1, 102, -1, -1,},
				{-1, 127, 45, 143, 48, 141, -1},
				{112, 150, 12, 33, 64, 47, 108},
				{114, 35, 30, 82, 152, 10, 132},
				{78, 84, 87, -1, 94, 99, 99}};
		wrongPremadeShipOrientations = new int[][]{
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 2, 2, 3, 1, 0},
				{1, 1, 3, 0, 2, 3, 3},
				{1, 0, 3, 0, 3, 2, 3},
				{0, 0, 0, 0, 0, 0, 0}};

		centralCabinID = new int[4];
		centralCabinID[0] = 32;
		centralCabinID[1] = 33;
		centralCabinID[2] = 51;
		centralCabinID[3] = 60;
	}

	/**
	 * Method used to invoke the asking connection in the very preliminary part of the game
	 * @param IP current selected IP
	 * @param port current port
	 * @param socketConnection true if the asked connection is socket
	 * @throws IOException input output exception thrown
	 */
	public void connect(String IP,int port,boolean socketConnection) throws IOException {
		if(socketConnection){
			try{
				client=new ClientSocket(IP,port,this);
				client.readMessage();
				ui.askingNickname();
			}
			catch(IOException e){
				ui.sendGenericMessage("Sorry, but the socket connection has failed...");
			}
		}
		else{
			try{
				client=new ClientRMI(IP,port,this);
				ui.askingNickname();
			}
			catch(IOException | NotBoundException e){
				ui.sendGenericMessage("Sorry, but the RMI connection has failed...");
			}
		}
	}

	/**
	 * Getter of shipPlaced
	 * @return shipPlaced
	 */
	public boolean isShipPlaced(){
		return shipPlaced;
	}

	/**
	 * Setter of shipPlaced
	 * @param isShipPlaced new value of shipPlaced
	 */
	public void setShipPlaced(boolean isShipPlaced){
		shipPlaced = isShipPlaced;
	}

	/**
	 * Getter of canRestartTimer
	 * @return true if the timer can be restarted
	 */
	public boolean canRestartTimer(){
		return canRestartTimer;}

	/**
	 * Setter of canRestartTimer
	 * @param newVal new value
	 */
	public void setCanRestartTimer(boolean newVal){

		canRestartTimer = newVal;}

	/**
	 * Getter of timerIsLast
	 * @return true if the current timer is the last one
	 */
	public boolean isTimerLast(){return timerIsLast;}

	/**
	 * Setter of timerIsLast
	 * @param newVal new value
	 */
	public void setTimerIsLast(boolean newVal){timerIsLast = newVal;}

	/**
	 * Getter of timerIsPending
	 * @return true if the timer is currently not running
	 */
	public boolean isTimerPaused(){return timerIsPaused;}

	/**
	 * Getter of firstComponentPlaced
	 * @return true if the first component is placed
	 */
	public boolean firstComponentPlaced(){return firstComponentPlaced;}

	/**
	 * Setter of firstComponentPlaced
	 * @param newVal new value
	 */
	public void setFirstComponentPlaced(boolean newVal){firstComponentPlaced = newVal;}

	/**
	 * Setter of premadeShipWrong
	 * @param newVal new value
	 */
	public void setPremadeShipWrong(boolean newVal){premadeShipWrong = newVal;}

	/**
	 * Setter of firstComponentPlaced
	 * @return premadeShipWrong
	 */
	public boolean getPremadeShipWrong(){return premadeShipWrong;}

	/**
	 * Setter of the current nickname
	 * @param nickname current nickname
	 * @throws IOException input output exception thrown
	 */
	public void setNickname(String nickname) throws IOException {
		this.nickname=nickname;
		client.sendMessage(new LogRequestMessage(nickname));
	}

	/**
	 * Sender of the players number
	 * @param playersNumber current number of players selected
	 * @throws IOException input output exception thrown
	 */
	public void setPlayersNumber(int playersNumber) throws IOException {client.sendMessage(new PlayersNumResponseMessage(nickname,playersNumber));}

	/**
	 * Sender of the request to draw a card
	 * @throws IOException input output exception thrown
	 */
	public void drawCard() throws IOException {
		client.sendMessage(new DrawCardRequestMessage(nickname));}

	/**
	 * Sender of the request to draw a specific small deck
	 * @param index index of the small deck
	 * @throws IOException input output exception thrown
	 */
	public void requestSmallDeck(int index) throws IOException {

		if(!lastTimerExpired()) {
			pickedSmallDeckIndex = index;
			client.sendMessage(new SmallDeckRequestMessage(nickname, index));
		}
	}

	/**
	 * Sender of the request to return a specific small deck
	 * @throws IOException input output exception thrown
	 */
	public void returnSmallDeck() throws IOException {
		if (!lastTimerExpired()) {
			try {
				client.sendMessage(new NotifyActionCompleted(nickname, new TurnMessage()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			client.sendMessage(new ReturnSmallDeckMessage(nickname, pickedSmallDeckIndex));
		}
	}

	/**
	 * Sender of the request to claim a reward
	 * @param choice true if you want to claim the reward
	 * @throws IOException input output exception thrown
	 */
	public void claimReward(boolean choice) throws IOException {
		client.sendMessage(new ClaimRewardChoiceMessage(nickname,choice));
	}

	/**
	 * Sender of the request to land on a specific planet
	 * @param id planets ID
	 * @throws IOException input output exception thrown
	 */
	public void landOnPlanet(int id) throws IOException {
		try {
			client.sendMessage(new NotifyActionCompleted(nickname, new WaitForOthersTurns()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		client.sendMessage(new PlanetLandRequestMessage(this.nickname,id));
	}

	/**
	 * Sender of the request to draw a specific component
	 * @throws IOException input output exception thrown
	 */
	public void drawComponent() throws IOException {
		if(!lastTimerExpired())
			client.sendMessage(new DrawComponentRequestMessage(nickname));}

	/**
	 * Sender of the request to draw a specific component
	 * @param id component ID
	 * @throws IOException input output exception thrown
	 */
	public void drawSpecificComponent(int id) throws IOException {
		if(!lastTimerExpired())
			client.sendMessage(new DrawSpecificComponentRequestMessage(nickname, id));}

	/**
	 * Sender of the request to draw a pre-made component
	 * @param id pre-made component ID
	 * @throws IOException input output exception thrown
	 */
	public void drawPremadeComponent(int id) throws IOException {
		if(!lastTimerExpired())
			client.sendMessage(new PremadeShipComponentRequest(nickname, id));}

	/**
	 * Sender of the request to pick a revealed component
	 * @param componentIndex index of the component to reveal
	 * @throws IOException input output exception thrown
	 */
	public void pickRevealedComponent(int componentIndex) throws IOException {
		if(!lastTimerExpired())
			client.sendMessage(new PickRevealedComponentRequestMessage(nickname, componentIndex));}

	/**
	 * Sender of the endgame message
	 * @param choice choice to end or begin a new game
	 * @throws IOException input output exception thrown
	 */
	public void sendEndGame(int choice) throws IOException {client.sendMessage(new EndGameMessage(this.nickname,choice));}

	/**
	 * Sender of the request to return a component
	 * @param component component to return
	 * @throws IOException input output exception thrown
	 */
	public void returnComponent(ShipComponent component) throws IOException {
		if(!lastTimerExpired()){
			client.sendMessage(new ReturnComponentRequestMessage(nickname, component));

			try {
				client.sendMessage(new NotifyActionCompleted(nickname, new TurnMessage()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Sender of the request to start timer
	 * @throws IOException input output exception thrown
	 */
	public void startTimer() throws IOException {
		if(!lastTimerExpired())
			client.sendMessage(new StartTimerRequestMessage(nickname));}

	/**
	 * Sender of the request to place the ship
	 * @throws IOException input output exception thrown
	 */
	public void sendShip() throws IOException {
		try {
			client.sendMessage(new NotifyActionCompleted(nickname, new WaitForOthersTurns()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		client.sendMessage(new PlaceShipRequestMessage(nickname,ship));}

	/**
	 * Sender of the updated ship
	 * @throws IOException input output exception thrown
	 */
	public void sendUpdatedShip() throws IOException {
		ship.initializeShipAttributesFromComponents();
		try {
			client.sendMessage(new NotifyActionCompleted(nickname, new WaitForOthersTurns()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		client.sendMessage(new UpdateServerShipMessage(nickname,ship));
	}

	/**
	 * Sender of the current dynamic motor power
	 * @param dynamicMotorPower current dynamic motor power
	 * @throws IOException input output exception thrown
	 */
	public void sendDynamicMotorPower(int dynamicMotorPower) throws IOException {
		try {
			client.sendMessage(new NotifyActionCompleted(nickname, new WaitForOthersTurns()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		client.sendMessage(new DynamicMotorPowerMessage(nickname,dynamicMotorPower,	ship.getShip()));
	}

	/**
	 * Sender of the current dynamic fire power
	 * @param dynamicFirePower current dynamic fire power
	 * @throws IOException input output exception thrown
	 */
	public void sendDynamicFirePower(double dynamicFirePower) throws IOException {
		try {
			client.sendMessage(new NotifyActionCompleted(nickname, new WaitForOthersTurns()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		client.sendMessage(new DynamicFirePowerMessage (nickname,dynamicFirePower,	ship.getShip()));
	}

	/**
	 * Sender of the request to discard some stocks
	 * @param stocks stocks to discard
	 * @throws IOException input output exception thrown
	 */
	public void sendStocksToDiscard(Stocks stocks) throws IOException {client.sendMessage(new DiscardStocksMessage(nickname, stocks));}

	/**
	 * Sender of the request to activate a card
	 * @param answerIsYes true if they want to activate a card
	 * @throws IOException input output exception thrown
	 */
	public void sendCardActivationRequest(boolean answerIsYes) throws IOException {
		if(!answerIsYes)
		{
			try {
				client.sendMessage(new NotifyActionCompleted(nickname, new WaitForOthersTurns()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		client.sendMessage(new CardActivationRequestMessage(nickname, answerIsYes));
	}
	//ABANDONED SHIP

	/**
	 * Method called to print the exception
	 * @param t current exception
	 */
	public void printThreadUncheckedExceptions(Throwable t){
		Thread current = Thread.currentThread();
		Thread.getDefaultUncaughtExceptionHandler().uncaughtException(current, t);
		t.printStackTrace();
	}

	/**
	 * Setter of the ship
	 * @param message message which contains the ship
	 */
	public void setShip(LogResponseMessage message) {
		ship = new ShipDashboard(centralCabinID[message.getPlayerNumber() - 1]);
		ship.setNickname(nickname);
	}

	/**
	 * Getter of the current card
	 * @return current card
	 */
	public Card getCurCard(){return curCard;}

	/**
	 * Getter of the current nickname
	 * @return current nickname
	 */
	public String getNickname(){return this.nickname;}

	/**
	 * Getter of the pre-made ship orientations
	 * @return current pre-made ship orientation
	 */
	public int[][] getPremadeShipOrientations(){return premadeShipOrientations;}

	/**
	 * Getter of the X coordinate of the pre-made ship
	 * @return current X coordinate
	 */
	public int getPremadeShipX(){return premadeShipX;}

	/**
	 * Setter of the current X coordinate for the pre-made ship
	 * @param premadeShipX X coordinate
	 */
	public void setPremadeShipX(int premadeShipX){this.premadeShipX=premadeShipX;}

	/**
	 * Adder of Y coordinate of the pre-made ship
	 */
	public void addPremadeShipY(){this.premadeShipY++;}

	/**
	 * Getter of the Y coordinate of the pre-made ship
	 * @return current Y coordinate
	 */
	public int getPremadeShipY(){return premadeShipY;}

	/**
	 * Adder of X coordinate of the pre-made ship
	 */
	public void addPremadeShipX(){this.premadeShipX++;}

	/**
	 * Adder to the ship for the pre-made ship
	 * @param component component to add into the ship
	 * @param premadeShipX current X coordinate of pre-made ship
	 * @param premadeShipY current Y coordinate of pre-made ship
	 * @param possibility current possibility
	 */
	public void addToShip(ShipComponent component,int premadeShipX, int premadeShipY, boolean possibility){ship.addComponent(component, premadeShipX, premadeShipY, possibility);}

	/**
	 * Method used to have a particular action according to the message received with socket,
	 * the messages are dispatched with visitor
	 * @see ClientMessageHandlerVisitor
	 * @param message message to handle
	 * @throws IOException input output exception thrown
	 */
	synchronized public void actions(Message message) throws IOException {

		ClientMessageHandlerVisitor clientMessageHandlerVisitor = new ClientMessageHandlerVisitor(this);
		message.accept(clientMessageHandlerVisitor);
	}

	/**
	 * Getter of the current state
	 * @return current state
	 */
	public GameState getState(){return this.gameState;}

	/**
	 * Setter of the current state
	 * @param gameState current state
	 */
	public void setState(GameState gameState){this.gameState=gameState;}

	/**
	 * Setter of the current card
	 * @param card current card
	 */
	public void setCard(Card card){this.curCard=card;}

	/**
	 * Method used to finalize the ship
	 * @see ShipDashboard
	 */
	public void finalizeShip(){ship.finalizeShip();}

	/**
	 * Old method used to initialize the ship
	 * @deprecated
	 */
	public void initializeShipAttributesFromComponents(){ship.initializeShipAttributesFromComponents();}

	/**
	 * Getter of the crew loss from a card, it's used with visitor
	 * @see CardVisitHandlerForClient
	 * @param card current card
	 * @return card crew loss
	 */
	public int getCardCrewLoss(Card card){return card.returner(new CardVisitHandlerForClient());}

	/**
	 * Method used to handle the current state with visitor
	 * @throws IOException input output exception thrown
	 */
	public void handleState() throws IOException {gameState.accept(new ClientInterfaceStateHandlerVisitor(this.ui,this,this.sequentialExecutor));}

	/**
	 * Getter of the current ship
	 * @return current ship
	 */
	public ShipComponent[][] getShip(){return ship.getShip();}

	/**
	 * Getter of a specific component from the ship
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return specific component
	 */
	public ShipComponent getShipComponent(int x, int y){return ship.getShip()[y - get_yLowerBound()][x - get_xLowerBound()];}

	/**
	 * Getter of the crew of the ship
	 * @return ship crew
	 */
	public int getShipCrew(){return ship.getCrew();}

	/**
	 * Method used to remove the crew from a specif cabin
	 * @param x X coordinate of the cabin
	 * @param y Y coordinate of the cabin
	 * @param humansToRemove amount of humans to remove
	 * @param removePurpleAlien true if you want to remove the purple alien
	 * @param removeBrownAlien true if you want to remove the brown alien
	 * @throws IncompatibleTargetComponent thrown when the component is not a cabin
	 */
	public void removeCrewFromCabin(int x, int y, int humansToRemove, boolean removePurpleAlien, boolean removeBrownAlien) throws IncompatibleTargetComponent{
		ship.removeCrewFromCabin(x - get_xLowerBound(), y - get_yLowerBound(), humansToRemove, removePurpleAlien, removeBrownAlien);}

	/**
	 * Getter of the amount of double motors
	 * @return current double motors from the ship
	 */
	public int getShipDoubleMotors(){return ship.getDoubleMotors();}

	/**
	 * Getter of the amount of double cannons
	 * @return current double cannons from the ship
	 */
	public int getShipDoubleCannons(){return ship.getDoubleCannons();}

	/**
	 * Method called to use some batteries
	 * @param batteriesToRemove batteries to use and remove
	 */
	public void useShipBatteries(int batteriesToRemove){ship.useBatteries(batteriesToRemove);}

	/**
	 * Getter of the amount of batteries of the ship
	 * @return amount of batteries
	 */
	public int getShipBatteries(){return ship.getBatteries();}

	/**
	 * Getter of the current static firepower
	 * @return current firepower
	 */
	public double getShipFirePower(){return ship.getStaticFirePower();}

	/**
	 * Getter of the current static motor power
	 * @return current motor power
	 */
	public int getShipMotorPower(){return ship.getStaticMotorPower();}

	/**
	 * Getter of the current ship width
	 * @deprecated
	 * @return current ship width
	 */
	public int getShipWidth(){return ship.getShipWidth();}

	/**
	 * Getter of the current ship height
	 * @deprecated
	 * @return current ship height
	 */
	public int getShipHeight(){return ship.getShipHeight();}

	/**
	 * Getter of the ship X lower bound
	 * @return current ship X lower bound
	 */
	public int get_xLowerBound(){return ship.get_xLowerBound();}

	/**
	 * Getter of the ship X upper bound
	 * @return current ship X upper bound
	 */
	public int get_xUpperBound(){return ship.get_xUpperBound();}

	/**
	 * Getter of the ship Y lower bound
	 * @return current ship Y lower bound
	 */
	public int get_yLowerBound(){return ship.get_yLowerBound();}

	/**
	 * Getter of the ship Y upper bound
	 * @return current ship Y upper bound
	 */
	public int get_yUpperBound(){return ship.get_yUpperBound();}

	/**
	 * Method used to add a specific component
	 * @param comp current component
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @throws IllegalPositionException thrown when the coordinates of a ship are not respected
	 * @throws SlotTakenException thrown when a certain slot is already taken
	 * @throws FloatingComponentException thrown when a component is not attached to the rest of the ship
	 */
	public void addComponent(ShipComponent comp, int x, int y) throws IllegalPositionException, SlotTakenException, FloatingComponentException {
		ship.addComponent(comp, x, y);
		try {
			client.sendMessage(new NotifyActionCompleted(nickname, new TurnMessage()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method used to book a specific component
	 * @param component specific component
	 * @throws BookingSlotsFullException thrown when there are still two booked components in a ship dashboard
	 */
	public void bookComponent(ShipComponent component) throws BookingSlotsFullException {
		ship.bookComponent(component);
		try {
			client.sendMessage(new NotifyActionCompleted(nickname, new TurnMessage()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method used to add a booked component into the garbage
	 */
	public void addBookedComponentsToGarbage() {ship.addBookedComponentsToGarbage();}

	/**
	 * Method used to increase the garbage
	 * @param amount current garbage amount
	 */
	public void addGarbage(int amount){ship.addGarbage(amount);}

	/**
	 * Getter of a specific booked component
	 * @param index index of the specific component to get
	 * @return specific booked component
	 * @throws NoBookedComponentException thrown when there are no booked components
	 * @throws IndexOutOfBoundsException thrown when the index is out of bound
	 */
	public ShipComponent getBookedComponent(int index) throws NoBookedComponentException, IndexOutOfBoundsException {
		try {
			return ship.getBookedComponent(index);
		}
		catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * Method used to remove a specific booked component
	 * @param index index of the booked component to remove
	 */
	public void removeBookedComponent(int index){ship.removeBookedComponent(index);}

	/**
	 * Getter of the list of booked components
	 * @return list of booked components
	 */
	public ArrayList<ShipComponent> getBookedComponents(){return ship.getBookedComponents();}

	/**
	 * Getter of the list of revealed components
	 * @return list of revealed components
	 */
	public ArrayList<ShipComponent> getRevealedComponents(){return revealedComponents;}

	/**
	 * Getter of the list of indexes of small decks
	 * @return list of indexes of small decks
	 */
	public ArrayList<Integer> getSmallDecksIndexes(){return smallDecksIndexes;}

	/**
	 * Method used to understand if a specific component is available
	 * @param componentIndex index of the specific component
	 * @return true if the components is available
	 */
	public boolean isComponentAvailable(int componentIndex){return componentIndex < revealedComponents.size();}

	/**
	 * Method used to update the list of revealed components
	 * @param revealedComponents list of new revealed components
	 */
	public void updateRevealedComponents(ArrayList<ShipComponent> revealedComponents){this.revealedComponents = revealedComponents;}

	/**
	 * Method used to update the list of small deck indexes
	 * @param smallDecksIndexes new list of small deck indexes
	 */
	public void updateSmallDecks(ArrayList<Integer> smallDecksIndexes){this.smallDecksIndexes = smallDecksIndexes;}

	public void resetPremadeShip(){
		premadeShipX = 0;
		premadeShipY = 0;
	}

	/**
	 * Method used to pre-make the ship for debugging
	 */
	public void premakeShip(){
		if(!premadeShipWrong){
			if(premadeShipIDs[premadeShipY][premadeShipX] != -1 && (premadeShipX != 3 || premadeShipY != 2)){
				try {
					drawPremadeComponent(premadeShipIDs[premadeShipY][premadeShipX]);
				} catch (IOException e) {
					System.out.println("Something went wrong while retrieving the component! Try again.");
				}
			}
			else {
				premadeShipX++;
				if(premadeShipX >= 7){
					premadeShipX = 0;
					premadeShipY++;
				}
				if(premadeShipY < 5)
					premakeShip();
				else{
					finalizeShip();
					try {
						setShipPlaced(true);
						sendShip();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		else {
			if(wrongPremadeShipIDs[premadeShipY][premadeShipX] != -1 && (premadeShipX != 3 || premadeShipY != 2)){
				try {
					drawPremadeComponent(wrongPremadeShipIDs[premadeShipY][premadeShipX]);
				} catch (IOException e) {
					System.out.println("Something went wrong while retrieving the component! Try again.");
				}
			}
			else {
				premadeShipX++;
				if(premadeShipX >= 7){
					premadeShipX = 0;
					premadeShipY++;
				}
				if(premadeShipY < 5)
					premakeShip();
				else{
					finalizeShip();
					try {
						setShipPlaced(true);
						sendShip();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	/**
	 * Method used to update small deck: it's called directly from RMI
	 * @param smallDecksIndexes list of small deck indexes
	 */
	public void updateSmallDecksCall(ArrayList<Integer> smallDecksIndexes){parallelExecutor.submit(() -> updateSmallDecks(smallDecksIndexes));}

	/**
	 * Method used to call UI interface for beginning: it's called directly from RMI
	 * @param players list of players
	 */
	public void beginCall(ArrayList<String> players){
		sequentialExecutor.submit(() -> {
		try {
			ui.begin(players);
		} catch (Throwable t) {
			printThreadUncheckedExceptions(t);
		}
	});
	}

	/**
	 * Method used to call the UI interface for an incorrect message: it's called directly from RMI
	 * @param kind kind of message
	 */
	public void incorrectCall(MessageKind kind){
		parallelExecutor.submit(() -> {
			try {
				throw new RuntimeException("The message received was not of valid kind: " + kind);
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to show a drawn card: it's called directly from RMI
	 * @param card current card
	 */
	public void cardDrawnCall(Card card){
		sequentialExecutor.submit(() -> {
			try {
				ui.notifyNewCardDrawn(card);
				setCard(card);
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to notify new credits and used to add them into the ship:
	 * it's called directly from RMI
	 * @param credits current credits
	 */
	public void creditsEarnedCall(int credits){
		ship.addCredits(credits);
		ui.notifyCredits(credits);
	}

	/**
	 * Method used to call the UI interface to handle a components: it's called directly from RMI
	 * @param component current component to handle
	 */
	public void drawCompnentCall(ShipComponent component){
		sequentialExecutor.submit(() -> {
			try {
				ui.handleComponent(component);
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to show the land on a planet: it's called directly from RMI
	 * @param freePlanets list of the free planets
	 */
	public void freePlanetsCall(ArrayList<Integer> freePlanets){
		sequentialExecutor.submit(() -> {
			try {
				ui.landOnPlanet(freePlanets, (Planets) getCurCard());
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to notify the new state: it's called directly from RMI
	 * @param gameState current state
	 */
	public void gameStateCall(GameState gameState){
		sequentialExecutor.submit(() -> {
			try {
				ui.notifyNewGameState(gameState);
				setState(gameState);
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to show a generic message: it's called directly from RMI
	 * @param message generic message
	 */
	public void genericCall(String message){
		parallelExecutor.submit(() -> {
			try {
				ui.sendGenericMessage(message);
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface show the login response: it's called directly from RMI
	 * @param message log response message
	 */
	public void logResponseCall(LogResponseMessage message){
		sequentialExecutor.submit(() -> {
			try {
				canRestartTimer = false;
				timerIsLast = false;
				timerIsPaused = false;
				firstComponentPlaced = false;
				shipPlaced = false;
				viewingRevealedComponents = false;
				if (message.getNicknameStatus()) {
					setShip(message);
				}
				ui.showLoginResponse(message);
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used from client to notify his presence and get the current ship:
	 * it's called directly from RMI
	 */
	public void clientResilienceCall() {
		parallelExecutor.submit(() -> {
            try {
				ResilienceResponseMessage message = new ResilienceResponseMessage(this.nickname);
				message.setShip(this.ship);
                client.sendMessage(message);
            } catch (Throwable t) {
				printThreadUncheckedExceptions(t);
            }
        });
	}

	/**
	 * Method used to call the UI interface to show the number of players: it's called directly from RMI
	 */
	public void playersNumCall(){
		sequentialExecutor.submit(() -> {
			try {
				ui.numberOfPlayers();
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to show the resume of a game thanks to resilience:
	 * it's called directly from RMI
	 * @param ship resumed ship
	 * @param curCard resumed card
	 * @param state resumed state
	 * @param lastMessage last message sent
	 */
	public void resumeCall(ShipDashboard ship, Card curCard, GameState state, Message lastMessage){
		sequentialExecutor.submit(() -> {
			try {
				this.ship=ship;
				firstComponentPlaced=false;
				if(ship!=null) {
					for (int y = 0; y < ship.getShip().length; y++) {
						for (int x = 0; x < ship.getShip()[y].length; x++) {
							if (!(x == 3 && y == 2) && ship.getShip()[y][x] != null && !(ship.getShip()[y][x] instanceof UnavailableSlot)) {
								firstComponentPlaced = true;
								break;
							}
						}
					}
				}
				else this.ship = new ShipDashboard();
				this.curCard=curCard;
				ui.notifyNewCardDrawn(curCard);
				this.gameState=state;
				ui.setInterface(this);
				ui.resume();
				if(lastMessage!=null){
					actions(lastMessage);
				}
				else handleState();
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to show positions and turns: it's called directly from RMI
	 * @param message position and turns message
	 */
	public void positionsAndTurnsCall(PositionsAndTurnsMessage message){
		parallelExecutor.submit(() -> {
			for(ShipDashboard p : message.getPlayers()){
				if(p.getNickname().equals(nickname))
					ship.setPosition(p.getPosition());
			}
			if(message.getPrintPositions())
				ui.showPositionsAndTurns(message.getPlayers());
		});
	}

	/**
	 * Method used to set a pre-made ship component: it's called directly from RMI
	 * @param component current ship component
	 */
	public void premadeShipComponentCall(ShipComponent component) {
		parallelExecutor.submit(()->{
			ShipComponent curPremadeShipComp;
			curPremadeShipComp = component;
			curPremadeShipComp.counterClockwiseRotation(getPremadeShipOrientations()[getPremadeShipY()][getPremadeShipX()]);
			addToShip(component, getPremadeShipX(), getPremadeShipY(), false);
			addPremadeShipX();
			if (getPremadeShipX() >= 7) {
				setPremadeShipX(0);
				addPremadeShipY();
			}
			if (getPremadeShipY() < 5)
				premakeShip();
			else {
				finalizeShip();
				try {
					setShipPlaced(true);
					placeShip(null);
				} catch (Throwable t) {
					printThreadUncheckedExceptions(t);
				}
			}
		});
	}

	/**
	 * Method used to call the UI interface to handle projectiles: it's called directly from RMI
	 * @param projectile current projectiles
	 * @param trajectory trajectory of projectiles
	 */
	public void projectileTrajectoryCall(Projectile projectile,int trajectory){
		sequentialExecutor.submit(() -> {
			try {
				ui.handleProjectile(projectile, trajectory, ship);
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to update the revealed components
	 * @param components current components to update
	 */
	public void revealedComponentsCall(ArrayList<ShipComponent> components){
		parallelExecutor.submit(() -> {
			updateRevealedComponents(components);
			if(viewingRevealedComponents && ui instanceof TUI tui) {
				tui.notifyRevealedComponents();
				tui.printComponents(revealedComponents);
			}
		});
	}

	/**
	 * Method used to call the UI interface to show small deck response: it's called directly from RMI
	 * @param deck current deck
	 */
	public void smallDeckResponseCall(Deck deck, int deckIndex){
		sequentialExecutor.submit(() -> {
			try {
				pickedSmallDeckIndex = deckIndex;
				ui.handleSmallDeck(deck);
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to add and rearrange the stocks: it's called directly from RMI
	 * @param stocks stocks to add
	 */
	public void stocksToAddCall(Stocks stocks){
		sequentialExecutor.submit(() -> {
			try {
				ui.askAddAndRearrangeStocks(stocks, ship.getShip());
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to notify timer expired: it's called directly from RMI
	 * @param last true if the timer is expired
	 */
	public void timerExpiredCall(boolean last){
		parallelExecutor.submit(() -> {
			setCanRestartTimer(!last);
			timerIsLast = last;
			timerIsPaused = true;
			if(last){

				ui.handleTimerExpired(last);
			}
			else
				ui.notifyTimerExpired();
		});
		if(last)
			return;

		setCanRestartTimer(!last);
		sequentialExecutor.submit(() -> {
			//if the timer isn't the last one, it can be restarted, if it IS the last one than it CAN'T be restarted
			try {
				ui.handleTimerExpired(last);

			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to notify the timer restarted:
	 * it's called directly from RMI
	 * @param last true if the timer is started
	 */
	public void timerStartedCall(boolean last){
		parallelExecutor.submit(() -> {
			try {
				setCanRestartTimer(false);
				timerIsLast = last;
				timerIsPaused = false;
				ui.notifyTimerRestarted(last);
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to notify the turn: it's called directly from RMI
	 */
	public void turnCall(){
		sequentialExecutor.submit(() -> {
			try {
				ui.notifyTurn();
				handleState();
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to wait for the players: it's called directly from RMI
	 */
	public void waitingCall(){
		sequentialExecutor.submit(() -> {
			try {
				ui.waitingPartners();
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to wait for the players: it's called directly from RMI
	 */
	public void waitingTurnsCall(){
		parallelExecutor.submit(() -> {
			try {
				setShipPlaced(true);
				ui.waitingForOthersTurns();
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to call the UI interface to show winners: it's called directly from RMI
	 * @param winners list of winners
	 */
	public void winnersCall(ArrayList<ShipDashboard> winners){
		sequentialExecutor.submit(() -> {
			try {
				ui.winners(winners, getNickname());
			} catch (Throwable t) {
				printThreadUncheckedExceptions(t);
			}
		});
	}

	/**
	 * Method used to remove all crew mates
	 */
	public void removeAllCrewmates(){ship.removeAllCrewmates();}

	/**
	 * Adds booked components and the last drawn component (if not already placed) to the garbage heap
	 * @param selectedComponent the last drawn component, if not placed yet, otherwise null
	 */
	public void handleGarbage(ShipComponent selectedComponent){
		addBookedComponentsToGarbage();
		if(selectedComponent != null)
			addGarbage(1);
	}

	/**
	 * Handles the placing of the ship on the board
	 * @param selectedComponent the last drawn component, if not placed yet, otherwise null
	 */
	public void placeShip(ShipComponent selectedComponent) throws IOException{
		handleGarbage(selectedComponent);
		finalizeShip();
		setShipPlaced(true);
		sendShip();
	}

	/**
	 * Method used to check if the last timer expired or not;
	 * it invokes {@link #isTimerPaused()} and {@link #isTimerLast()} in order to verify it
	 * @return true if the last hourglass is expired
	 */
	public boolean lastTimerExpired() {
		return (isTimerPaused() && isTimerLast());
	}
}
