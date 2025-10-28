package model.Cards.Enemies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.*;
import model.Cards.CardVisitorProgresser.*;
import model.NextGameStateAndMessages;
import model.Projectiles.Projectile;

import java.util.ArrayList;

import static model.Game.rollTheDice;

/**
 * Extension of enemy card: it represents the pirates
 * @see Enemy
 */
public class Pirates extends Enemy{

	/**
	 * It represents the credits gained winning against the enemy
	 */
	public final int creditsGained;

	/**
	 * List of shots as projectiles
	 * @see Projectile
	 */
	public final ArrayList<Projectile> shots;

	/**
	 * List of randomized trajectories used to hit the players
	 */
	private final ArrayList<Integer> randomizedTrajectories;

	/**
	 * Index of next shot to be handled from the player
	 */
	int nextShotToHandle;

	/**
	 * Constructor of the pirates card: it initializes all the attributes
	 * @param lvl level of the card
	 * @param firePower current firepower
	 * @param daysLoss current days to lose
	 * @param creditsGained current credits to gain
	 * @param shots current list of shots
	 * @param image path to the image
	 */
	@JsonCreator
	public Pirates(
			@JsonProperty("level") int lvl,
			@JsonProperty("fire_power") int firePower,
			@JsonProperty("days_loss") int daysLoss,
			@JsonProperty("credits_gained") int creditsGained,
			@JsonProperty("meteors") ArrayList<Projectile> shots,
			@JsonProperty("image") String image
	) {
		super("Pirates", lvl, firePower, daysLoss, image);
		this.creditsGained = creditsGained;
		this.shots = shots;
		this.randomizedTrajectories = new ArrayList<>();
		for(int i = 0; i < shots.size(); i++){
			randomizedTrajectories.add(-1);
		}
		//randomizedTrajectories.add(-1);
		this.nextShotToHandle = 0;
	}

	/**
	 * It sets for a certain index a randomized trajectory
	 * @see model.Game
	 * @param index index of the trajectory to set
	 */
	public void randomizeTrajectory(int index) {randomizedTrajectories.set(index, rollTheDice());}

	/**
	 * Getter of a certain trajectory
	 * @param index index of the trajectory
	 * @return specific trajectory
	 */
	public int getTrajectory(int index) {return randomizedTrajectories.get(index);}

	/**
	 * Getter of the trajectory of the next shot to handle
	 * @return trajectory of the next shot
	 */
	public int getNextShotTrajectory() {return randomizedTrajectories.get(nextShotToHandle);}

	/**
	 * Getter of the index of the next shot to handle
	 * @return index of next shot to handle
	 */
	public int getIndexOfNextShotToHandle() {return nextShotToHandle;}

	/**
	 * Getter of the next shot to be handled
	 * @return next shot to be handled
	 */
	public Projectile getNextShotToHandle(){return shots.get(nextShotToHandle);}

	/**
	 * The attribute nextShotToHandle gets incremented by 1
	 */
	public void markShotAsHandled(){nextShotToHandle++;}

	/**
	 * It says if the shots are all handled
	 * @return true if the index of next shot exceeds the shots
	 */
	public Boolean allShotsHandled(){return nextShotToHandle >= shots.size();}

	/**
	 * It sets the next shot to handle as 0
	 */
	public void prepareShotsForNewTurn(){nextShotToHandle = 0;}

	/**
	 * Method used to print the instances of this class
	 * @return string used to print the object
	 */
	@Override
	public String toString() {
		String str =
				"Pirates\n" +
				super.toString() +
				"    Reward: " + creditsGained + " credits\n" +
				"    Shots: ";

		str += shots.getFirst().toString();
		for(int i = 1; i < shots.size(); i++){
			str +=  ", " + shots.get(i).toString();
		}
		str += "\n";
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
	public Pirates clone() {return (Pirates) super.clone();}
}
