package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;

/**
 * Visitor interface for the different cards: it's used to return an int
 */
public interface CardVisitReturner {
    int visit(Stardust card);
    int visit(Planets card);
    int visit(OpenSpace card);
    int visit(MeteorSwarm card);
    int visit(Epidemic card);
    int visit(CombatZone card);
    int visit(AbandonedShip card);
    int visit(AbandonedStation card);
    int visit(Smugglers card);
    int visit(Slavers card);
    int visit(Pirates card);
}
