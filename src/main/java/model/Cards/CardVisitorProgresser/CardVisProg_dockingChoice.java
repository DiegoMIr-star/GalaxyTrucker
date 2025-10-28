package model.Cards.CardVisitorProgresser;

import Connections.Messages.*;
import Controller.State.AddAndRearrangeStocks;
import Controller.State.CardDrawing;
import Controller.State.DockingChoice;
import Controller.State.GiveUpCrewChoice;
import model.Cards.*;
import model.Game;
import model.NextGameStateAndMessages;
import model.ShipDashboard;

import java.util.ArrayList;

/**
 * Card visitor used for cards that require a docking decision from the player
 * It extends {@link CardVisProg_state(Game, ArrayList, ArrayList)}
 */
public class CardVisProg_dockingChoice  extends CardVisProg_state{

    /**
     * Attribute used to store the message related to this game phase
     */
    CardActivationRequestMessage message;

    /**
     * Constructor of the current state to handle player's choice about docking
     * @param game is the current game
     * @param players is the array of players
     * @param message is the message related to this game phase
     * @param nicknames is the array of nicknames
     */
    public CardVisProg_dockingChoice(Game game, ArrayList<ShipDashboard> players, Message message, ArrayList<String> nicknames) {
        super(game, players, nicknames);
        currentGameState = new DockingChoice();
        this.message = (CardActivationRequestMessage) message;
        nextGameStateAndMessages = new NextGameStateAndMessages(players);
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
     * Method to apply the visitor to a card of type {@link AbandonedShip}
     * if the player accepts, he receives bonus (credits) and malus (he has to choose the crew members to give up);
     * if the player refuses, and there are other players, the choice is passed to the next player;
     * otherwise, card drawing is set as new state.
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(AbandonedShip card) {
        if (message.isYes()) {
            card.bonusMalus(players.get(getSenderIndex(message)));
            nextGameStateAndMessages.setNextGameState(new GiveUpCrewChoice());
            nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new TurnMessage());
        }
        else if (!isLastPlayer(getSenderIndex(message))) {
            nextGameStateAndMessages.setNextGameState(new DockingChoice());
            nextGameStateAndMessages.setPlayerMessage(players.get(getSenderIndex(message) + 1).getNickname(), new TurnMessage());
        }
        else {
            nextGameStateAndMessages.setNextGameState(new CardDrawing());
        }
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Method to apply the visitor to a card of type {@link AbandonedStation}
     * if the player accepts, he receives bonus (he gains stocks to add to the ship) and malus (he loses days);
     * if the player refuses, and there are other players, the choice is passed to the next player;
     * otherwise, card drawing is set as new state.
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(AbandonedStation card) {
        if (message.isYes()) {
            card.loseDays(players.get(getSenderIndex(message)));
            //the players should also be notified of the credits earned
            nextGameStateAndMessages.setNextGameState(new AddAndRearrangeStocks());
            nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new StocksToAddMessage(card.stocks));
        }
        else if (!isLastPlayer(getSenderIndex(message))) {
            nextGameStateAndMessages.setNextGameState(new DockingChoice());
            nextGameStateAndMessages.setPlayerMessage(players.get(getSenderIndex(message) + 1).getNickname(), new TurnMessage());
        }
        else {
            nextGameStateAndMessages.setNextGameState(new CardDrawing());
        }
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }
}
