package model.DifferentShipComponents;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import model.Projectiles.Orientation;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstract class used in order to represent a ship component:
 * it is extended in all the different kind of components allowed
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BatteryComponent.class, name = "Battery_Component"),
        @JsonSubTypes.Type(value = Cabin.class, name = "Cabin"),
        @JsonSubTypes.Type(value= Cannon.class, name= "Cannon"),
        @JsonSubTypes.Type(value = CargoHold.class, name = "Cargo_Hold"),
        @JsonSubTypes.Type(value = Engine.class, name = "Engine"),
        @JsonSubTypes.Type(value= LifeSupport.class, name= "Life_Support"),
        @JsonSubTypes.Type(value = Shield.class, name = "Shield"),
        @JsonSubTypes.Type(value = Structural.class, name = "Structural"),
        @JsonSubTypes.Type(value= UnavailableSlot.class, name= "Unavailable_Slot")
})

public abstract class ShipComponent implements Serializable {

    /**
     * Kind of component
     */
    private final ComponentType type;

    /**
     * List of the different sides which a component can have
     */
    private final ArrayList<Side> sides;

    /**
     * Specific ID of a component
     */
    private final int ID;

    /**
     * Path to the image of the component
     */
    final String image;

    /**
     * Object orientation to understand the rotations of the component
     */
    Orientation orientation;

    /**
     * Constructor of the ship component, which initialize the different parameters
     * @param type current kind
     * @param northSide north side
     * @param eastSide east side
     * @param southSide south side
     * @param westSide west side
     * @param ID specific ID
     * @param image path for image
     */
    public ShipComponent(ComponentType type,Side northSide, Side eastSide, Side southSide, Side westSide, int ID, String image){
        this.type = type;
        sides= new ArrayList<>(4);
        sides.add(0, northSide);
        sides.add(1, eastSide);
        sides.add(2, southSide);
        sides.add(3, westSide);
        this.ID = ID;
        this.image=image;
        orientation = Orientation.NORTH;
    }

    /**
     * Method called to clockwise rotate the sides of the component
     * @see #clockwisePositiveRotation() where it's implemented
     * @param numOf90DegRotations number of rotations
     */
    public void clockwiseRotation(int numOf90DegRotations) {
        for (int i = 0; i < numOf90DegRotations; i++) {
            clockwisePositiveRotation();
        }
    }

    /**
     * Private method which applies the clockwise rotation on the component
     */
    private void clockwisePositiveRotation() {
        Side temp = this.sides.getLast();
        for (int i = this.sides.size() - 1; i > 0; i--) {
            this.sides.set(i, sides.get(i-1));
        }
        sides.set(0, temp);

        orientation = Orientation.clockwiseRotation(orientation, 1);
    }

    /**
     * Method called to counterclockwise rotate the sides of the component
     * @see #counterClockwiseNegativeRotation()  where it's implemented
     * @param numOf90DegRotations number of rotations
     */
    public void counterClockwiseRotation (int numOf90DegRotations) {
        for (int i = 0; i < numOf90DegRotations; i++) {
            counterClockwiseNegativeRotation();
        }
    }

    /**
     * Private method which applies the counter clockwise rotation on the component
     */
    private void counterClockwiseNegativeRotation() {
        Side temp = this.sides.getFirst();
        for (int i = 0;  i < this.sides.size() - 1; i++) {
            this.sides.set(i, this.sides.get(i+1));
        }
        this.sides.set(3, temp);

        orientation = Orientation.clockwiseRotation(orientation, -1);
    }

    /**
     * Method which applies a single 90 degrees counterclockwise rotation
     */
    public void counterClockwiseRotation(){counterClockwiseRotation(1);}

    /**
     * Method which applies a single 90 degrees clockwise rotation
     */
    public void clockwiseRotation(){clockwiseRotation(1);}

    /**
     * Getter of all sides
     * @return list of sides
     */
    public ArrayList<Side> getSides(){return sides;}

    /**
     * Getter of the north side
     * @return north side
     */
    public Side getNorthSide() { return sides.getFirst(); }

    /**
     * Getter of the east side
     * @return east side
     */
    public Side getEastSide() { return sides.get(1); }

    /**
     * Getter of the south side
     * @return south side
     */
    public Side getSouthSide() { return sides.get(2); }

    /**
     * Getter of the west side
     * @return west side
     */
    public Side getWestSide() { return sides.get(3); }

    /**
     * Getter of the ID of the component
     * @return ID of the component
     */
    public int getID(){ return ID; }

    /**
     * Getter of the path to the image
     * @return path to the image of the component in sources
     */
    public String getImage(){ return image;}

    /**
     * It checks if the component is of a specific kind
     * @param type current kind of component
     * @return true if it's the same kind of component
     */
    public boolean isType(ComponentType type) { return (this.type == type); }

    /**
     * Getter of the orientation
     * @return current orientation
     */
    public Orientation getOrientation() { return orientation; }

    /**
     * Setter of the orientation
     * @param newOrientation new orientation to set
     */
    public void setOrientation(Orientation newOrientation) { orientation = newOrientation; }

    /**
     * It duplicates the component
     * @return same component
     */
    abstract public ShipComponent clone();

    /**
     * Getter of the kind
     * @return kind of component
     */
    public ComponentType getType(){return type;}

    /**
     * It checks if the object passed in is an instance of the same class,
     * if it is, it checks if all the attributes of the object are equal
     * to this object
     * @param o object to compare
     * @return true if the object is equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ShipComponent comp)) {
            return false;
        }
		boolean result = true;
        result &= this.getID() == comp.getID();
        result &= this.getOrientation() == comp.getOrientation();
        result &= this.getNorthSide() == comp.getNorthSide();
        result &= this.getEastSide() == comp.getEastSide();
        result &= this.getSouthSide() == comp.getSouthSide();
        result &= this.getWestSide() == comp.getWestSide();
        return result;
    }
}
