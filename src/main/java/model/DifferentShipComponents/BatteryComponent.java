package model.DifferentShipComponents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Extension of ship component: it represents the hold for batteries
 * @see ShipComponent
 */
public class BatteryComponent extends ShipComponent{

    /**
     * Max amount of batteries hold in the component
     */
    private final int maxAmount;

    /**
     * Current number of batteries
     */
    private int batteries;

    /**
     * Constructor of the specific component: it initializes the attributes
     * @param northSide current north side
     * @param eastSide current east side
     * @param southSide current south side
     * @param westSide current west side
     * @param batteries number of batteries
     * @param ID specific ID
     * @param image path to the image
     */
    @JsonCreator
    public BatteryComponent(
            @JsonProperty("north_side") Side northSide,
            @JsonProperty("east_side") Side eastSide,
            @JsonProperty("south_side") Side southSide,
            @JsonProperty("west_side") Side westSide,
            @JsonProperty("batteries") int batteries,
            @JsonProperty("ID") int ID,
            @JsonProperty("Image") String image
    ){
        super(ComponentType.BATTERY_COMPONENT,northSide,eastSide,southSide,westSide, ID, image);
        maxAmount = batteries;
        this.batteries = batteries;
    }

    /**
     * Constructor of the specific component: it initializes the attributes without ID
     * @param northSide current north side
     * @param eastSide current east side
     * @param southSide current south side
     * @param westSide current west side
     * @param batteries number of batteries
     * @param image path to the image
     */
    public BatteryComponent(
            @JsonProperty("north_side") Side northSide,
            @JsonProperty("east_side") Side eastSide,
            @JsonProperty("south_side") Side southSide,
            @JsonProperty("west_side") Side westSide,
            @JsonProperty("batteries") int batteries,
            @JsonProperty("Image") String image
    ){
        super(ComponentType.BATTERY_COMPONENT,northSide,eastSide,southSide,westSide, -1,image);
        maxAmount = batteries;
        this.batteries = batteries;
    }

    /**
     * Getter of the max amount of batteries
     * @return max amount of batteries
     */
    public int getMaxAmount(){return maxAmount;}

    /**
     * Getter of the current number of batteries
     * @return current amount of batteries
     */
    public int getCurrentBatteries(){ return batteries; }

    /**
     * It takes from the component a certain amount of batteries
     * @param amount specific amount
     */
    public void useBatteries(int amount){batteries -= amount;}

    /**
     * It sets the current amount of batteries into 0
     */
    public void useAllBatteries(){batteries = 0;}

    /**
     * It creates a copy of the specific component
     * @return copy of the component
     */
    @Override
    public ShipComponent clone() {
        BatteryComponent newComp = new BatteryComponent(this.getNorthSide(), this.getEastSide(), this.getSouthSide(),
                this.getWestSide(), maxAmount, this.getID(), this.image);
        newComp.setOrientation(orientation);
        newComp.batteries = batteries;

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
        if(!(o instanceof BatteryComponent comp)){ return false; }
		boolean result = true;
        result &= super.equals(o);
        result &= this.maxAmount == comp.getMaxAmount();
        result &= this.batteries == comp.batteries;
        return result;
    }
}