package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;

/**
 * This is a visitor class used in the TUI to print a specific sentence according to the card
 * @see View.TUI
 */
public class TUICardVisitor implements CardVisitReturner{
    @Override
    public int visit(Stardust card) {return 0;}

    @Override
    public int visit(Planets card) {return 0;}

    @Override
    public int visit(OpenSpace card) {return 0;}

    @Override
    public int visit(MeteorSwarm card) {return 0;}

    @Override
    public int visit(Epidemic card) {return 0;}

    @Override
    public int visit(CombatZone card) {return 0;}

    @Override
    public int visit(AbandonedShip card) {return 0;}

    @Override
    public int visit(AbandonedStation card) {return 0;}

    @Override
    public int visit(Smugglers card) {
        System.out.println("You can earn "+card.stocks + ".");
        System.out.println("At the same time you will lose "+card.daysLoss+" days.");
        return 1;
    }

    @Override
    public int visit(Slavers card) {
        System.out.println("You can earn "+card.creditsGained+" credits, losing "+card.daysLoss+" days.");
        return 1;
    }

    @Override
    public int visit(Pirates card) {
        System.out.println("You can earn "+card.creditsGained+" credits, losing "+card.daysLoss+" days.");
        return 1;
    }
}
