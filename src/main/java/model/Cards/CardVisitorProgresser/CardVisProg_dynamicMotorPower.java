package model.Cards.CardVisitorProgresser;

import Connections.Messages.*;
import Controller.State.CardDrawing;
import Controller.State.FirePowerChoice;
import Controller.State.GiveUpCrewChoice;
import Controller.State.MotorPowerChoice;
import View.ColorManagement.ConsoleColor;
import model.Cards.*;
import model.Game;
import model.NextGameStateAndMessages;
import model.ShipDashboard;
import model.exceptions.NullCallbackException;
import model.exceptions.OutOfStockException;

import java.util.ArrayList;

/**
 * Card visitor used for cards that require to let the player dynamically choose the motor power.
 * It extends {@link CardVisProg_state(Game, ArrayList, ArrayList)}
 */
public class CardVisProg_dynamicMotorPower extends CardVisProg_state {

	/**
	 * Attribute used to store the message related to this game phase
	 */
	private DynamicMotorPowerMessage message;

	/**
	 * Attribute used to store the value of the static motor power
	 */
	private int staticMotorPower;

	/**
	 * Attribute used to store the value of the dynamic motor power
	 */
	private int dynamicMotorPower;

	/**
	 * Attribute used to store the final motor power
	 */
	private int motorPower;

	/**
	 * Callback to handle the case of zero motor power
	 */
	private ZeroMotorPowerListener onZeroMotorPowerCallback;

	/**
	 * Constructor of the current state to handle player's choice about motor power
	 * @param game is the current game
	 * @param players is the array of players
	 * @param message is the message related to this game phase
	 * @param nicknames is the array of nicknames
	 * @param staticMotorPower is the static motor power
	 * @param dynamicMotorPower is the dynamic motor power
	 * @param motorPower is the total motor power
	 * @param onZeroMotorPowerCallback is the callback to call in case of zero motor power
	 */
	public CardVisProg_dynamicMotorPower(Game game, ArrayList<ShipDashboard> players, Message message, ArrayList<String> nicknames, int staticMotorPower, int dynamicMotorPower, int motorPower, ZeroMotorPowerListener onZeroMotorPowerCallback) {
		super(game, players, nicknames);
		currentGameState = new MotorPowerChoice();
		this.message = (DynamicMotorPowerMessage) message;
		nextGameStateAndMessages = new NextGameStateAndMessages(players);
		this.staticMotorPower = staticMotorPower;
		this.dynamicMotorPower = dynamicMotorPower;
		this.motorPower = motorPower;
		this.onZeroMotorPowerCallback = onZeroMotorPowerCallback;
	}

	/**
	 * Method used to check if the current player is the last one
	 * @param playerIndex is the current player's index
	 * @return a Boolean that is true if the player is the last one
	 */
	private Boolean isLastPlayer(int playerIndex){
		return (playerIndex == players.size() - 1);
	}

	/**
	 * Method used to eliminate a player
	 * @param playerHasToBeEliminated indicates if the players has to be eliminated
	 */
	private void eliminatePlayerIfNecessary(boolean playerHasToBeEliminated){
		if(playerHasToBeEliminated){
			if(onZeroMotorPowerCallback != null){
				onZeroMotorPowerCallback.OnZeroMotorPower(getSenderIndex(message));
				System.out.println("Player eliminated.");
			}
			else
				throw new NullCallbackException("The callback was null.");
		}
	}

	/**
	 * Method to apply the visitor to a card of type {@link OpenSpace}
	 * based on the player's motor power, the player advances;
	 * in case of zero motor power, the player is eliminated.
	 * @param card is the current card to visit
	 * @return the next game state with linked messages
	 */
	@Override
	public NextGameStateAndMessages visit(OpenSpace card) {
		boolean playerHasToBeEliminated = false;
		players.get(getSenderIndex(message)).updateShip(message.getUpdatedShip());
		if (motorPower == 0) {
			nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new GenericMessage("Your motor power is 0! Your trip ends here :("));

			//we can't eliminate the player here because they're needed to know who the next one is supposed to be, in the next if
			playerHasToBeEliminated = true;

		}
		else
			card.setDaysToMove(motorPower, players.get(getSenderIndex(message)));

		if (isLastPlayer(getSenderIndex(message))){
			nextGameStateAndMessages.setNextGameState(new CardDrawing());
			nextGameStateAndMessages.setPlayers(players);
			eliminatePlayerIfNecessary(playerHasToBeEliminated);
			return nextGameStateAndMessages;
		}
		nextGameStateAndMessages.setNextGameState(new MotorPowerChoice());
		nextGameStateAndMessages.setPlayerMessage(players.get(getSenderIndex(message) + 1).getNickname(), new TurnMessage());
		nextGameStateAndMessages.setPlayers(players);

