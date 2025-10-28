package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.Card;
import model.DifferentShipComponents.ShipComponent;
import model.exceptions.InvalidIndexException;
import model.exceptions.OutOfAvailableCardsException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class used to represent a current game
 * @see Controller.Controller
 */
public class Game implements Serializable {

	/**
	 * Arraylist of the ShipDashboards of the current players
	 */
	private ArrayList<ShipDashboard> players;

	/**
	 * Amount of level 2 cards needed in a small deck
	 */
	private final int cardLvl2 = 2;

	/**
	 * Amount of level 1 cards needed in a small deck
	 */
	private final int cardLvl1 = 1;

	/**
	 * One of four small decks used during the game
	 */
	private final Deck smallDeck1;

	/**
	 * One of four small decks used during the game
	 */
	private final Deck smallDeck2;

	/**
	 * One of four small decks used during the game
	 */
	private final Deck smallDeck3;

	/**
	 * One of four small decks used during the game
	 */
	private final Deck smallDeck4;

	/**
	 * Ensemble of the 4 small decks
	 */
	private final Deck totalDeck;

	/**
	 * Length of the path on the dashboard of the game
	 */
	public final int pathLength;

	/**
	 * Bank used in the game
	 * @see Bank
	 */
	private final Bank bank;

	/**
	 * Permanent list of all cards of the game
	 */
	private final Deck gameDeck;

	/**
	 * List of ship components which are still covered:
	 * they are asked form the players during the construction of their ship
	 */
	private final ArrayList<ShipComponent> coveredShipComponents;

	/**
	 * List of ship components which are uncovered:
	 * they are used during the construction of their ship
	 */
	private final ArrayList<ShipComponent> uncoveredShipComponents;

	/**
	 * List of small decks
	 */
	private final ArrayList<Deck> smallDecks;

	/**
	 * Game Timer for constructing the ships
	 */
	private transient GameTimer gameTimer;

	/**
	 * Constructor of a game: it initializes the attributes
	 * @param bank bank used during the game
	 * @param pathLength current length on the dashboard
	 * @param gameDeck big deck with all the cards used
	 * @param shipComponents list of components used in the game for the construction
	 *                       of the ship
	 */
	public Game(@JsonProperty("bank") Bank bank, @JsonProperty("pathLength") int pathLength,
					@JsonProperty("gameDeck") Deck gameDeck, @JsonProperty("coveredShipComponents") ArrayList<ShipComponent> shipComponents) {
		//the small decks will be created by the Controller, and the total deck will be created in parallel by this class
		smallDecks = new ArrayList<>();
		this.smallDeck1 = new Deck(cardLvl2, cardLvl1);
		smallDecks.add(smallDeck1);
		this.smallDeck2 = new Deck(cardLvl2, cardLvl1);
		smallDecks.add(smallDeck2);
		this.smallDeck3 = new Deck(cardLvl2, cardLvl1);
		smallDecks.add(smallDeck3);
		this.smallDeck4 = new Deck(cardLvl2, cardLvl1);
		smallDecks.add(smallDeck4);
		this.totalDeck = new Deck(cardLvl2 * 4, cardLvl1 * 4);
		this.bank = bank;
		this.gameDeck = gameDeck;
		coveredShipComponents = shipComponents;
		uncoveredShipComponents = new ArrayList<>();
		this.pathLength = pathLength;
		System.out.println("New game timer created\n\n");
		gameTimer = new GameTimer();
	}

	/**
	 * It returns a random value between 2 and 12 as value of dices
	 * @return random value
	 */
	public static int rollTheDice() {
		Random diceValue = new Random();
		return (diceValue.nextInt(6) + 1) + (diceValue.nextInt(6) + 1);
	}

	/**
	 * Setter of the game timer for persistence
	 */
	public void setTimerPersistence(){ gameTimer = new GameTimer();}

