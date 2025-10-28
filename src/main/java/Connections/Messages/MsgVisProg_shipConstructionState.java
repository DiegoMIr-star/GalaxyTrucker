package Connections.Messages;

import Controller.State.ToBeFixedAndFixingShips;
import View.ColorManagement.ConsoleColor;
import model.*;
import model.DifferentShipComponents.ShipComponent;

import java.util.ArrayList;

/**
 * Visitor class used to dispatch the different messages during ship construction in the controller
 * @see Controller.Controller
 */
public class MsgVisProg_shipConstructionState implements MessageVisitorProgresser{

	/**
	 * Current game
	 */
	Game game;

	/**
	 * Players ships
	 */
	ArrayList<ShipDashboard> players;

	/**
	 * Players ships after they have been placed
	 */
	ArrayList<ShipDashboard> playersBuilt = null;

	/**
	 * Object which contains the next game with the messages to send
	 */
	NextGameStateAndMessages nextGameStateAndMessages;

	/**
	 * Timer listener implementation, it's used when the timer is expired: very useful to avoid
	 * mixing controller with model
	 */
	TimerListener onTimerExpiredCallback;

	/**
	 * Current number of players
	 */
	int numPlayers;

	/**
	 * List of players nicknames
	 */
	ArrayList<String> nicknames;

	/**
	 * Class constructor, used to initializes all the attributes
	 * @param game current game
	 * @param players current list of players
	 * @param onTimerExpiredCallback current timer listener
	 * @param numPlayers current number of players
	 * @param nicknames current list of nicknames
	 */
	public MsgVisProg_shipConstructionState(Game game, ArrayList<ShipDashboard> players, ArrayList<ShipDashboard> playersBuilt, TimerListener onTimerExpiredCallback, int numPlayers, ArrayList<String> nicknames) {
		this.game = game;
		this.players = players;
		nextGameStateAndMessages = new NextGameStateAndMessages(players);
		this.onTimerExpiredCallback = onTimerExpiredCallback;
		this.numPlayers = numPlayers;
		this.nicknames = nicknames;
		this.playersBuilt = playersBuilt;
	}

	private NextGameStateAndMessages throwWrongMessageException(Message message){
		System.out.println(ConsoleColor.TEXT_RED +
				"unexpected message of type " + message.getKind() + " during ship construction state" + ConsoleColor.RESET);
		throw new RuntimeException("unexpected message of type " + message.getKind() + " during ship construction state");
	}

	/**
	 * Visit method for available small deck message: it throws a runtime exception
	 * @param message availableSmallDeckMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(AvailableSmallDeckMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for begin message: it throws a runtime exception
	 * @param message beginMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(BeginMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for card activation request message: it throws a runtime exception
	 * @param message cardActivationRequestMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(CardActivationRequestMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for card drawn message: it throws a runtime exception
	 * @param message cardDrawnMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(CardDrawnMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for claim reward choice message: it throws a runtime exception
	 * @param message claimRewardChoiceMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(ClaimRewardChoiceMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for credits earned message: it throws a runtime exception
	 * @param message creditsEarnedMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(CreditsEarnedMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for discard stocks message: it throws a runtime exception
	 * @param message discardStocksMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(DiscardStocksMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for draw card request message: it throws a runtime exception
	 * @param message drawCardRequestMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(DrawCardRequestMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for draw component request message: it picks the asked component and
	 * it updates the state and the message to send into the next game state and messages object
	 * @param message drawComponentRequestMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(DrawComponentRequestMessage message) {
		//the player has asked for a covered component
		ShipComponent requestedComponent = game.getCoveredShipComponent();
		if(requestedComponent == null){
			nextGameStateAndMessages.setPlayerMessage(message.getNickname(),
					new GenericMessage("There aren't any covered components left to draw"));
			nextGameStateAndMessages.setPlayerMessage(message.getNickname(),
					new TurnMessage());
			return nextGameStateAndMessages;
		}
		//else
		nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new DrawComponentResponseMessage(requestedComponent));

		return nextGameStateAndMessages;
	}

	/**
	 * Visit method for draw component response message: it throws a runtime exception
	 * @param message drawComponentResponseMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(DrawComponentResponseMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for draw specific request message: it picks the specific component and
	 * it updates the state and the message to send into the next game state and messages object
	 * @param message drawSpecificComponentRequestMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(DrawSpecificComponentRequestMessage message) {
		//the player has asked for a covered component
		ShipComponent requestedComponentById = game.getComponentById(message.getId());
		if(requestedComponentById == null){
			nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new GenericMessage("Couldn't find that component"));
		}

		nextGameStateAndMessages.setPlayerMessage(message.getNickname(),new DrawComponentResponseMessage(requestedComponentById));
		return nextGameStateAndMessages;
	}

	/**
	 * Visit method for dynamic firepower message: it throws a runtime exception
	 * @param message dynamicFirePowerMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(DynamicFirePowerMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for dynamic motor power message: it throws a runtime exception
	 * @param message dynamicMotorPowerMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(DynamicMotorPowerMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for endgame message: it throws a runtime exception
	 * @param message endGameMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(EndGameMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for free planet response message: it throws a runtime exception
	 * @param message freePlanetsResponseMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(FreePlanetsResponseMessage message) {return throwWrongMessageException(message);}

	/**
	 * Getter of the available decks indexes from the current game
	 * @return current list of available decks indexes
	 */
	private ArrayList<Integer> getAvailableDecksIndexes(){
		ArrayList<Integer> freeDecks = new ArrayList<>();
		for (int i = 0; i < game.getDecks().size(); i++) {
			if (!game.getDecks().get(i).isUsed()) {
				freeDecks.add(i);
			}
		}
		return freeDecks;
	}

