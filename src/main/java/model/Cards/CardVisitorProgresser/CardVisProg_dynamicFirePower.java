package model.Cards.CardVisitorProgresser;

import Connections.Messages.*;
import Controller.State.*;
import model.Cards.*;
import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.Game;
import model.NextGameStateAndMessages;
import model.ShipDashboard;
import model.exceptions.OutOfStockException;

import java.util.ArrayList;

/**
 * Card visitor used for cards that require to let the player dynamically choose the firepower.
 * It extends {@link CardVisProg_state(Game, ArrayList, ArrayList)}
 */
public class CardVisProg_dynamicFirePower  extends CardVisProg_state{

	/**
	 * Attribute used to store the value of the static firepower
	 */
	double staticFirePower;

	/**
	 * Attribute used to store the value of the dynamic firepower
	 */
	double dynamicFirePower;

	/**
	 * Attribute used to store the final firepower
	 */
	double firePower;

	/**
	 * Attribute used to store the message related to this game phase
	 */
	Message message;

	/**
	 * Constructor of the current state to handle player's choice about firepower
	 * @param game is the current game
	 * @param players is the array of players
	 * @param message is the message related to this game phase
	 * @param nicknames is the array of nicknames
	 * @param staticFirePower is the static firepower
	 * @param dynamicFirePower is the dynamic firepower
	 * @param firePower is the total firepower
	 */
	public CardVisProg_dynamicFirePower(Game game, ArrayList<ShipDashboard> players, Message message, ArrayList<String> nicknames, double staticFirePower, double dynamicFirePower, double firePower) {
		super(game, players, nicknames);
		currentGameState = new FirePowerChoice();
		this.staticFirePower = staticFirePower;
		this.dynamicFirePower = dynamicFirePower;
		this.firePower = firePower;
		this.message = message;
		nextGameStateAndMessages = new NextGameStateAndMessages(players);
	}

	/**
	 * Method used to check if the current player is the last one
	 * @param playerIndex is the current player's index
	 * @return a Boolean that is true if the player is the last one
	 */
	private Boolean isLastPlayer(int playerIndex){
		return (playerIndex == this.players.size() - 1);
	}

	/**
	 * Method to apply the visitor to a card of type {@link CombatZone}
	 * based on the lowest firepower among all the players, this method determines which player is affected
	 * @param card is the current card to visit
	 * @return the next game state with linked messages
	 */
	@Override
	public NextGameStateAndMessages visit(CombatZone card) {
		//initialization of affectedPlayer and lessMotorPower
		if (getSenderIndex(message) == 0) {
			card.setAffectedPlayerIndex(getSenderIndex(message));
			card.setLowestFirePower(firePower);
		}
		//update of the current AffectedPlayer
		if (firePower < card.getLowestFirePower()) {
			card.setAffectedPlayerIndex(getSenderIndex(message));
			card.setLowestFirePower(firePower);
		}
		if (isLastPlayer(getSenderIndex(message))) {
			nextGameStateAndMessages.setNextGameState(new ManageProjectile());
			//notify turn not needed, this message serves that purpose as well
			nextGameStateAndMessages.setPlayerMessage(players.get(card.getAffectedPlayerIndex()).getNickname(),
					new ProjectileTrajectoryMessage(card.getNextHitToHandle(), card.getRandomTrajectory()));
		}
		else {
			nextGameStateAndMessages.setPlayerMessage(players.get(getSenderIndex(message)+1).getNickname(), new TurnMessage());
		}

		nextGameStateAndMessages.setPlayers(players);
		return nextGameStateAndMessages;
	}