	/**
	 * It adds into the deck passed as parameter a random card of a certain level:
	 * it's called as much as the cards of a certain level
	 * @see #initializeDecks()
	 * @param deck specific deck
	 * @param level level of the wanted card
	 * @throws OutOfAvailableCardsException thrown when there's no card of a certain level
	 */
	private void addCardToDeck(Deck deck, int level) throws OutOfAvailableCardsException {
		//in GameDeck, the cards are ordered according to the level (I or II),
		// and the number of cards for each level is known
		try {
		//	int deckCardOffset = 21;
			if (level == 1) {
				int i = (int)(Math.random() * gameDeck.getNumLvl1());
			//	int i = ((gameDeck.getCards().size() - 1 - deckCardOffset) + gameDeck.getCards().size()) % gameDeck.getCards().size();
			//	System.out.println("picking card i = " + i + ": \n" + gameDeck.getCards().get(i));

				if (gameDeck.getNumLvl1() <= 0) {
					throw new OutOfAvailableCardsException("There is no available card of level I");
				} else {
					Card cardToBeAdded = gameDeck.getCard(i);
					//the card added to the small deck is added to the final deck too
					totalDeck.addCardToDeck(cardToBeAdded);
					deck.addCardToDeck(cardToBeAdded);
				}
			} else if (level == 2) {
				int i = (int)(Math.random() * (gameDeck.getSize() - gameDeck.getNumLvl1())) + gameDeck.getNumLvl1();
			//	int i = ((gameDeck.getCards().size() - 1 - deckCardOffset) + gameDeck.getCards().size()) % gameDeck.getCards().size();
			//	System.out.println("picking card i = " + i + ": \n" + gameDeck.getCards().get(i));

				if (gameDeck.getSize() - gameDeck.getNumLvl1() <= 0) {
					throw new OutOfAvailableCardsException("There is no available card of level II");
				} else {
					Card cardToBeAdded = gameDeck.getCard(i);
					totalDeck.addCardToDeck(cardToBeAdded);
					deck.addCardToDeck(cardToBeAdded);
				}
			}
		} catch (InvalidIndexException e) {
			System.out.println(e + "It doesn't exist a card of this level. Digit 1 or 2");
		}
	}

	/**
	 * this method will be called by the Controller at the beginning of the game.
	 * At the end of the method, both the small decks and the
	 * total deck will be initialized.
	 */
	public void initializeDecks() {
		//initializes cards of level II
		for (int i = 0; i < cardLvl2; i++) {
			addCardToDeck(smallDeck1, 2);
			addCardToDeck(smallDeck2, 2);
			addCardToDeck(smallDeck3, 2);
			addCardToDeck(smallDeck4, 2);
		}
		//initializes cards of level I
		for (int i = 0; i < cardLvl1; i++) {
			addCardToDeck(smallDeck1, 1);
			addCardToDeck(smallDeck2, 1);
			addCardToDeck(smallDeck3, 1);
			addCardToDeck(smallDeck4, 1);
		}

		smallDeck4.setCurrState(true);
	}

	/**
	 * It returns into the deck an amount of stocks
	 * @param stocks stocks to return
	 */
	public void returnStocks(Stocks stocks) {bank.addStocks(stocks);}

	/**
	 * Method used to update the position of the players according to their
	 * days to move
	 * @param players list of players
	 */
	public void updatePositions(ArrayList<ShipDashboard> players) {
		if(players == null)
			return;
		int[] oldPositions = new int[players.size()];
		int[] daysToMove = new int[players.size()];
		int i = 0;
		int j = 0;

		for (ShipDashboard player : players) {
			oldPositions[i] = player.getPosition();
			i++;
		}
		for (ShipDashboard player : players) {
			daysToMove[j] = player.getDaysToMove();
			j++;
		}

		int days = daysToMove[0];
		i = 0;
		while (days == 0 && i < daysToMove.length) {
			days = daysToMove[i];
			i++;
		}


		//updating position in route order, from the leader to the last player
		if (days > 0) {
			i = 0;
			for (j = 0; j < oldPositions.length; j++) {
				int newPosition = getNewPositionPositiveDays(oldPositions, i, daysToMove);
				//the new position is updated in the attribute position of the player, and the Days to move are set to 0
				oldPositions[i] = newPosition;
				players.get(i).setPosition(newPosition);
				players.get(i).setDaysToMove(0);
				i++;
			}
		}
		//updating position in counter route order, from the last player to the leader
		else if (days < 0){
			i = players.size() - 1;
			for (j = oldPositions.length - 1; j >= 0; j--) {
				int newPosition = getNewPositionNegativeDays(oldPositions, i, daysToMove);
				//the new position is updated in the attribute position of the player, and the Days to move are set to 0
				oldPositions[i] = newPosition;
				players.get(i).setPosition(newPosition);
				players.get(i).setDaysToMove(0);
				i--;
			}
		}

		players.sort((p1, p2) -> p2.getPosition() - p1.getPosition());
	}

