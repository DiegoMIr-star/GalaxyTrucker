package model.Cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.CardVisitorProgresser.*;
import model.NextGameStateAndMessages;
import model.ShipDashboard;
import model.Stocks;
import model.exceptions.InvalidIndexException;

import java.util.*;

/**
 * Extension of card: it represents the planets
 * @see Card
 */
public class Planets extends Card{

	/**
	 * Days lost as effect of the card
	 */
	public final int daysLost;

	/**
	 * List of stocks for each planet, each index is a different planet
	 */
	public final ArrayList<Stocks> planetStocks;

	/**
	 * Array of bool, it says if a specific planet is occupied
	 */
	private final boolean[] planetOccupation;

	/**
	 * Constructor of the card: it initializes the different attribute
	 * @param lvl level of the card
	 * @param daysLost lost days
	 * @param planetStocks list of stocks for each planet
	 * @param image path to the image
	 */
	@JsonCreator
	public Planets(
			@JsonProperty("level") int lvl,
			@JsonProperty("days_lost") int daysLost,
			@JsonProperty("stocks") ArrayList<Stocks> planetStocks,
			@JsonProperty("image") String image
	) {
		super("Planets", lvl,image);
		this.daysLost = daysLost;
		this.planetStocks = planetStocks;
		this.planetOccupation = new boolean[planetStocks.size()];
        Arrays.fill(planetOccupation, false);
	}

	/**
	 * It returns the indexes of the free planets
	 * @return list of indexes of the free planets
	 */
	public ArrayList<Integer> getFreePlanets() {
		ArrayList<Integer> freePlanets = new ArrayList<>();
		for (int i = 0; i < planetOccupation.length; i++) {
			if (!planetOccupation[i]) {
				freePlanets.add(i);
			}
		}
		return freePlanets;
	}

	/**
	 * It sets as occupied the specific planet landed and set the days to move
	 * for the current player
	 * @param planetIndex index of the specific planet landed
	 * @param player current player
	 * @throws InvalidIndexException thrown when is asked an invalid index of planets
	 */
	public void land(int planetIndex, ShipDashboard player) throws InvalidIndexException {
		//check if it's in the right interval
		if (planetIndex >= planetStocks.size() || planetIndex < 0) {
			throw new InvalidIndexException("invalid planetIndex: out of range");
		}
		else if (planetOccupation[planetIndex]) {
			throw new InvalidIndexException("invalid planetIndex: planet already occupied");
		}
		else {
			planetOccupation[planetIndex] = true;
			//the number of the days lost by the player is updated
			player.setDaysToMove(-daysLost);
		}
	}

	/**
	 * Getter of the stocks contained in a specific planet
	 * @param planetIndex index of the specific planet
	 * @return stocks contained in the specific planet
	 */
	public Stocks getStocks(int planetIndex) {return planetStocks.get(planetIndex);}

	/**
	 * Method which helps to print the card into the TUI
	 * @return string which print the card
	 */
	@Override
	public String toString() {
		String str =
				"Planets\n" +
						"    Days lost by landing: " + daysLost + "\n";


		str += "    Planet 1: " + planetStocks.getFirst().toString() + "\n";
		for(int i = 1; i < planetStocks.size(); i++){
			str +=  "    Planet " + (i + 1) + ": " + planetStocks.get(i).toString() + "\n";
		}

		return str;
	}

	/**
	 * Method used to apply the effect of the card by using the Visitor pattern
	 * @see CardVisitHandlerForClient
	 * @see CardVisitHandlerForServer
	 * @param visitor object visitor which contains the logic of the card
	 */
	@Override
	public void apply(CardVisitor visitor) {visitor.visit(this);}

	/**
	 * Method to return some parameters of the card according to Visitor patter
	 * @see CardVisitReturner
	 * @param visitor object visitor which contains the logic of the card
	 * @return value
	 */
	@Override
	public int returner(CardVisitReturner visitor) {return visitor.visit(this);}

	/**
	 * Method used to return state and messages according to the specific effect of the card
	 * @see Controller.Controller
	 * @param visitor object visitor which contains the logic of the card
	 * @return next game and messages
	 */
	@Override
	public NextGameStateAndMessages apply(CardVisProg_state visitor){
		return visitor.visit(this);
	}

	/**
	 * Method used to return the copy of the card
	 * @return copy of the card
	 */
	@Override
	public Planets clone() {return (Planets) super.clone();}
}