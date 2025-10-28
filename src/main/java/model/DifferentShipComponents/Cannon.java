package model.DifferentShipComponents;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Extension of ship component: it represents the cannon
 * @see ShipComponent
 */
public class Cannon extends ShipComponent{

    /**
     * True if the cannon is double
     */
    private final boolean isDouble;

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
    public Cannon(
            @JsonProperty("north_side") Side northSide,
            @JsonProperty("east_side") Side eastSide,
            @JsonProperty("south_side") Side southSide,
            @JsonProperty("west_side") Side westSide,
            @JsonProperty("double") boolean isDouble,
            @JsonProperty("ID") int ID,
            @JsonProperty("Image") String image
    ){
        super(ComponentType.CANNON,northSide,eastSide,southSide,westSide,ID,image);
        this.isDouble=isDouble;
    }

    /**
     * Constructor of the specific component: it initializes the attributes without ID
     * @param northSide current north side
     * @param eastSide current east side
     * @param southSide current south side
     * @param westSide current west side
     * @param image path to the image
     */
    public Cannon(
            @JsonProperty("north_side") Side northSide,
            @JsonProperty("east_side") Side eastSide,
            @JsonProperty("south_side") Side southSide,
            @JsonProperty("west_side") Side westSide,
            @JsonProperty("double") boolean isDouble,
            @JsonProperty("Image") String image
    ){
        super(ComponentType.CANNON,northSide,eastSide,southSide,westSide, -1,image);
        this.isDouble=isDouble;
    }

    /**
     * It says if there's a double cannon
     * @return true if the cannon is double
     */
    public boolean isDouble(){return isDouble;}

    /**
     * It creates a copy of the specific component
     * @return copy of the component
     */
    @Override
    public ShipComponent clone() {
        Cannon newComp = new Cannon(this.getNorthSide(), this.getEastSide(), this.getSouthSide(),
                this.getWestSide(), this.isDouble, this.getID(), this.image);
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
        if(!(o instanceof Cannon comp)){ return false; }
		boolean result = true;
        result &= super.equals(o);
        result &= this.isDouble == comp.isDouble;
        return result;
    }
}