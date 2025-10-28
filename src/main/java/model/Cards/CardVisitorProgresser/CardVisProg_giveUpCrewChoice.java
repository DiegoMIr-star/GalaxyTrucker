package model.Cards.CardVisitorProgresser;

import Connections.Messages.CreditsEarnedMessage;
import Connections.Messages.Message;
import Connections.Messages.TurnMessage;
import Connections.Messages.UpdateServerShipMessage;
import Controller.State.CardDrawing;
import Controller.State.FirePowerChoice;
import Controller.State.GiveUpCrewChoice;
import model.Cards.*;
import model.Cards.Enemies.Slavers;
import model.Game;
import model.NextGameStateAndMessages;
import model.ShipDashboard;

import java.util.ArrayList;

/**
 * Visitor class used to apply the effects of Combat Zone, Abandoned Ship and Slavers
 * in the give up crew state in the controller
 */
public class CardVisProg_giveUpCrewChoice extends CardVisProg_state {

    /**
     * Current update server ship message
     */
    UpdateServerShipMessage message;

    /**
     * Class constructor, it initializes all the attributes of the class
     * @param game current game
     * @param players current list of players
     * @param message current message
     * @param nicknames current list of nicknames
     */
    public CardVisProg_giveUpCrewChoice(Game game, ArrayList<ShipDashboard> players, Message message, ArrayList<String> nicknames) {
        super(game, players, nicknames);
        currentGameState = new GiveUpCrewChoice();
        this.message = (UpdateServerShipMessage) message;
        nextGameStateAndMessages = new NextGameStateAndMessages(players);
        currentGameState = new GiveUpCrewChoice();
    }

    /**
     * True if a player is the last one
     * @param playerIndex current player index
     * @return true if the player is the last one
     */
    private Boolean isLastPlayer(int playerIndex){return (playerIndex == players.size() - 1);}

    /**
     * Visit method used for combat zone: it updates and finalize the ship, updating
     * the game state and the messages to send to the different clients
     * @param card current combat zone card
     * @return next game state and messages object with next state and messages for the players
     */
    @Override
    public NextGameStateAndMessages visit(CombatZone card) {
        players.get(card.getAffectedPlayerIndex()).updateShip(message.getUpdatedShip().getShip());
        card.setAffectedPlayerIndex(-1);
        card.setLowestMotorPower(-1);
        nextGameStateAndMessages.setNextGameState(new FirePowerChoice());
        nextGameStateAndMessages.setPlayerMessage(players.getFirst().getNickname(), new TurnMessage());
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Visit method used for abandoned ship: it updates and finalize the ship, updating
     * the game state and the messages to send to the different clients
     * @param card current abandoned ship card
     * @return next game state and messages object with next state and messages for the players
     */
    @Override
    public NextGameStateAndMessages visit(AbandonedShip card) {
        players.get(getSenderIndex(message)).updateShip(message.getUpdatedShip().getShip());
        nextGameStateAndMessages.setNextGameState(new CardDrawing());
        nextGameStateAndMessages.setPlayerMessage(message.getNickname(), new CreditsEarnedMessage(card.creditsGained));
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }

    /**
     * Visit method used for slavers: it updates and finalize the ship, updating
     * the game state and the messages to send to the different clients. If the client
     * is not the last one the next state is a firepower choice
     * @param card current abandoned ship card
     * @return next game state and messages object with next state and messages for the players
     */
    @Override
    public NextGameStateAndMessages visit(Slavers card) {
        players.get(getSenderIndex(message)).updateShip(message.getUpdatedShip().getShip());
        if (!isLastPlayer(getSenderIndex(message))) {
            nextGameStateAndMessages.setNextGameState(new FirePowerChoice());
            nextGameStateAndMessages.setPlayerMessage(players.get(getSenderIndex(message) + 1).getNickname(), new TurnMessage());
            nextGameStateAndMessages.setPlayers(players);
            return nextGameStateAndMessages;
        }
        nextGameStateAndMessages.setNextGameState(new CardDrawing());
        nextGameStateAndMessages.setPlayers(players);
        return nextGameStateAndMessages;
    }
}


