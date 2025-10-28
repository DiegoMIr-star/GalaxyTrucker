package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.DifferentShipComponents.ShipComponent;
import model.Projectiles.Orientation;
import model.ShipDashboard;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling the ship fixing phase
 */
public class FixController implements Initializable {

    /**
     * Grid necessary to organize GUI visualization
     */
    @FXML
    private GridPane grid;

    /**
     * Image for GUI visualization, used as background
     */
    @FXML
    ImageView backgroundImage;

    /**
     * Label used to communicate messages to the player
     */
    @FXML
    Label text;

    /**
     * Current client manager
     */
    private ClientInterface clientInterface;

    /**
     * Attribute used to store the ship components matrix
     */
    private ShipComponent[][] ship;

    /**
     * Attribute which represents the ship
     */
    private ShipDashboard shipDashboard;

    /**
     * Attribute, a matrix of booleans,
     * that indicates the presence of a correct or incorrect component in each position
     */
    private boolean[][] wrongComponents;

    /**
     * Final attribute used to save the integer that represents the number of rows
     */
    private final int ROWS = 5;

    /**
     * Final attribute used to save the integer that represents the number of columns
     */
    private final int COLS = 7;

    /**
     * Final attribute used to save the matrix of images of components that represents the ship
     */
    private final ImageView[][] gridImages = new ImageView[ROWS][COLS];

    /**
     * Empty method that is required by the interface{@link Initializable}
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Method used to handle the set the scene:
     * it assembles the ship with images and sets the background image
     */
    public void setGridAndBackground() {
        try {
            backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/cardboard-1b.jpg")).toExternalForm()));
            text.setText("Just fix your ship!");
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    ImageView cell = new ImageView();
                    cell.setFitWidth(121);
                    cell.setFitHeight(121);
                    cell.setPickOnBounds(true);
                    grid.add(cell, c, r);
                    gridImages[r][c] = cell;
                }
            }
        }
        catch(Exception e) {
            printProblem(e);
        }
    }

    /**
     * Method used to set the ship as current to apply the fixing part:
     * @param ship is the current ship to check
     */
    public void setShip(ShipDashboard ship) {
        this.shipDashboard = ship;
        this.ship=shipDashboard.getShip();
    }

    /**
     * Setter of the current client interface
     * @param clientInterface current client interface
     */
    public void setClientInterface (ClientInterface clientInterface) {
        this.clientInterface = clientInterface;
    }

    /**
     * Method used to remove a component if it's not correct
     * @param r is the row coordinate of the considered component
     * @param c is the column coordinate of the considered component
     */
    private void removeComponent(int r, int c) {
        if (r != 2 || c != 3) {
            if (clientInterface.ship.checkShip()[r][c]) {
                gridImages[r][c].setImage(null);
                shipDashboard.removeComponent(c, r);
                this.wrongComponents = shipDashboard.checkShip();
                ship = shipDashboard.getShip();
                printShip();
                setShipWithErrors();
            }
        }
    }

    /**
     * Setter of the wrong component in the error components matrix
     * @param wrongComponents is the error components matrix (true indicates the presence of error)
     */
    public void setCheck(boolean[][] wrongComponents) {
        this.wrongComponents = wrongComponents;
    }

    /**
     * Method used to check if the current ship is  completely fixed
     * @return boolean: if it's true it means that the ship is fixed (no wrong components)
     */
    public boolean checkIfFixed() {
        return !shipDashboard.shipNeedsFixing(shipDashboard.checkShip());
    }

    /**
     * Method used to show the ship with wrong components highlighted in red
     * when a component is wrong the player has to click it to remove it
     * when the ship is fully fixed, this method notifies the player
     */
    public void setShipWithErrors() {
        ShipComponent component;
        try {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    component = ship[r][c];
                    if (component != null && component.getImage() != null && !component.getImage().isEmpty()) {
                        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/" + component.getImage())));
                        gridImages[r][c].setImage(image);
                        gridImages[r][c].setRotate(-90 * (Orientation.getCounterClockwise90DegRotationFromOrientation(component.getOrientation())));
                        if (wrongComponents[r][c]) {
                            Blend blend = new Blend(BlendMode.MULTIPLY, null, new ColorInput(0, 0, 121, 121, Color.color(1, 0, 0, 0.5)));
                            gridImages[r][c].setEffect(blend);
                            int finalR = r, finalC = c;
                            gridImages[finalR][finalC].setOnMouseClicked(_ -> {
                                try {
                                    removeComponent(finalR, finalC);
                                } catch (Exception e) {
                                    printProblem(e);
                                }
                            });
                        } else {
                            gridImages[r][c].setEffect(null);
                        }
                    } else {
                        gridImages[r][c].setImage(null);
                    }
                }
            }
            if (checkIfFixed()) {
                clientInterface.ship.connectLifeSupports();
                text.setText("Your ship is \n now fixed! \nClose the \nwindow to go on \nwith the game :)");
            }
        }
        catch(Exception e){
            printProblem(e);
        }
    }

    /**
     * Method used to print the ship
     */
    private void printShip() {
        ShipComponent component;
        try{
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    component = clientInterface.ship.getShip()[r][c];
                    if(component != null && component.getImage() != null && !component.getImage().isEmpty()){
                        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/"+component.getImage())));
                        gridImages[r][c].setImage(image);
                        gridImages[r][c].setOnMouseClicked(_ -> {});
                        gridImages[r][c].setRotate(-90*(Orientation.getCounterClockwise90DegRotationFromOrientation(component.getOrientation())));
                    }
                }
            }
        }
        catch(Exception e){
            printProblem(e);
        }
    }

    /**
     * Method used to handle the exceptions related to printing issues
     * @param e is the current exception
     */
    private void printProblem(Exception e){
        e.printStackTrace();
    }
}
