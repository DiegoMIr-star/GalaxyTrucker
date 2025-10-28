package model.Cards.CardVisitorProgresser;

import model.Cards.*;
import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.NextGameStateAndMessages;

/**
 * Visitor class used to dispatch the different cards and return the object next game state
 * and messages, an object with the next game and the next messages to send
 * @see NextGameStateAndMessages
 */
public interface CardVisitorProgresser {

	NextGameStateAndMessages visit(Stardust card);
	NextGameStateAndMessages visit(Planets card);
	NextGameStateAndMessages visit(OpenSpace card);
	NextGameStateAndMessages visit(MeteorSwarm card);
	NextGameStateAndMessages visit(Epidemic card);
	NextGameStateAndMessages visit(CombatZone card);
	NextGameStateAndMessages visit(AbandonedShip card);
	NextGameStateAndMessages visit(AbandonedStation card);
	NextGameStateAndMessages visit(Smugglers card);
	NextGameStateAndMessages visit(Slavers card);
	NextGameStateAndMessages visit(Pirates card);
}
