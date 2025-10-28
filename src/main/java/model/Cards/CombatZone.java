package model.Cards;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.CardVisitorProgresser.*;
import model.NextGameStateAndMessages;
import model.Projectiles.Projectile;
import model.ShipDashboard;

import static model.Game.rollTheDice;

/**
 * Extension of card: it represents the combat zone
 * @see Card
 */
public class CombatZone extends Card {

    /**
     * Days lost signed in the first line of the card
     */
    public final int daysLossLine1;

    /**
     * Crew lost signed in the second line of the card
     */
    public final int crewLossLine2;

    /**
     * Shots contained in the third line of the card
     */
    public final Projectile[] hitsLine3;

    /**
     * It marks the current index of projectile
     */
    private int currProjectileIndex;

    /**
     * It marks the minimum firepower indicated
     */
    private double lowestFirePower;

    /**
     * It marks the minimum motor power indicated
     */
    private int lowestMotorPower;

    /**
     * It contains the penalty stocks that has to be paid
     */
    public final int stocksPenaltyLine2;

    /**
     * Contains the index of the player affected by the current line of the card, based on their stats
     */
    private int affectedPlayerIndex;

    /**
     * Constructor of the card: it initializes the attributes
     * @param lvl level of the card
     * @param daysLoss days which has to be lost
     * @param crewLoss crew which has to be lost
     * @param stocksPenalty penalty stocks
     * @param hits current shots
     * @param image path to the image
     */
    @JsonCreator
    public CombatZone(
            @JsonProperty("level") int lvl,
            @JsonProperty("days_loss") int daysLoss,
            @JsonProperty("crew_loss") int crewLoss,
            @JsonProperty("stocks_penalty") int stocksPenalty,
            @JsonProperty("hits") Projectile[] hits,
            @JsonProperty("image") String image
    ) {
        super("Combat Zone", lvl, image);
        this.daysLossLine1 = daysLoss; //daysLoss has to be a negative number
        this.crewLossLine2 = crewLoss;
        hitsLine3 = hits;
        currProjectileIndex = 0;
        this.stocksPenaltyLine2 = stocksPenalty;
        this.affectedPlayerIndex = -1;
    }

    /**
     * It applies the penalty of the card to the right player
     * @see ShipDashboard
     * @param players list of players
     */
    public void Line1(ArrayList<ShipDashboard> players) {
        int lessCrew;
        int crew;
        int affectedPlayerIndex;

        lessCrew = players.getFirst().getCrew();
        affectedPlayerIndex = 0;

        for (int i = 1; i < players.size(); i++) {
            crew = players.get(i).getCrew();
            //if crew = lessCrew, the player which is in a higher position gets the penalty. so it is sufficient to let
            //the previous player in the array be affected, since in the first position of the array there's the leader,
            //and to update the affectedPlayer only if crew < lessCrew
            if (crew < lessCrew) {
                lessCrew = crew;
                affectedPlayerIndex = i;
            }
        }
        players.get(affectedPlayerIndex).setDaysToMove(-this.daysLossLine1);
        this.affectedPlayerIndex = affectedPlayerIndex;
    }

    /**
     * @return the index of the affected player
     */
    public int getAffectedPlayerIndex() {
        return affectedPlayerIndex;
    }


    /**
     * It updates the index of the affected player
     */
    public void setAffectedPlayerIndex(int affectedPlayerIndex) {
        this.affectedPlayerIndex = affectedPlayerIndex;
    }

    /**
     * It updates the current projectile to be handled
     */
    public void markShotAsHandled() {currProjectileIndex++;}

    /**
     * It returns a random value as trajectory
     * @return random trajectory
     */
    public int getRandomTrajectory() {return rollTheDice();}

    /**
     * It says if the shots are all handled
     * @return true if the shots are all handled
     */
    public boolean allShotsHandled() {return currProjectileIndex == hitsLine3.length;}

    /**
     * It returns the next hit to be handled
     * @return next shot
     */
    public Projectile getNextHitToHandle(){return hitsLine3[currProjectileIndex];}

    /**
     * Getter of the lowest firepower
     * @return current lowest firepower
     */
    public double getLowestFirePower() {return lowestFirePower;}

    /**
     * Getter of the lowest motor power
     * @return current lowest motor power
     */
    public int getLowestMotorPower() {return lowestMotorPower;}

    /**
     * Setter of the lowest firepower
     * @param lowestFirePower current lowest firepower
     */
    public void setLowestFirePower(double lowestFirePower) {this.lowestFirePower = lowestFirePower;}

    /**
     * Setter of the lowest motor power
     * @param lowestMotorPower current lowest motor power
     */
    public void setLowestMotorPower(int lowestMotorPower) {this.lowestMotorPower = lowestMotorPower;}

    /**
     * Method which helps to print the card into the TUI
     * @return string which print the card
     */
    @Override
    public String toString() {
        String str =
                "Combat zone\n" +
                        "    Player with the smallest crew - days lost: " + daysLossLine1 + "\n" +
                        "    Player with the weakest engines - " +
                        (crewLossLine2 != 0 ? "crew lost: " + crewLossLine2 : "stocks lost: " + stocksPenaltyLine2) + "\n" +
                        "    Player with the weakest cannons - shots: ";

        str += hitsLine3[0].toString();
        for(int i = 1; i < hitsLine3.length; i++){
            str +=  ", " + hitsLine3[i].toString();
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
    public CombatZone clone() {
        return (CombatZone) super.clone();
    }
}
