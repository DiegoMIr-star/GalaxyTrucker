package Loader;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.DifferentShipComponents.ShipComponent;

import java.util.ArrayList;

/**
 * Class used in order to collect the different ship components from json files
 */
public class ObjectsComponent {

    /**
     * List of ship components loaded form json file
     */
    private ArrayList<ShipComponent> shipComponents;

    /**
     * Constructor used to collect the different ship components from json file
     * @param shipComponents list of ship components
     */
    @JsonCreator
    public ObjectsComponent(
            @JsonProperty("shipComponents") ArrayList<ShipComponent> shipComponents
    ) {this.shipComponents = shipComponents;}

    /**
     * Getter of the list of the different ship components loaded from json files
     * @return list of loaded ship components
     */
    public ArrayList<ShipComponent> getShipComponents() { return shipComponents; }

    /**
     * Setter of different ship components
     * @param shipComponents current list of ship components
     */
    public void setShipComponents(ArrayList<ShipComponent> shipComponents) { this.shipComponents = shipComponents; }
}