	/**
	 * It sets the available decks indexes into a message for next game state and messages
	 */
	private void updateAvailableDecks() {
		ArrayList<Integer> availableCardPiles = getAvailableDecksIndexes();
		for(int i = 0; i < nicknames.size(); i++){
			nextGameStateAndMessages.setPlayerMessage(nicknames.get(i), new AvailableSmallDeckMessage(availableCardPiles));
		}
	}

	/**
	 * Visit method for available small deck request message: it updates available decks and
	 * it returns the next game state and messages object
	 * @param message availableSmallDecksRequestMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(AvailableSmallDecksRequestMessage message) {
		updateAvailableDecks();
		return nextGameStateAndMessages;
	}

	/**
	 * Visit method for game state message: it throws a runtime exception
	 * @param message gameStateMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(GameStateMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for generic message: it throws a runtime exception
	 * @param message genericMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(GenericMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for log request message: it throws a runtime exception
	 * @param message logRequestMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(LogRequestMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for log response message: it throws a runtime exception
	 * @param message logResponseMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(LogResponseMessage message) {return throwWrongMessageException(message);}

	/**
	 * It updates the next game state and messages with revealed components
	 */
	private void sendUpdatedRevealedComponents() {
		ArrayList<ShipComponent> revealedShipComponents = game.getRevealedShipComponents();
		for(int i = 0; i < nicknames.size(); i++){
			nextGameStateAndMessages.setPlayerMessage(nicknames.get(i), new RevealedComponentsMessage(revealedShipComponents));
		}
	}

	/**
	 * Visit method for pick revealed component request message: it picks the specific revealed component and
	 * it updates the state and the message to send into the next game state and messages object
	 * @param message pickRevealedComponentRequestMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(PickRevealedComponentRequestMessage message) {
		int requestedComponentIndex = message.getComponentIndex();
		nextGameStateAndMessages.setPlayerMessage(message.getNickname(),
				new DrawComponentResponseMessage(game.getRevealedShipComponent(requestedComponentIndex)));
		//each time a player takes an uncovered component, all the players get updated about the new Arraylist of
		// uncovered ship components
		sendUpdatedRevealedComponents();

		return nextGameStateAndMessages;
	}

	/**
	 * It updates the next game state and messages
	 * @param printPositions not used
	 */
	private void sendPositionsAndTurns(boolean printPositions) {
		for(int i = 0; i < playersBuilt.size(); i++){
			nextGameStateAndMessages.setPlayerMessage(nicknames.get(i), new PositionsAndTurnsMessage(playersBuilt, printPositions));
		//	view.sendPositionsAndTurns(getPositions(), getTurns());
		}
	}