	/**
	 * It gives the new position of a certain player when the days to move
	 * are negative
	 * @see #updatePositions(ArrayList) where it's called
	 * @param oldPositions current positions of players, positions which need to be
	 *                     updated
	 * @param i index of the current player
	 * @param daysToMove amount of days to move
	 * @return new position
	 */
	private static int getNewPositionNegativeDays(int[] oldPositions, int i, int[] daysToMove) {
		int newPosition = oldPositions[i];
		int days = daysToMove[i];
		while (days != 0) {
			//days have to be added to the old positions and have to be decremented to reach the 0 value
			//the opposite if days are negative
			if (days < 0) {
				newPosition--;
                /*if (newPosition < 1) {
                    newPosition = pathLength;
                }*/
				int k = oldPositions.length - 1;
				while (k >= 0) {
					//if the new position is occupied by a player, it isn't counted
					if (oldPositions[k] == newPosition) {
						break;
					}
					k--;
				}
				// if there are no other players in the new position, the days get decremented. if not, they don't.
				if (k == -1) {
					days++;
				}
			}
		}
		return newPosition;
	}

	/**
	 * It gives the new position of a certain player when the days to move
	 * are positive
	 * @see #updatePositions(ArrayList) where it's called
	 * @param oldPositions current positions of players, positions which need to be
	 *                     updated
	 * @param i index of the current player
	 * @param daysToMove amount of days to move
	 * @return new position
	 */
	private static int getNewPositionPositiveDays(int[] oldPositions, int i, int[] daysToMove) {
		int newPosition = oldPositions[i];
		int days = daysToMove[i];
		while (days != 0) {
			//days have to be added to the old positions and have to be decremented to reach the 0 value
			if (days > 0) {
				newPosition++;
				int k = 0;
				while (k < i) {
					//if the new position is occupied by a player, it isn't counted
					if (oldPositions[k] == newPosition) {
						break;
					}
					k++;
				}
				// if there are no other players in the new position, the days get decremented. if not, they don't.
				if (k == i) {
					days--;
				}
			}
		}
		return newPosition;
	}

	/**
	 * According to the players passed as parameter, it returns a list of winners
	 * @param players list of players to evaluate for the winning
	 * @return list of winners
	 */
	public ArrayList<ShipDashboard> endGame(ArrayList<ShipDashboard> players){
		ArrayList<ShipDashboard> winners = new ArrayList<>();
		//recompenses of credits for the order of arrival
		for (int i = 0; i < players.size(); i++) {
			if(i==0){
				players.get(i).bonusMalus(0, 8);
			}
			if(i==1){
				players.get(i).bonusMalus(0, 6);
			}
			if(i==2){
				players.get(i).bonusMalus(0, 4);
			}
			if(i==3){
				players.get(i).bonusMalus(0, 2);
			}
		}
		//most beautiful ship
		int minConnectors=255;
		for (ShipDashboard player : players) {
			minConnectors = Math.min(minConnectors, player.getNumOfExposedConnectors());
		}
		for (ShipDashboard player : players) {
			if (player.getNumOfExposedConnectors() == minConnectors) {
				player.bonusMalus(0, 4);
			}
		}
		//to return stocks and to gain credits
		for (ShipDashboard player : players) {
			player.bonusMalus(0, player.getStocks().getBlueStocks());
			player.bonusMalus(0, 2 * player.getStocks().getGreenStocks());
			player.bonusMalus(0, 3 * player.getStocks().getYellowStocks());
			player.bonusMalus(0, 4 * player.getStocks().getSpecialRedStocks());
			//move to bank
			bank.addStocks(player.getStocks());
			player.removeStocks(player.getStocks());
		}
		//loss for lost components
		for (ShipDashboard player : players) {

			player.bonusMalus(0, -player.getGarbageHeap());

		}
		//winner?
		for (ShipDashboard player : players) {
			if (player.getCredits() >= 1) {
				//declare winners
				winners.add(player);
			}
		}
		return winners;
	}

