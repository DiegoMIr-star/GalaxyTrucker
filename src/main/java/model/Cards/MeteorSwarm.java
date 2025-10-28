package model.Cards;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.CardVisitorProgresser.*;
import model.NextGameStateAndMessages;
import model.Projectiles.Projectile;

import static model.Game.rollTheDice;

/**
 * Extension of card: it represents the meteor swarm card
 * @see Card
 */
public class MeteorSwarm extends Card{

    /**
     * List of different projectiles
     */
    public final ArrayList<Projectile> meteorites;

    /**
     * List of a random trajectory for each projectile
     */
    private final ArrayList<Integer> randomizedTrajectories;

    /**
     * Index of the next meteor to be handled
     */
    int nextMeteorToHandle;

    /**
     * It marks if a player has ended to manage the current method
     */
    int playersDoneManagingCurrentMeteor;

    /**
     * Constructor of the card: it initializes the attributes of the card
     * @param level level of the card
     * @param meteorites list of projectiles
     * @param image path to the image
     */
    @JsonCreator
    public MeteorSwarm(
            @JsonProperty("level") int level,
            @JsonProperty("meteorites") ArrayList<Projectile> meteorites,
            @JsonProperty("image") String image
    ){
        super("MeteorSwarm", level, image);
        this.meteorites=meteorites;
        randomizedTrajectories = new ArrayList<>();
        for(Projectile _ : meteorites){
            randomizedTrajectories.add(-1);
        }
        nextMeteorToHandle = 0;
        playersDoneManagingCurrentMeteor = 0;
    }

    /**
     * It assigns to a specific projectile a random trajectory
     * @param index index of the projectile trajectory to be set
     */
    public void randomizeTrajectory(int index) {randomizedTrajectories.set(index, rollTheDice());}

    /**
     * It returns the trajectory of a projectile
     * @param index index of the projectile
     * @return specific trajectory on the ship
     */
    public int getTrajectory(int index) {return randomizedTrajectories.get(index);}

    /**
     * It returns the trajectory of the next meteor to be handled
     * @return trajectory of the next meteor to be handled
     */
    public int getNextMeteorTrajectory() {return randomizedTrajectories.get(nextMeteorToHandle);}

    /**
     * It returns the index of the next meteor to be handled
     * @return index of the next meteor to be handled
     */
    public int getIndexOfNextMeteorToHandle() {return nextMeteorToHandle;}

    /**
     * It returns the specific projectile, the next to be handled
     * @return next projectile to be handles
     */
    public Projectile getNextMeteorToHandle() {return meteorites.get(nextMeteorToHandle);}

    /**
     * It marks the player, when the current meteor is handled
     */
    public void markPlayerAsDoneManagingCurrentMeteor() {playersDoneManagingCurrentMeteor++;}

    /**
     * Getter of playersDoneManagingCurrentMeteor
     * @return playersDoneManagingCurrentMeteor
     */
    public int getPlayersDoneManagingCurrentMeteor() {return playersDoneManagingCurrentMeteor;}

    /**
     * It marks a meteor as handled and set the next meteor to be handled
     */
    public void markMeteorAsHandled(){
        nextMeteorToHandle++;
        playersDoneManagingCurrentMeteor = 0;
    }

    /**
     * It says if the meteors are all handled
     * @return true if the meteorites are all handled
     */
    public Boolean allMeteorsHandled(){return nextMeteorToHandle >= meteorites.size();}

    /**
     * Method which helps to print the card into the TUI
     * @return string which print the card
     */
    @Override
    public String toString() {
        String str =
                "Meteor swarm / Stray big meteors\n" +
                        "    Meteors: ";

        str += meteorites.getFirst().toString();
        for(int i = 1; i < meteorites.size(); i++){
            str +=  ", " + meteorites.get(i).toString();
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
    public MeteorSwarm clone() {return (MeteorSwarm) super.clone();}
}
