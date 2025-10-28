package model;

import Connections.Messages.Message;
import Controller.State.GameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The class is used in the controller to return the current game state with the messages to send
 * to the specific players.
 * @see Controller.Controller
 */
public class NextGameStateAndMessages {

	/**
	 * Next state
	 */
	GameState nextGameState;

	/**
	 * List of messages to send
	 */
	Map<String, ArrayList<Message>> messages;

	/**
	 * List of different players
	 */
	ArrayList<ShipDashboard> players;

	/**
	 * Constructor of the class: it initializes all the attributes
	 * @param players list of players of the current game
	 */
	public NextGameStateAndMessages(ArrayList<ShipDashboard> players) {
		nextGameState = null;
		messages = new HashMap<>();
		if(players != null) {
			for (ShipDashboard player : players) {
				messages.put(player.getNickname(), new ArrayList<>());
			}
			setPlayers(players);
		}
	}

	/**
	 * Setter of players
	 * @param players current list of players
	 */
	public void setPlayers(ArrayList<ShipDashboard> players) {this.players = players;}

	/**
	 * Getter of players
	 * @return list of the current players
	 */
	public ArrayList<ShipDashboard> getPlayers(){return players;}

	/**
	 * Getter of the next state
	 * @return next state
	 */
	public GameState getNextGameState() {return nextGameState;}

	/**
	 * Setter of the state
	 * @param nextGameState state to set
	 */
	public void setNextGameState(GameState nextGameState) {this.nextGameState = nextGameState;}

	/**
	 * Getter of the messages per player
	 * @return map of messages per player
	 */
	public Map<String, ArrayList<Message>> getPlayerMessages() {return messages;}
	/*public void setPlayerMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}*/

	/**
	 * Getter of messages per single player
	 * @param nickname player to analise
	 * @return list of messages
	 */
	public ArrayList<Message> getPlayerMessage(String nickname) {return messages.get(nickname);}

	/**
	 * Setter of specific message to specific player
	 * @param nickname player to analise
	 * @param message specific message to add
	 */
	public void setPlayerMessage(String nickname, Message message) {
		if(!messages.containsKey(nickname)) {
			messages.put(nickname, new ArrayList<>());
		}
		messages.get(nickname).add(message);
	}
}
