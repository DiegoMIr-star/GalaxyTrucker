package model.Cards.CardVisitorProgresser;

import Connections.Messages.GenericMessage;
import Connections.Messages.ProjectileTrajectoryMessage;
import Connections.Messages.TurnMessage;
import Connections.Messages.UpdateServerShipMessage;
import Controller.State.CardDrawing;
import Controller.State.FirePowerChoice;
import Controller.State.ManageProjectile;
import model.Cards.*;
import model.Cards.Enemies.Pirates;
import model.Game;
import model.NextGameStateAndMessages;
import model.ShipDashboard;

import java.util.ArrayList;

/**
 * Visitor class used to apply the effect of cards with manage projectiles like Meteor Swarm,
 * Pirates and Combat Zone in the Controller
 */
public class CardVisProg_manageProjectile  extends CardVisProg_state {

	/**
	 * Current update server ship message
	 */
	UpdateServerShipMessage message;

	/**
	 * Class constructor used to initialize all the attributes
	 * @param game current game
	 * @param players current players
	 * @param message current update server ship message
	 * @param nicknames current list of nicknames
	 */
	public CardVisProg_manageProjectile (Game game, ArrayList<ShipDashboard> players, UpdateServerShipMessage message, ArrayList<String> nicknames) {
		super(game, players, nicknames);
		currentGameState = new ManageProjectile();
		nextGameStateAndMessages = new NextGameStateAndMessages(players);
		this.message = message;
	}

	/**
	 * Method used to apply the Meteor Swarm effect
	 * @param card current meteor swarm card
	 * @return next game state and messages object, which contains the next state and the
	 *         messages to be sent
	 */
	@Override
	public NextGameStateAndMessages visit(MeteorSwarm card) {
		card.markPlayerAsDoneManagingCurrentMeteor();
		//every player has handled the current meteor
		if (card.getPlayersDoneManagingCurrentMeteor() >= players.size()) {
			card.markMeteorAsHandled();
			if (!card.allMeteorsHandled()) {
				if (card.getNextMeteorTrajectory() == -1)
					card.randomizeTrajectory(card.getIndexOfNextMeteorToHandle());

				nextGameStateAndMessages.setNextGameState(new ManageProjectile());
				for (int i = 0; i < players.size(); i++) {
					//notify turn not needed, this message serves that purpose as well
					nextGameStateAndMessages.setPlayerMessage(nicknames.get(i), new ProjectileTrajectoryMessage(card.getNextMeteorToHandle(), card.getNextMeteorTrajectory()));
				}
			}
			//every player has handled every meteor
			else {
				nextGameStateAndMessages.setNextGameState(new CardDrawing());
			}
		} else {
			nextGameStateAndMessages.setPlayerMessage(players.get(getSenderIndex(message)).getNickname(),
					new GenericMessage("Waiting for the other players to finish handling the meteor..."));
			//nothing changes. We keep waiting until every player has handled the current meteor.
		}

		nextGameStateAndMessages.setPlayers(players);
		return nextGameStateAndMessages;
	}

	/**
	 * Method used to apply the Combat Zone effect
	 * @param card current combat zone card
	 * @return next game state and messages object, which contains the next state and the
	 *         messages to be sent
	 */
	@Override
	public NextGameStateAndMessages visit(CombatZone card) {
		card.markShotAsHandled();
		if (!card.allShotsHandled()) {
			nextGameStateAndMessages.setPlayerMessage(players.get(card.getAffectedPlayerIndex()).getNickname(),
					new ProjectileTrajectoryMessage(card.getNextHitToHandle(), card.getRandomTrajectory()));
		} else {
			card.setAffectedPlayerIndex(-1);
			card.setLowestFirePower(-1);

			nextGameStateAndMessages.setNextGameState(new CardDrawing());
		}
		nextGameStateAndMessages.setPlayers(players);
		return nextGameStateAndMessages;
	}

	/**
	 * It checks if the current player is the last one in the list
	 * @param playerIndex current player index
	 * @return true if the player is the last one
	 */
	private Boolean isLastPlayer(int playerIndex){return (playerIndex == this.players.size() - 1);}

	/**
	 * If there are more players, it's their turn to deal with the card, otherwise
	 * it's time to set the card drawing state
	 */
	private void startNextTurnAgainstENEMY_orFinishCard() {
		// if there are more players, it's their turn to deal with the card
		if(!isLastPlayer(getSenderIndex(message))){
			nextGameStateAndMessages.setNextGameState(new FirePowerChoice());
			nextGameStateAndMessages.setPlayerMessage(players.get(getSenderIndex(message) + 1).getNickname(), new TurnMessage());
		}
		//otherwise, it's time to draw a new card
		else{
			nextGameStateAndMessages.setNextGameState(new CardDrawing());
		}
	}

	/**
	 * Method used to apply the Pirates effect
	 * @param card current pirates card
	 * @return next game state and messages object, which contains the next state and the
	 *         messages to be sent
	 */
	@Override
	public NextGameStateAndMessages visit(Pirates card) {
		card.markShotAsHandled();
		if (!card.allShotsHandled()) {
			if (card.getNextShotTrajectory() == -1)
				card.randomizeTrajectory(card.getIndexOfNextShotToHandle());

			nextGameStateAndMessages.setNextGameState(new ManageProjectile());
			//notify turn not needed, this message serves that purpose as well
			nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new ProjectileTrajectoryMessage(card.getNextShotToHandle(), card.getNextShotTrajectory()));
		}
		//if the player is done handling the shots, it's the next player's turn, if there is one
		else {
			card.prepareShotsForNewTurn();
			startNextTurnAgainstENEMY_orFinishCard();
		}
		nextGameStateAndMessages.setPlayers(players);
		return nextGameStateAndMessages;
	}
}