	/**
	 * Method to apply the visitor to a card of type {@link Smugglers}
	 * based on the comparison of the player's firepower with the Smugglers one, this method determines the players affected
	 * The affected players lose the most precious stocks.
	 * @param card is the current card to visit
	 * @return the next game state with linked messages
	 */
	@Override
	public NextGameStateAndMessages visit(Smugglers card) {
		if (handleVictoryOrDrawWithEnemy(card.firePower, firePower))
			return nextGameStateAndMessages;

		try{
			players.get(getSenderIndex(message)).removeMostPreciousStocks(card.penaltyStocks);
			nextGameStateAndMessages.setPlayerMessage(message.getNickname(),
					new GenericMessage("You have lost " + card.penaltyStocks + " of your most precious stocks or batteries."));
		} catch (OutOfStockException e) {
			nextGameStateAndMessages.setPlayerMessage(message.getNickname(),
					new GenericMessage("You didn't have any more stocks or batteries to steal, so the smugglers just left."));
		}
		ShipDashboard ship = players.get(getSenderIndex(message));
		ship.initializeShipAttributesFromComponents();
		nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new UpdateClientShipMessage(ship));

		if(isLastPlayer(getSenderIndex(message))) {
			nextGameStateAndMessages.setNextGameState(new CardDrawing());
		}
		else
			nextGameStateAndMessages.setPlayerMessage(players.get(getSenderIndex(message) + 1).getNickname(),
					new TurnMessage());
		nextGameStateAndMessages.setPlayers(players);
		return nextGameStateAndMessages;
	}

	/**
	 * Method to apply the visitor to a card of type {@link Slavers}
	 * based on the comparison of the player's firepower with the Slavers one, this method determines the players affected
	 * The affected players lose crew members.
	 * @param card is the current card to visit
	 * @return the next game state with linked messages
	 */
	@Override
	public NextGameStateAndMessages visit(Slavers card) {
		if (handleVictoryOrDrawWithEnemy(card.firePower, firePower))
			return nextGameStateAndMessages;

		nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new GenericMessage(
				"Your fire power wasn't high enough to defeat the slavers."));
		nextGameStateAndMessages.setNextGameState(new GiveUpCrewChoice());
		nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new TurnMessage());

		nextGameStateAndMessages.setPlayers(players);
		return nextGameStateAndMessages;
	}

	/**
	 * Method to apply the visitor to a card of type {@link Pirates}
	 * based on the comparison of the player's firepower with the Pirates one, this method determines the players affected
	 * The affected players receive a random trajectory projectile.
	 * @param card is the current card to visit
	 * @return the next game state with linked messages
	 */
	@Override
	public NextGameStateAndMessages visit(Pirates card) {
		if (handleVictoryOrDrawWithEnemy(card.firePower, firePower))
			return nextGameStateAndMessages;

		if (card.getNextShotTrajectory() == -1)
			card.randomizeTrajectory(card.getIndexOfNextShotToHandle());

		nextGameStateAndMessages.setNextGameState(new ManageProjectile());
		//notify turn not needed, this message serves that purpose as well
		nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new ProjectileTrajectoryMessage(card.getNextShotToHandle(), card.getNextShotTrajectory()));
		nextGameStateAndMessages.setPlayers(players);
		return nextGameStateAndMessages;

	}

	/**
	 * Method to check if a player has enough firepower in order to not be affected
	 * @param enemyFirePower is the firepower of the enemy to compare
	 * @param playerFirePower is the firepower of the current player considered
	 * @return a boolean that is true if the player doesn't lose against the enemy
	 */
	private boolean handleVictoryOrDrawWithEnemy(double enemyFirePower, double playerFirePower) {
		if(playerFirePower > enemyFirePower) {
			nextGameStateAndMessages.setNextGameState(new ClaimRewardChoice());
			nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new TurnMessage());
			nextGameStateAndMessages.setPlayers(players);
			return true;
		}
		else if(playerFirePower == enemyFirePower) {
			if( !isLastPlayer(getSenderIndex(message))) {
				nextGameStateAndMessages.setNextGameState(new FirePowerChoice());
				nextGameStateAndMessages.setPlayerMessage(players.get(getSenderIndex(message) + 1).getNickname(),
						new TurnMessage());
			}
			else {
				nextGameStateAndMessages.setNextGameState(new CardDrawing());
				nextGameStateAndMessages.setPlayerMessage(players.getFirst().getNickname(),
						new TurnMessage());
			}
			nextGameStateAndMessages.setPlayers(players);
			return true;
		}
		return false;
	}
}