		eliminatePlayerIfNecessary(playerHasToBeEliminated);
		return nextGameStateAndMessages;
	}

	/**
	 * Method to apply the visitor to a card of type {@link CombatZone}
	 * based on the lowest motor power among all the players, this method determines which player is affected
	 * @param card is the current card to visit
	 * @return the next game state with linked messages
	 */
	@Override
	public NextGameStateAndMessages visit(CombatZone card) {
		players.get(getSenderIndex(message)).updateShip(message.getUpdatedShip());
		//initialization of affectedPlayer and lessMotorPower
		if (getSenderIndex(message) == 0) {
			card.setAffectedPlayerIndex(0);
			card.setLowestMotorPower(motorPower);
			if(!isLastPlayer(getSenderIndex(message))){
				nextGameStateAndMessages.setNextGameState(new MotorPowerChoice());
				nextGameStateAndMessages.setPlayerMessage(players.get(getSenderIndex(message) + 1).getNickname(), new TurnMessage());
			}
			else {
				if (card.crewLossLine2 != 0) {
					nextGameStateAndMessages.setNextGameState(new GiveUpCrewChoice());
					nextGameStateAndMessages.setPlayerMessage(players.get(card.getAffectedPlayerIndex()).getNickname(), new TurnMessage());
				}
				if (card.stocksPenaltyLine2 != 0) {
					try {
						players.get(card.getAffectedPlayerIndex()).removeMostPreciousStocks(card.stocksPenaltyLine2);
						nextGameStateAndMessages.setPlayerMessage(players.get(card.getAffectedPlayerIndex()).getNickname(),
								new GenericMessage("You are the player with the weakest engines, so you lost " + card.stocksPenaltyLine2 + " of your most precious stocks or batteries"));
					} catch (OutOfStockException e) {
						nextGameStateAndMessages.setPlayerMessage(message.getNickname(),
								new GenericMessage("You didn't have any more stocks or batteries to steal, so the smugglers just left."));
					}
					nextGameStateAndMessages.setNextGameState(new FirePowerChoice());
					nextGameStateAndMessages.setPlayerMessage(players.get(card.getAffectedPlayerIndex()).getNickname(),
							new UpdateClientShipMessage(players.get(card.getAffectedPlayerIndex())));
					nextGameStateAndMessages.setPlayerMessage(players.getFirst().getNickname(), new TurnMessage());
				}
			}
			nextGameStateAndMessages.setPlayers(players);
			return nextGameStateAndMessages;
		}
		//update of the current AffectedPlayer
		if (motorPower < card.getLowestMotorPower()) {
			card.setAffectedPlayerIndex(getSenderIndex(message));
			card.setLowestMotorPower(motorPower);;
		}
		if (isLastPlayer(getSenderIndex(message))) {
			if (card.crewLossLine2 != 0) {
				nextGameStateAndMessages.setNextGameState(new GiveUpCrewChoice());
				nextGameStateAndMessages.setPlayerMessage(players.get(card.getAffectedPlayerIndex()).getNickname(), new TurnMessage());
			}
			if (card.stocksPenaltyLine2 != 0) {
				try {
					players.get(card.getAffectedPlayerIndex()).removeMostPreciousStocks(card.stocksPenaltyLine2);
					nextGameStateAndMessages.setPlayerMessage(players.get(card.getAffectedPlayerIndex()).getNickname(),
							new GenericMessage("You are the player with the weakest engines, so you lost " + card.stocksPenaltyLine2 + " of your most precious stocks or batteries"));
				} catch (OutOfStockException e) {
					nextGameStateAndMessages.setPlayerMessage(message.getNickname(),
							new GenericMessage("You didn't have any more stocks or batteries to steal, so the smugglers just left."));
				}
				nextGameStateAndMessages.setNextGameState(new FirePowerChoice());
				nextGameStateAndMessages.setPlayerMessage(players.get(card.getAffectedPlayerIndex()).getNickname(),
						new UpdateClientShipMessage(players.get(card.getAffectedPlayerIndex())));
				nextGameStateAndMessages.setPlayerMessage(players.getFirst().getNickname(), new TurnMessage());
			}
		}
		else {
			nextGameStateAndMessages.setNextGameState(new MotorPowerChoice());
			nextGameStateAndMessages.setPlayerMessage(players.get(getSenderIndex(message) + 1).getNickname(), new TurnMessage());
		}
		nextGameStateAndMessages.setPlayers(players);
		return nextGameStateAndMessages;
	}
}
