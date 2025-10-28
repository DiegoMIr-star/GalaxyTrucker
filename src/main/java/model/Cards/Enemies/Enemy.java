package model.Cards.Enemies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.Card;

/**
 * This class is an extension of the Card class: it's also abstract in order
 * to be extended for all enemies kind
 * @see Card
 */
public abstract class Enemy extends Card {

	/**
	 * Amount of firepower which is necessary to win against the enemy
	 */
	public final int firePower;

	/**
	 * Days lost after you lose against the enemy
	 */
	public final int daysLoss;

	/**
	 * Constructor of the enemy card: it initializes all the attributes
	 * @param name name of the card
	 * @param lvl level of the card
	 * @param firePower firepower which has to be set
	 * @param daysLoss lost days to set
	 * @param image path to the image of the card
	 */
	@JsonCreator
	public Enemy(
			@JsonProperty("type") String name,
			@JsonProperty("level") int lvl,
			@JsonProperty("fire_power") int firePower,
			@JsonProperty("days_loss") int daysLoss,
			@JsonProperty("image") String image
	) {
		super(name, lvl, image);
		this.firePower = firePower;
		this.daysLoss = daysLoss;
	}

	/**
	 * Method used to print the instances of this class
	 * @return string used to print the object
	 */
	@Override
	public String toString() {
		String str = "    Minimum fire power Required: " + firePower + "\n" +
				"    Days lost: " + daysLoss + "\n";

		return str;
	}
	/*@Override
	public NextGameStateAndMessages apply(CardVisProg_dynamicFirePower visitor){
		return visitor.visit(this);
	}*/
}
