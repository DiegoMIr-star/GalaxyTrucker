package View;

import Connections.ClientInterface;
import Connections.Messages.LogResponseMessage;
import model.Cards.Card;
import model.Cards.Planets;
import model.Deck;
import model.DifferentShipComponents.ShipComponent;
import model.Projectiles.Projectile;
import model.ShipDashboard;
import model.Stocks;

import java.io.IOException;
import java.util.ArrayList;

/**
 * View extension used to implement TUI and GUI
 * @see TUI
 * @see GUI
 */
public interface UI extends View{
	void showLoginResponse(LogResponseMessage message);
	void setInterface(ClientInterface clientInterface);
	void claimReward(Card card) throws IOException;
	void drawShipComponentOrSmallDeck();
	void handleProjectile(Projectile projectile, int trajectory, ShipDashboard ship);
	void connectLifeSupportsHireCrewInitializeAttributesAndSendShip(ShipComponent[][] ship);
	void fixShip(ShipDashboard ship);
	void handleComponent(ShipComponent component);
	void handleSmallDeck(Deck deck);
	void askDoubleMotor();
	void askDocking(Card card) throws IOException;
	void askDoubleCannon(ShipComponent[][] ship);
	boolean askToActivateDoubleCannonWhenAttacked(Projectile projectile, int trajectory, ShipDashboard ship);
	void askGiveUpCrew(int amount);
	void askAddAndRearrangeStocks(Stocks stocks, ShipComponent[][] ship);
	void notifyTimerRestarted(boolean isLast);
	void notifyTimerExpired();
	void handleTimerExpired(boolean isLast);
	void notifyCredits(int credits);
	void landOnPlanet(ArrayList<Integer> freePlanetsIndex, Planets curCard) throws IOException;
	void planetsState();
	void drawCard() throws IOException;
	void resume() throws IOException;
	void showPositionsAndTurns(ArrayList<ShipDashboard> players);
	void waitingForOthersTurns();
}
