package model.Cards.Enemies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.*;
import model.Cards.CardVisitorProgresser.*;
import model.NextGameStateAndMessages;
import model.Stocks;

/**
 * Extension of enemy card: it represents the smugglers
 * @see Enemy
 */
public class Smugglers extends Enemy {

	/**
	 * Stocks achieved in case of win
	 */
	public final Stocks stocks;

	/**
	 * Number of precious stocks to remove in case of loss
	 */
	public int penaltyStocks;

	/**
	 * Constructor of the smugglers card: it initializes all the attributes
	 * @param stocks currents winning stocks
	 * @param lvl level
	 * @param firePower current firepower
	 * @param daysLoss current days lost
	 * @param penaltyStocks current penalty stocks
	 * @param image path to the image
	 */
	@JsonCreator
	public Smugglers(
			@JsonProperty("stocks") Stocks stocks,
			@JsonProperty("level") int lvl,
			@JsonProperty("fire_power") int firePower,
			@JsonProperty("days_loss") int daysLoss,
			@JsonProperty("penalty_stocks") int penaltyStocks,
			@JsonProperty("image") String image
	) {
		super("Smugglers", lvl, firePower, daysLoss, image);
		this.stocks = stocks;
		this.penaltyStocks = penaltyStocks;
	}

	/**
	 * Method used to print the instances of this class
	 * @return string used to print the object
	 */
	@Override
	public String toString() {
		String str =
				"Smugglers\n" +
				super.toString() +
				"    Reward: " + stocks.toString() + "\n" +
				"    Stocks lost: " + penaltyStocks + " most precious containers.\n";


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
	public Smugglers clone() {return (Smugglers) super.clone();}
}