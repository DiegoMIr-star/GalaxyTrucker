package model.Cards.CardVisitorProgresser;

import Connections.Messages.Message;
import Controller.State.GameState;
import model.Cards.*;
import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.Game;
import model.NextGameStateAndMessages;
import model.ShipDashboard;

import java.util.ArrayList;

/**
 * Card visitor used to implement the different visitors used in the controller
 */
public class CardVisProg_state{

	/**
	 * Current game
	 */
	Game game;

	/**
	 * Current list of players
	 */
	ArrayList<ShipDashboard> players;

	/**
	 * Object next game state and messages
	 */
	NextGameStateAndMessages nextGameStateAndMessages;

	/**
	 * Current game state
	 */
	GameState currentGameState;

	/**
	 * List of players nicknames
	 */
	ArrayList<String> nicknames;

	/**
	 * Class constructor, it initializes all the attributes
	 * @param game current game
	 * @param players current list of players
	 * @param nicknames current list of player nicknames
	 */
	public CardVisProg_state(Game game, ArrayList<ShipDashboard> players, ArrayList<String> nicknames){
		this.game = game;
		this.players = players;
		this.nicknames = nicknames;
	}

	/**
	 * Method used to find the message sender index: it searches in the player list
	 * @param message current message
	 * @return current message sender
	 */
	protected int getSenderIndex(Message message){

		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getNickname().equals(message.getNickname())){
				return i;
			}
		}

		ArrayList<String> names = new ArrayList<>();
		for(int i = 0; i < players.size(); i++){
			names.add(players.get(i).getNickname());
		}

		throw new IndexOutOfBoundsException("Couldn't find the player that sent the message in the players array:\n" + message.getNickname() + " wasn't found in\n" + names);
	}

	/**
	 * The current card is in the wrong state
	 * @param card current stardust card
	 * @throws RuntimeException  run time exception thrown, it prints the current game state
	 */
	public NextGameStateAndMessages visit(Stardust card) {throw new RuntimeException("Unexpected card for " + currentGameState);}

	/**
	 * The current card is in the wrong state
	 * @param card current planets card
	 * @throws RuntimeException  run time exception thrown, it prints the current game state
	 */
	public NextGameStateAndMessages visit(Planets card) {throw new RuntimeException("Unexpected card for " + currentGameState);}

	/**
	 * The current card is in the wrong state
	 * @param card current open space card
	 * @throws RuntimeException  run time exception thrown, it prints the current game state
	 */
	public NextGameStateAndMessages visit(OpenSpace card) {throw new RuntimeException("Unexpected card for " + currentGameState);}

	/**
	 * The current card is in the wrong state
	 * @param card current meteor swarm card
	 * @throws RuntimeException  run time exception thrown, it prints the current game state
	 */
	public NextGameStateAndMessages visit(MeteorSwarm card) {throw new RuntimeException("Unexpected card for " + currentGameState);}

	/**
	 * The current card is in the wrong state
	 * @param card current epidemic card
	 * @throws RuntimeException  run time exception thrown, it prints the current game state
	 */
	public NextGameStateAndMessages visit(Epidemic card) {throw new RuntimeException("Unexpected card for " + currentGameState);}

	/**
	 * The current card is in the wrong state
	 * @param card current combat zone card
	 * @throws RuntimeException  run time exception thrown, it prints the current game state
	 */
	public NextGameStateAndMessages visit(CombatZone card) {throw new RuntimeException("Unexpected card for " + currentGameState);}

	/**
	 * The current card is in the wrong state
	 * @param card current abandoned ship card
	 * @throws RuntimeException  run time exception thrown, it prints the current game state
	 */
	public NextGameStateAndMessages visit(AbandonedShip card) {throw new RuntimeException("Unexpected card for " + currentGameState);}

	/**
	 * The current card is in the wrong state
	 * @param card current abandoned station card
	 * @throws RuntimeException  run time exception thrown, it prints the current game state
	 */
	public NextGameStateAndMessages visit(AbandonedStation card) {throw new RuntimeException("Unexpected card for " + currentGameState);}

	/**
	 * The current card is in the wrong state
	 * @param card current smugglers card
	 * @throws RuntimeException  run time exception thrown, it prints the current game state
	 */
	public NextGameStateAndMessages visit(Smugglers card) {throw new RuntimeException("Unexpected card for " + currentGameState);}

	/**
	 * The current card is in the wrong state
	 * @param card current slavers card
	 * @throws RuntimeException  run time exception thrown, it prints the current game state
	 */
	public NextGameStateAndMessages visit(Slavers card) {throw new RuntimeException("Unexpected card for " + currentGameState);}

	/**
	 * The current card is in the wrong state
	 * @param card current pirates card
	 * @throws RuntimeException  run time exception thrown, it prints the current game state
	 */
	public NextGameStateAndMessages visit(Pirates card) {throw new RuntimeException("Unexpected card for " + currentGameState);}

	/**
	 * Method used for testing: it's only a setter
	 * @param state current state
	 */
	public void setGameState(GameState state){this.currentGameState = state;}
}
