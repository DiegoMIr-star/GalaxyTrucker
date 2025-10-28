package model.Cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.CardVisitorProgresser.*;
import model.NextGameStateAndMessages;
import model.ShipDashboard;

import java.util.ArrayList;

/**
 * Extension of card: it represents the stardust card
 * @see Card
 */
public class Stardust extends Card {

    /**
     * It represents the number of exposed connectors
     */
    int exposedConnectors = 0;

    /**
     * Constructor of the card: it initializes the attribute
     * @param lvl level of the card
     * @param image path to the image
     */
    @JsonCreator
    public Stardust(
            @JsonProperty("level") int lvl,
            @JsonProperty("image") String image
    ) {
        super("Stardust", lvl, image);
    }

    /**
     * It applies the effect of the card, putting the exposed connectors into
     * a method of the ship dashboard
     * @see ShipDashboard
     * @param players current players
     */
    public void apply(ArrayList<ShipDashboard> players) {
        for(ShipDashboard p : players) {
            exposedConnectors = p.getNumOfExposedConnectors();
            p.setDaysToMove(-exposedConnectors);
        }
        //this card requires that the positions are updated from the last player to the leader
    }

    /**
     * Method which helps to print the card into the TUI
     * @return string which print the card
     */
    @Override
    public String toString() {return "Stardust\n";}

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
    public NextGameStateAndMessages apply(CardVisProg_state visitor){return visitor.visit(this);}

    /**
     * Method used to return the copy of the card
     * @return copy of the card
     */
    @Override
    public Stardust clone() {return (Stardust) super.clone();}
}