	/**
	 * It checks if the player has a sufficient crew to continue the game
	 * @param players list of players to check
	 * @return indexes of eliminated players
	 */
	public ArrayList<Integer> checkForfeitForInsufficientCrew(ArrayList<ShipDashboard> players) {
		ArrayList<Integer> eliminatedPlayers = new ArrayList<>();
		//if the number of humans is 0, the player has to abandon the game
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getCrew() == 0) {
				eliminatedPlayers.add(i);
			}
		}
		return eliminatedPlayers;
	}

	/**
	 * It checks if the player is doubled by the leader
	 * @param players list of players
	 * @return indexes of eliminated players
	 */
	public ArrayList<Integer> checkForfeitForDoubledPosition(ArrayList<ShipDashboard> players) {
		ArrayList<Integer> eliminatedPlayers = new ArrayList<>();
		//if a player is doubled by the leader, the player has to abandon the game
		ShipDashboard leader = players.getFirst();
		for (int i = 1; i < players.size(); i++) {
			if (leader.getPosition() >= players.get(i).getPosition()+ pathLength) {

				eliminatedPlayers.add(i);
			}
		}
		return eliminatedPlayers;
	}

	public void startFirstTimer(TimerListener callback){
		gameTimer.setCurTimerRound(0);
		gameTimer.startNextTimer(callback, false);
	}

	/**
	 * This method adds an uncovered ship component:
	 * it's called every time the player returns a component to the server
	 * @param componentToUncover specific component to add to the uncovered
	 */
	public void revealShipComponent(ShipComponent componentToUncover) {uncoveredShipComponents.add(componentToUncover);}

	/**
	 * Getter of the uncovered ship components
	 * @return list of uncovered ship components
	 */
	public ArrayList<ShipComponent> getRevealedShipComponents() {return uncoveredShipComponents;}

	/**
	 * Getter of a specific ship component, which is removed when revealed
	 * @param index index of the specific component
	 * @return specific component
	 */
	public ShipComponent getRevealedShipComponent(int index) {return uncoveredShipComponents.remove(index);}

	/**
	 * It returns the first covered component, which is also removed from covered
	 * @return first covered component
	 */
	public ShipComponent getCoveredShipComponent() {
		if(!coveredShipComponents.isEmpty()){
			Collections.shuffle(coveredShipComponents);
			return coveredShipComponents.removeFirst();
		}
		else
			return null;
	}

	/**
	 * It returns a specific component by its ID, regardless if
	 * it's covered or uncovered
	 * @param id ID of specific component
	 * @return specific component
	 */
	public ShipComponent getComponentById(int id){

		for(ShipComponent component : uncoveredShipComponents){
			if(component.getID() == id){
				return component;
			}
		}

		for(ShipComponent component : coveredShipComponents){
			if(component.getID() == id){
				return component;
			}
		}

		return null;
	}

	/**
	 *
	 * @return the ArrayList of ShipDashboards of the current players
	 */

	public ArrayList<ShipDashboard> getPlayers() {
		return players;
	}

	/**
	 * Getter of the total deck, composition of small decks
	 * @return total deck
	 */
	public Deck getTotalDeck() {return totalDeck;}

	/**
	 * Getter of the game's full deck, including every card of the game
	 * @return game deck
	 */
	public Deck getGameDeck() {return gameDeck;}

	/**
	 * Getter of the index-th small deck
	 * @param index index of specific card pile
	 * @return first small deck
	 */
	public Deck getSmallDeck(int index) {return smallDecks.get(index);}

	/**
	 * Getter of the first small deck
	 * @return first small deck
	 */
	public Deck getSmallDeck1() {return smallDeck1;}

	/**
	 * Getter of the second small deck
	 * @return second small deck
	 */
	public Deck getSmallDeck2() {return smallDeck2;}

	/**
	 * Getter of the third small deck
	 * @return third small deck
	 */
	public Deck getSmallDeck3() {return smallDeck3;}

	/**
	 * Getter of the fourth small deck
	 * @return fourth small deck
	 */
	public Deck getSmallDeck4() {return smallDeck4;}

	/**
	 * Getter of the small decks
	 * @return list of small decks
	 */
	public ArrayList<Deck> getDecks() {return smallDecks;}

	/**
	 * Getter of the game timer
	 * @return the timer
	 */
	public GameTimer getTimer() {return gameTimer;}

	/**
	 * Getter of the game timer
	 * @return the timer
	 */
	public boolean isTimerRunning() {return gameTimer.isTimerRunning();}
}