package model.DifferentShipComponents;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Extension of ship component: it represents the life support
 * @see ShipComponent
 */
public class LifeSupport extends ShipComponent{

    /**
     * True if the life support is purple, false if it's brown
     */
    private final boolean colorIsPurple;

    /**
     * Constructor of the specific component: it initializes the attributes
     * @param northSide current north side
     * @param eastSide current east side
     * @param southSide current south side
     * @param westSide current west side
     * @param isPurple true if the life support is purple
     * @param ID specific ID
     * @param image path to the image
     */
    @JsonCreator
    public LifeSupport(
            @JsonProperty("north_side") Side northSide,
            @JsonProperty("east_side") Side eastSide,
            @JsonProperty("south_side") Side southSide,
            @JsonProperty("west_side") Side westSide,
            @JsonProperty("purple") boolean isPurple,
            @JsonProperty("ID") int ID,
            @JsonProperty("Image") String image
    ){
        super(ComponentType.LIFE_SUPPORT,northSide,eastSide,southSide,westSide, ID,image);
        colorIsPurple=isPurple;
    }

    /**
     * Constructor of the specific component: it initializes the attributes without ID
     * @param northSide current north side
     * @param eastSide current east side
     * @param southSide current south side
     * @param westSide current west side
     * @param isPurple true if the life support is purple
     * @param image path to the image
     */
    public LifeSupport(
            @JsonProperty("north_side") Side northSide,
            @JsonProperty("east_side") Side eastSide,
            @JsonProperty("south_side") Side southSide,
            @JsonProperty("west_side") Side westSide,
            @JsonProperty("purple") boolean isPurple,
            @JsonProperty("Image") String image
    ){
        super(ComponentType.LIFE_SUPPORT,northSide,eastSide,southSide,westSide, -1,image);
        colorIsPurple=isPurple;
    }

    /**
     * It says if the life support is purple or brown
     * @return true if it's purple, false otherwise
     */
    public boolean isColorPurple() {return colorIsPurple;}

    /**
     * It creates a copy of the specific component
     * @return copy of the component
     */
    @Override
    public ShipComponent clone() {
        LifeSupport newComp = new LifeSupport(this.getNorthSide(), this.getEastSide(), this.getSouthSide(),
                this.getWestSide(), this.isColorPurple(), this.getID(), this.image);
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
        if(!(o instanceof LifeSupport comp)){ return false; }
		boolean result = true;
        result &= super.equals(o);
        result &= this.colorIsPurple == comp.colorIsPurple;
        return result;
    }
}