package model.Cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.CardVisitorProgresser.*;
import model.NextGameStateAndMessages;
import model.ShipDashboard;

/**
 * Extension of card: it represents the abandoned ship
 * @see Card
 */
public class AbandonedShip extends Card {

    /**
     * Amount of the crew, which has to be lost
     */
    public final int crewLoss;

    /**
     * Amount of the days, which has to be lost
     */
    public final int daysLoss;

    /**
     * Amount of credits which can earned
     */
    public final int creditsGained;

    /**
     * Constructor of the card: it initializes the attributes
     * @param humanLoss human loss of the card
     * @param daysLoss days loss of the card
     * @param creditsGained amount of credits which can be earned
     * @param lvl level of the card
     * @param image path to the image
     */
    @JsonCreator
    public AbandonedShip(
            @JsonProperty("human_loss") int humanLoss,
            @JsonProperty("days_loss") int daysLoss,
            @JsonProperty("credits_gained") int creditsGained,
            @JsonProperty("level") int lvl,
            @JsonProperty("image") String image
    ) {
        super("AbandonedShip", lvl, image);
        this.crewLoss = humanLoss;
        this.daysLoss = daysLoss;
        this.creditsGained = creditsGained;
    }
    //count the crew to check if the player can use this card
    //THIS CHECK SHOULD BE DONE IN THE CLIENT
    /*public boolean checkEligibility (ShipDashboard player) {
        //variable to save the number to compare with humanLoss
        int numberCrew;
        boolean permit = false;

        numberCrew= player.getCrew();
        if(numberCrew>=humanLoss){
            permit = true;
        }
        return permit;
    }*/

    /**
     * Method which applies the effect of the card to the players
     * @see ShipDashboard
     * @param player list of players
     */
    public void bonusMalus (ShipDashboard player) {
        player.bonusMalus(daysLoss, creditsGained);
    }

    /**
     * Method which helps to print the card into the TUI
     * @return string which print the card
     */
    @Override
    public String toString() {

        return "Abandoned ship\n" +
                "    Reward: " + creditsGained + " credits\n" +
                "    Days lost: " + daysLoss + "\n" +
                "    Humans lost: " + crewLoss + "\n";
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
    public AbandonedShip clone() {
        return (AbandonedShip) super.clone();
    }
}

