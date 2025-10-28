package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;

/**
 * Visitor class used to apply some effects of the cards in the client interface
 * @see Connections.ClientInterface
 */
public class CardVisitHandlerForClient implements CardVisitReturner{

    /**
     * Visit methode used to notify, there's an unexpected card
     * @param card current stardust card
     * @return card amount, here not used
     */
    @Override
    public int visit(Stardust card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}

    /**
     * Visit methode used to notify, there's an unexpected card
     * @param card current planets card
     * @return card amount, here not used
     */
    @Override
    public int visit(Planets card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}

    /**
     * Visit methode used to notify, there's an unexpected card
     * @param card current open space card
     * @return card amount, here not used
     */
    @Override
    public int visit(OpenSpace card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}

    /**
     * Visit methode used to notify, there's an unexpected card
     * @param card current meteor swarm card
     * @return card amount, here not used
     */
    @Override
    public int visit(MeteorSwarm card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}

    /**
     * Visit methode used to notify, there's an unexpected card
     * @param card current epidemic card
     * @return card amount, here not used
     */
    @Override
    public int visit(Epidemic card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}

    /**
     * Visit method, it returns the card crew loss
     * @param card current combat zone card
     * @return amount of crew loss
     */
    @Override
    public int visit(CombatZone card) {return card.crewLossLine2;}

    /**
     * Visit method, it returns the card crew loss
     * @param card current abandoned ship card
     * @return amount of crew loss
     */
    @Override
    public int visit(AbandonedShip card) {return card.crewLoss;}

    /**
     * Visit methode used to notify, there's an unexpected card
     * @param card current abandoned station card
     * @return card amount, here not used
     */
    @Override
    public int visit(AbandonedStation card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}

    /**
     * Visit methode used to notify, there's an unexpected card
     * @param card current smugglers card
     * @return card amount, here not used
     */
    @Override
    public int visit(Smugglers card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}

    /**
     * Visit method, it returns the card crew loss
     * @param card current slavers card
     * @return amount of crew loss
     */
    @Override
    public int visit(Slavers card) {return card.crewLoss;}

    /**
     * Visit methode used to notify, there's an unexpected card
     * @param card current pirates card
     * @return card amount, here not used
     */
    @Override
    public int visit(Pirates card) {throw new IllegalArgumentException("Unexpected card, the card was not one containing a crew loss event");}
}
