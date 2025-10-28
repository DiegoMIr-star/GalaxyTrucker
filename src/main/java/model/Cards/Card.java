package model.Cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import model.Cards.CardVisitorProgresser.*;
import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.NextGameStateAndMessages;

import java.io.Serializable;

/**
 * Abstract class used in order to represent a card:
 * it is extended in all the different kind of cards allowed
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CombatZone.class, name = "Combat_Zone"),
        @JsonSubTypes.Type(value = Epidemic.class, name = "Epidemic"),
        @JsonSubTypes.Type(value= MeteorSwarm.class, name= "Meteor_Swarm"),
        @JsonSubTypes.Type(value = OpenSpace.class, name = "Open_Space"),
        @JsonSubTypes.Type(value = Planets.class, name = "Planets"),
        @JsonSubTypes.Type(value= Stardust.class, name= "Stardust"),
        @JsonSubTypes.Type(value = AbandonedShip.class, name = "Abandoned_Ship"),
        @JsonSubTypes.Type(value = AbandonedStation.class, name = "Abandoned_Station"),
        @JsonSubTypes.Type(value= Pirates.class, name= "Pirates"),
        @JsonSubTypes.Type(value = Slavers.class, name = "Slavers"),
        @JsonSubTypes.Type(value = Smugglers.class, name = "Smugglers")
})

public abstract class Card implements Serializable, Cloneable {

    /**
     * Name of the specific card
     */
    public final String name;

    /**
     * Level of the card
     */
    public final int lvl;

    /**
     * Path to the image of the card
     */
    private final String image;

    /**
     * Constructor of the card: it initializes the attributes
     * @param name name of the card
     * @param lvl level of the card
     * @param image path to the image
     */
    @JsonCreator
    public Card (
            @JsonProperty("type") String name,
            @JsonProperty("level") int lvl,
            @JsonProperty("image") String image) {
        this.name = name;
        this.lvl = lvl;
        this.image = image;
    }

    /**
     * It returns the path to the image
     * @return string with the path
     */
    public String getImage() { return this.image; }

    /**
     * Method used to apply the effect of the card by using the Visitor pattern:
     * this method is abstract so it can be overridden from the other extensions
     * @see CardVisitHandlerForClient
     * @see CardVisitHandlerForServer
     * @param visitor object visitor which contains the logic of the card
     */
    public abstract void apply(CardVisitor visitor);

    /**
     * Method to return some parameters of the card according to Visitor patter
     * @see CardVisitReturner
     * @param visitor object visitor which contains the logic of the card
     * @return value
     */
    public abstract int returner(CardVisitReturner visitor);

    /**
     * Method used to return state and messages according to the specific effect of the card
     * @see Controller.Controller
     * @param visitor object visitor which contains the logic of the card
     * @return next game and messages
     */
    public abstract NextGameStateAndMessages apply(CardVisProg_state visitor);

    /**
     * Method used to return the copy of the card
     * @return copy of the card
     */
    @Override
    public Card clone() {
        try {
            Card clone = (Card) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
