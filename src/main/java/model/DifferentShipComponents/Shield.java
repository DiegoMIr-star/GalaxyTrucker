package model.DifferentShipComponents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Extension of ship component: it represents the shield
 * @see ShipComponent
 */
public class Shield extends ShipComponent{

    /**
     * Constructor of the specific component: it initializes the attributes
     * @param northSide current north side
     * @param eastSide current east side
     * @param southSide current south side
     * @param westSide current west side
     * @param ID specific ID
     * @param image path to the image
     */
    @JsonCreator
    public Shield(
            @JsonProperty("north_side") Side northSide,
            @JsonProperty("east_side") Side eastSide,
            @JsonProperty("south_side") Side southSide,
            @JsonProperty("west_side") Side westSide,
            @JsonProperty("ID") int ID,
            @JsonProperty("Image") String image
    ){
        super(ComponentType.SHIELD, northSide, eastSide, southSide, westSide, ID, image);
    }

    /**
     * Constructor of the specific component: it initializes the attributes without ID
     * @param northSide current north side
     * @param eastSide current east side
     * @param southSide current south side
     * @param westSide current west side
     * @param image path to the image
     */
    public Shield(
            @JsonProperty("north_side") Side northSide,
            @JsonProperty("east_side") Side eastSide,
            @JsonProperty("south_side") Side southSide,
            @JsonProperty("west_side") Side westSide,
            @JsonProperty("Image") String image
    ){
        super(ComponentType.SHIELD, northSide, eastSide, southSide, westSide,- 1, image);
    }

    /**
     * It creates a copy of the specific component
     * @return copy of the component
     */
    @Override
    public ShipComponent clone() {
        Shield newComp = new Shield(this.getNorthSide(), this.getEastSide(), this.getSouthSide(),
                this.getWestSide(), this.getID(), this.image);
        newComp.setOrientation(orientation);


        return newComp;
    }

    /**
     * It checks if the object passed in is an instance of the same class,
     * if it is, it checks if all the attributes of the object are equal
     * to this object
     * @param o object to compare
     * @return true if the object is equal
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Shield)){ return false; }
        boolean result;
        result = super.equals(o);
        return result;
    }
}