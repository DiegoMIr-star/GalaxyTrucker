package model.Cards;

import Connections.Messages.Message;
import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.Game;

/**
 * Visitor class used to dispatch the different cards
 */
public class CardVisitHandlerForServer implements CardVisitor {

    /**
     * Current message
     */
    Message message;

    /**
     * Current game
     */
    Game game;

    /**
     * Class constructor, it initializes all the attributes
     * @param game current game
     * @param message current message
     */
    public CardVisitHandlerForServer(Game game, Message message) {
        this.game = game;
        this.message = message;
    }

    /**
     * Visit methode used to apply the card effect
     * @param card current stardust effect
     */
    @Override
    public void visit(Stardust card) {card.apply(game.getPlayers());}

    /**
     * Visit method, it throws an exception for an unexpected card
     * @param card current planets card
     */
    @Override
    public void visit(Planets card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}

    /**
     * Visit method, it throws an exception for an unexpected card
     * @param card current open space card
     */
    @Override
    public void visit(OpenSpace card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}

    /**
     * Visit method, it throws an exception for an unexpected card
     * @param card current meteor warm card
     */
    @Override
    public void visit(MeteorSwarm card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}

    /**
     * Visit method, it throws an exception for an unexpected card
     * @param card current epidemic card
     */
    @Override
    public void visit(Epidemic card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}

    /**
     * Visit method, it's empty
     * @param card current combat zone card
     */
    @Override
    public void visit(CombatZone card) {}

    /**
     * Visit method, it's empty
     * @param card current abandoned ship card
     */
    @Override
    public void visit(AbandonedShip card) {}

    /**
     * Visit method, it throws an exception for an unexpected card
     * @param card current abandoned station card
     */
    @Override
    public void visit(AbandonedStation card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}

    /**
     * Visit method, it throws an exception for an unexpected card
     * @param card current smugglers card
     */
    @Override
    public void visit(Smugglers card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}

    /**
     * Visit method, it's empty
     * @param card current slavers card
     */
    @Override
    public void visit(Slavers card) {}

    /**
     * Visit method, it's empty
     * @param card current pirates card
     */
    @Override
    public void visit(Pirates card) {}
}