	/**
	 * Visit method for place ship request message: it places the ship and
	 * it returns the next game state and messages object
	 * @param message placeShipRequestMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(PlaceShipRequestMessage message) {
		GameTimer timer = game.getTimer();

		if(playersBuilt==null) {
			playersBuilt = new ArrayList<>();
		}
		this.playersBuilt.add(message.getShip());
		if (playersBuilt.size() == 1) {
			this.playersBuilt.getLast().setPosition(7);
			sendPositionsAndTurns(false);
		}
		if (playersBuilt.size() == 2) {
			this.playersBuilt.getLast().setPosition(4);
			sendPositionsAndTurns(false);
		}
		if (this.numPlayers > 2 && playersBuilt.size() == 3) {
			this.playersBuilt.getLast().setPosition(2);
			sendPositionsAndTurns(false);
		}
		if (this.numPlayers == 4 && playersBuilt.size() == 4) {
			this.playersBuilt.getLast().setPosition(1);
			sendPositionsAndTurns(false);
		}

		//when all the players have completed their ships, the state changes even if the hourglassRound is < 3
		nextGameStateAndMessages.setPlayers(players);
		if (playersBuilt.size() == this.numPlayers) {

			//the management of the fixing of the ship takes place in the client, who has to be notified by the clientHandler
			boolean lastTimerExpired = timer.getCurTimerRound() == timer.getTimerRoundsSeconds().size() &&
					!timer.isTimerRunning();
			timer.stopEverything();
			if(!lastTimerExpired){
				for(int i = 0; i < playersBuilt.size(); i++){
					nextGameStateAndMessages.setPlayerMessage(nicknames.get(i), new TimerExpiredMessage(true));
				}
			}
			nextGameStateAndMessages.setNextGameState(new ToBeFixedAndFixingShips());

			for(int i = 0; i < playersBuilt.size(); i++){
				nextGameStateAndMessages.setPlayerMessage(nicknames.get(i), new TurnMessage());
			}
			nextGameStateAndMessages.setPlayers(playersBuilt);
		}
		return nextGameStateAndMessages;
	}

	/**
	 * Visit method for planet land request message: it throws a runtime exception
	 * @param message planetLandRequestMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(PlanetLandRequestMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for players number request message: it throws a runtime exception
	 * @param message playersNumRequestMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(PlayersNumRequestMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for players number response message: it lets the player know that another player has
	 * picked the number of players before he could (the only way this happens is if multiple players log in
	 * at the same tie and the first one to pick the number of players for the game picks the exact number of
	 * players that have logged in up to that point)
	 * @param message playersNumResponseMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(PlayersNumResponseMessage message) {
		nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new GenericMessage(
						"A player quicker than you has stolen your right to choose the number of players and picked " + numPlayers + "."));
		return nextGameStateAndMessages;
	}

	/**
	 * Visit method for players positions message: it throws a runtime exception
	 * @param message playersPositionsMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(PlayersPositionsMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for positions and turns message: it throws a runtime exception
	 * @param message positionsAndTurnsMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(PositionsAndTurnsMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for pre-made ship component request message: it picks the pre-made ship
	 *  component from the game and it returns the next game state and messages object
	 * @param message premadeShipComponentRequest message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(PremadeShipComponentRequest message) {
		//the player has asked for a covered component
		ShipComponent requestedComponentById = game.getComponentById(message.getId());
		if(requestedComponentById == null){
			nextGameStateAndMessages.setPlayerMessage(message.getNickname(),
					new GenericMessage("Couldn't find that component: " + message.getId()));
			return nextGameStateAndMessages;
		}
		//else
		nextGameStateAndMessages.setPlayerMessage(message.getNickname(),
				new PremadeShipComponentResponse(requestedComponentById));

		return nextGameStateAndMessages;
	}

	/**
	 * Visit method for pre-made ship component response message: it throws a runtime exception
	 * @param message premadeShipComponentResponse message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(PremadeShipComponentResponse message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for projectile trajectory message: it throws a runtime exception
	 * @param message projectileTrajectoryMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(ProjectileTrajectoryMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for return component request message: it returns the component
	 * and it returns the next game state and messages object
	 * @param message returnComponentRequestMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(ReturnComponentRequestMessage message) {
		ShipComponent componentToReturn = message.getComponentToReturn();
		game.revealShipComponent(componentToReturn);
		nextGameStateAndMessages.setPlayerMessage(message.getNickname(),
				new GenericMessage("The component has been turned face up!"));

		//each time a player returns a component, all the players get updated about the new Arraylist of
		// uncovered ship components
		sendUpdatedRevealedComponents();
		return nextGameStateAndMessages;
	}

	/**
	 * Visit method for return small deck message: it returns the small deck
	 * and it returns the next game state and messages object
	 * @param message returnSmallDeckMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(ReturnSmallDeckMessage message) {
		int deckID = message.getDeckId();
		Deck selectedCardPile = game.getSmallDeck(deckID - 1);
		selectedCardPile.setCurrState(false);
		//each time a player returns a deck, all the players get notified of the new free decks
		updateAvailableDecks();

		return nextGameStateAndMessages;
	}

	/**
	 * Visit method for revealed components message: it throws a runtime exception
	 * @param message revealedComponentsMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(RevealedComponentsMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for small deck request message: it picks the small deck
	 * and it returns the next game state and messages object
	 * @param message smallDeckRequestMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(SmallDeckRequestMessage message) {
		int deckID = message.getDeckId();
		Deck selectedCardPile = game.getSmallDeck(deckID - 1);
		if (!selectedCardPile.isUsed()) {
			selectedCardPile.setCurrState(true);
			nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new SmallDeckResponseMessage(selectedCardPile, deckID));
		}
		else
			nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new SmallDeckResponseMessage(null, -1));

		//each time a player takes a deck, all the players get notified of the new free decks
		updateAvailableDecks();

		return nextGameStateAndMessages;
	}

	/**
	 * Visit method for small deck response message: it throws a runtime exception
	 * @param message smallDeckResponseMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(SmallDeckResponseMessage message) {return throwWrongMessageException(message);}

	/**
	 * It sets in the next game state and messages the timer expired
	 * @param isLast true if the timer is the last one
	 */
	private void notifyClientsTimerStarted(boolean isLast) {
		//tell clients the timer has been restarted
		for(int i = 0; i < nicknames.size(); i++) {
			nextGameStateAndMessages.setPlayerMessage(nicknames.get(i), new TimerStartedMessage(game.getTimer().getCurRoundSeconds(), isLast));
		}
	}

