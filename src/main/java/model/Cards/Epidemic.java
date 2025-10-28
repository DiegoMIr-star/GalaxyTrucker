package model.Cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.CardVisitorProgresser.*;
import model.NextGameStateAndMessages;
import model.ShipDashboard;

import java.util.ArrayList;

/**
 * Extension of card: it represents the epidemic card
 * @see Card
 */
public class Epidemic extends Card {

    /**
     * Constructor of the card: it initializes the attribute
     * @param lvl level of the card
     * @param image path to the image
     */
    @JsonCreator
    public Epidemic(
            @JsonProperty("level") int lvl,
            @JsonProperty("image") String image
    ) {
        super("Epidemic", lvl, image);
    }

    /**
     * It removes a member of the crew from each cabin next to another occupied
     * cabin
     * @see ShipDashboard
     * @param players list of current players
     */
    public void apply(ArrayList<ShipDashboard> players){
        for (ShipDashboard player : players) {
            player.removeConnectedCrew();
        }
    }

    /**
     * Method which helps to print the card into the TUI
     * @return string which print the card
     */
    @Override
    public String toString() {return "Epidemic\n";}

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
    public Epidemic clone() {return (Epidemic) super.clone();}
}