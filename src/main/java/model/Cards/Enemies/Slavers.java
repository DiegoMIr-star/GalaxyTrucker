package model.Cards.Enemies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.*;
import model.Cards.CardVisitorProgresser.*;
import model.NextGameStateAndMessages;

/**
 * Extension of enemy card: it represents the slavers
 * @see Enemy
 */
public class Slavers extends Enemy{

	/**
	 * It represents the crew loss after losing against the enemy
	 */
	public final int crewLoss;

	/**
	 * It represents the credits gained winning against the enemy
	 */
	public final int creditsGained;

	/**
	 * Constructor of the slavers card: it initializes all the attributes
	 * @param lvl level
	 * @param firePower current firepower
	 * @param daysLoss current days to loose
	 * @param crewLoss current crew to loose
	 * @param creditsGained current credits to gain
	 * @param image path to the image
	 */
	@JsonCreator
	public Slavers(
			@JsonProperty("level") int lvl,
			@JsonProperty("fire_power")int firePower,
			@JsonProperty("days_loss") int daysLoss,
			@JsonProperty("crew_loss") int crewLoss,
			@JsonProperty("credits_gained")int creditsGained,
			@JsonProperty("image") String image
	) {
		super("Slavers", lvl, firePower, daysLoss, image);
		this.crewLoss = crewLoss;
		this.creditsGained = creditsGained;
	}

	/**
	 * Method used to print the instances of this class
	 * @return string used to print the object
	 */
	@Override
	public String toString() {
		String str =
				"Slavers\n" +
				super.toString() +
				"    Reward: " + creditsGained + " credits\n" +
				"    Crew lost: " + crewLoss + "\n";

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
	public Slavers clone() {return (Slavers) super.clone();}
}

