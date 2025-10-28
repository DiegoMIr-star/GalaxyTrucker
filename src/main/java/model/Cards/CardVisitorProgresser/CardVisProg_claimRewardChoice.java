package model.Cards.CardVisitorProgresser;

import Connections.Messages.CreditsEarnedMessage;
import Connections.Messages.Message;
import Connections.Messages.StocksToAddMessage;
import Controller.State.AddAndRearrangeStocks;
import Controller.State.CardDrawing;
import Controller.State.ClaimRewardChoice;
import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.Game;
import model.NextGameStateAndMessages;
import model.ShipDashboard;

import java.util.ArrayList;

/**
 * Card visitor used for cards in which the player has to choose to claim a reward
 * It extends {@link CardVisProg_state(Game, ArrayList, ArrayList)}
 */
public class CardVisProg_claimRewardChoice extends CardVisProg_state {

    /**
     * Flag used to store if the player has claimed or not the reward
     */
    boolean claimed = false;

    /**
     * Attribute used to store the message related to this game phase
     */
    Message message;

    /**
     * Constructor of the current state to handle player's choice about claiming the reward
     * @param game is the current game
     * @param players is the array of players
     * @param message is the message related to this game phase
     * @param nicknames is the array of nicknames
     */
    public CardVisProg_claimRewardChoice(Game game, ArrayList<ShipDashboard> players, Message message, ArrayList<String> nicknames) {
        super(game, players, nicknames);
        currentGameState = new ClaimRewardChoice();
        nextGameStateAndMessages = new NextGameStateAndMessages(players);
        this.message = message;
        claimed = message.isClaimed();
    }

    /**
     * Method to apply the visitor to a card of type {@link Smugglers}
     * if the player accepts, he receives bonus (stocks to add) and malus (loss days);
     * otherwise, card drawing is set as new state.
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(Smugglers card) {
        if (claimed) {
            nextGameStateAndMessages.setNextGameState(new AddAndRearrangeStocks());
            nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new StocksToAddMessage(card.stocks));
            players.get(getSenderIndex(message)).setDaysToMove(-card.daysLoss);
            nextGameStateAndMessages.setPlayers(players);
            return nextGameStateAndMessages;
        }
        nextGameStateAndMessages.setNextGameState(new CardDrawing());
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Method to apply the visitor to a card of type {@link Slavers}
     * if the player accepts, he receives bonus (credits) and malus (loss days);
     * otherwise, card drawing is set as the next state.
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(Slavers card) {
        if (claimed) {
            players.get(getSenderIndex(message)).addCredits(card.creditsGained);
            nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new CreditsEarnedMessage(card.creditsGained));
            players.get(getSenderIndex(message)).setDaysToMove(-card.daysLoss);
        }
        nextGameStateAndMessages.setNextGameState(new CardDrawing());
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;

    }

    /**
     * Method to apply the visitor to a card of type {@link Pirates}
     * if the player accepts, he receives bonus (credits) and malus (loss days);
     * otherwise, card drawing is set as new state.
     * @param card is the current card to visit
     * @return the next game state with linked messages
     */
    @Override
    public NextGameStateAndMessages visit(Pirates card) {
        if (claimed) {
            players.get(getSenderIndex(message)).addCredits(card.creditsGained);
            nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new CreditsEarnedMessage(card.creditsGained));
            players.get(getSenderIndex(message)).setDaysToMove(-card.daysLoss);
        }
        nextGameStateAndMessages.setNextGameState(new CardDrawing());
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }
}