	/**
	 * It says if the players has finished his ship
	 * @param nickname player nickname
	 * @return true if the player has finished the ship
	 */
	private boolean didPlayerFinishShip(String nickname){
		if (playersBuilt == null) {
			return false;
		}

		for(ShipDashboard player : playersBuilt){
			if(player.getNickname().equals(nickname))
				return true;
		}

		return false;
	}

	/**
	 * Visit method for start timer request message: it starts the timers
	 * and it returns the next game state and messages object
	 * @param message startTimerRequestMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(StartTimerRequestMessage message) {
		GameTimer timer = game.getTimer();
		if(game.isTimerRunning())
		{
			nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new GenericMessage("The timer is already running."));
			return nextGameStateAndMessages;
		}
		//else
		if(!timer.curRoundIsLast()){
			notifyClientsTimerStarted(false);
			timer.startNextTimer(onTimerExpiredCallback,false);
		}
		//if next round is last
		else {
			if(didPlayerFinishShip(message.getNickname())){
				notifyClientsTimerStarted(true);
				timer.startNextTimer(onTimerExpiredCallback,true);
			}
			else{
				nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new GenericMessage("You can't restart the last timer with an unfinished ship!"));
			}
		}
		return nextGameStateAndMessages;
	}

	/**
	 * Visit method for stocks to add message: it throws a runtime exception
	 * @param message stocksToAddMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(StocksToAddMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for timer expired message: it throws a runtime exception
	 * @param message timerExpiredMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(TimerExpiredMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for timer started message: it throws a runtime exception
	 * @param message timerStartedMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(TimerStartedMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for turn message: it throws a runtime exception
	 * @param message turnMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(TurnMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for update client ship message: it throws a runtime exception
	 * @param message updateClientShipMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(UpdateClientShipMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for update server ship message: it throws a runtime exception
	 * @param message updateServerShipMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(UpdateServerShipMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for waiting partners message: it throws a runtime exception
	 * @param message waitingPartnersMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(WaitingPartnersMessage message) {return throwWrongMessageException(message);}

	/**
	 * Visit method for winners message: it throws a runtime exception
	 * @param message winnersMessage message
	 * @return next game state and messages
	 */
	@Override
	public NextGameStateAndMessages visit(WinnersMessage message) {return throwWrongMessageException(message);}
}
