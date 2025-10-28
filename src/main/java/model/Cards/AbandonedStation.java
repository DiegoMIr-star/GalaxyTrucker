package model.Cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.CardVisitorProgresser.*;
import model.NextGameStateAndMessages;
import model.Stocks;
import model.ShipDashboard;

/**
 * Extension of card: it represents the abandoned station
 * @see Card
 */
public class AbandonedStation extends Card {

    /**
     * Amount of the days, which has to be lost
     */
    public final int daysLoss;

    /**
     * Amount of the crew required to apply the effect of the card
     */
    public final int requiredCrew;

    /**
     * Stocks present in the card, which can be earned from a player
     */
    public final Stocks stocks;

    /**
     * Constructor of the card: it initializes the attributes
     * @param daysLoss days loss of the card
     * @param requiredCrew amount of the crew required for the effect
     * @param stocks current stocks of the card
     * @param lvl level of the card
     * @param image path to the image
     */
    @JsonCreator
    public AbandonedStation(
            @JsonProperty("days_loss") int daysLoss,
            @JsonProperty("requires_crew") int requiredCrew,
            @JsonProperty("stocks") Stocks stocks,
            @JsonProperty("level") int lvl,
            @JsonProperty("image") String image
            ) {
        super("AbandonedStation", lvl, image);
        this.daysLoss = daysLoss;
        this.requiredCrew = requiredCrew;
        this.stocks = stocks;
    }

    //this check should be done in the client
    /*public boolean checkEligibility (ShipDashboard player) {
        //variable to save the number to compare with humanLoss
        int numberCrew;
        boolean permit = false;

        numberCrew= player.getCrew();
        if(numberCrew>=requiredCrew) {
            permit = true;
        }
        return permit;
    }*/

    /**
     * It sets the days to move into a player
     * @see ShipDashboard
     * @param player current player
     */
    public void loseDays (ShipDashboard player) {player.setDaysToMove(-daysLoss);}

    /**
     * Method which helps to print the card into the TUI
     * @return string which print the card
     */
    @Override
    public String toString() {
        String str =
                "Abandoned station\n" +
                        "    Reward: " + stocks.toString() + "\n" +
                        "    Days lost: " + daysLoss + "\n" +
                        "    Required crew: " + requiredCrew + "\n";


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
    public AbandonedStation clone() {
        return (AbandonedStation) super.clone();
    }
}