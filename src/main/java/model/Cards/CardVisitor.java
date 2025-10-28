package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;

/**
 * Visitor interface for the different cards
 */
public interface CardVisitor {
    void visit(Stardust card);
    void visit(Planets card);
    void visit(OpenSpace card);
    void visit(MeteorSwarm card);
    void visit(Epidemic card);
    void visit(CombatZone card);
    void visit(AbandonedShip card);
    void visit(AbandonedStation card);
    void visit(Smugglers card);
    void visit(Slavers card);
    void visit(Pirates card);
}
