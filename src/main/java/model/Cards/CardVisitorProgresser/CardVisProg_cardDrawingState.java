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

import java.util.ArrayList;

import static model.Game.rollTheDice;

/**
 * Card visitor used for managing the state transitions after a new card is drawn
 * It extends {@link CardVisProg_state(Game, ArrayList, ArrayList)}
 */
public class CardVisProg_cardDrawingState  extends CardVisProg_state{

    /**
     * Constructor of the current state to handle the new card drawn state
     * @param game is the current game
     * @param players is the array of players
     * @param nicknames is the array of nicknames
     */
    public CardVisProg_cardDrawingState (Game game, ArrayList<ShipDashboard> players, ArrayList<String> nicknames) {
        super(game, players, nicknames);
        currentGameState = new CardDrawing();
        nextGameStateAndMessages = new NextGameStateAndMessages(players);
    }

    /**
     * Method to apply the visitor to a card of type {@link Stardust}
     * it applies the new card effects and sets card drawing as new state
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(Stardust card) {
        card.apply(players);
        nextGameStateAndMessages.setNextGameState(new CardDrawing());
        for (int i = 0; i < players.size(); i++) {
            nextGameStateAndMessages.setPlayerMessage(players.get(i).getNickname(), new GenericMessage("You lost " + (-players.get(i).getDaysToMove()) + " days."));
        }
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Method to apply the visitor to a card of type {@link Planets}
     * it sets planets landing choice as the next state, sending the corresponding message to the first player
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(Planets card) {
        nextGameStateAndMessages.setNextGameState(new PLANETS_LandingChoice());
        nextGameStateAndMessages.setPlayerMessage(players.getFirst().getNickname(), new FreePlanetsResponseMessage(card.getFreePlanets()));
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Method to apply the visitor to a card of type {@link OpenSpace}
     * it sets motor power choice as the next state and notifies the turn for the first player
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(OpenSpace card) {
        nextGameStateAndMessages.setNextGameState(new MotorPowerChoice());
        nextGameStateAndMessages.setPlayerMessage(players.getFirst().getNickname(), new TurnMessage());
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Method to apply the visitor to a card of type {@link MeteorSwarm}
     * it sets manage projectile as the next state and notifies all players about the projectile
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(MeteorSwarm card) {
        int trajectory = rollTheDice();

        nextGameStateAndMessages.setNextGameState(new ManageProjectile());
        for (int i = 0; i < players.size(); i++) {
            //can use nicknames array since the same exact message needs to be sent to every player anyway
            nextGameStateAndMessages.setPlayerMessage(nicknames.get(i), new ProjectileTrajectoryMessage(card.getNextMeteorToHandle(), trajectory));
        }
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Method to apply the visitor to a card of type {@link Epidemic}
     * it applies the card, sets card drawing as the next state and notifies all players about the updated status
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(Epidemic card) {
        card.apply(players);
        nextGameStateAndMessages.setNextGameState(new CardDrawing());
        for (int i = 0; i < players.size(); i++) {
            nextGameStateAndMessages.setPlayerMessage(players.get(i).getNickname(), new UpdateClientShipMessage(players.get(i)));
        }
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Method to apply the visitor to a card of type {@link CombatZone}
     * it applies the logic of the first line of the card and sets motor power choice as the new state for the following card's line
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(CombatZone card) {
        card.Line1(players);
        nextGameStateAndMessages.setNextGameState(new MotorPowerChoice());
        nextGameStateAndMessages.setPlayerMessage(players.get(card.getAffectedPlayerIndex()).getNickname(),
                new GenericMessage("You are the player with the smallest crew, so you lost " + card.daysLossLine1 + " days."));
        nextGameStateAndMessages.setPlayerMessage(players.getFirst().getNickname(), new TurnMessage());
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Method to apply the visitor to a card of type {@link AbandonedShip}
     * it iterates to find a player with enough crew to dock,
     * if the player is found, the method sets docking choice as the next state
     * otherwise, card drawing is set as the next state
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(AbandonedShip card) {
        int i = 0;
        while (i < players.size() && players.get(i).getCrew() < card.crewLoss) {
            nextGameStateAndMessages.setPlayerMessage(players.get(i).getNickname(), new GenericMessage("You don't have enough crew members to dock the ship!"));
            i++;
        }
        if (i < players.size()) {
            nextGameStateAndMessages.setNextGameState(new DockingChoice());
            nextGameStateAndMessages.setPlayerMessage(players.get(i).getNickname(), new TurnMessage());
        } else {
            nextGameStateAndMessages.setNextGameState(new CardDrawing());
        }
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Method to apply the visitor to a card of type {@link AbandonedStation}
     * it iterates to find a player with enough crew to dock,
     * if the player is found, the method sets docking choice as the next state
     * otherwise, card drawing is set as the next state
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(AbandonedStation card) {
        int i = 0;
        while (i < players.size() && players.get(i).getCrew() < card.requiredCrew) {
            nextGameStateAndMessages.setPlayerMessage(players.get(i).getNickname(), new GenericMessage("You don't have enough crew members to dock the station!"));
            i++;
        }
        if (i < players.size()) {
            nextGameStateAndMessages.setNextGameState(new DockingChoice());
            nextGameStateAndMessages.setPlayerMessage(players.get(i).getNickname(), new TurnMessage());
        } else {
            nextGameStateAndMessages.setNextGameState(new CardDrawing());
        }
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Method to apply the visitor to a card of type {@link Smugglers}
     * it sets firepower choice as the next state and notifies the turn for the first player
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(Smugglers card) {
        nextGameStateAndMessages.setNextGameState(new FirePowerChoice());
        nextGameStateAndMessages.setPlayerMessage(players.getFirst().getNickname(), new TurnMessage());
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Method to apply the visitor to a card of type {@link Slavers}
     * it sets firepower choice as the next state and notifies the turn for the first player
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(Slavers card) {
        nextGameStateAndMessages.setNextGameState(new FirePowerChoice());
        nextGameStateAndMessages.setPlayerMessage(players.getFirst().getNickname(), new TurnMessage());
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Method to apply the visitor to a card of type {@link Pirates}
     * it sets firepower choice as the next state and notifies the turn for the first player
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(Pirates card) {
        nextGameStateAndMessages.setNextGameState(new FirePowerChoice());
        nextGameStateAndMessages.setPlayerMessage(players.getFirst().getNickname(), new TurnMessage());
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }
}
