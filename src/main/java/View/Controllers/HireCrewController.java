package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.DifferentShipComponents.Cabin;
import model.DifferentShipComponents.ShipComponent;
import model.Projectiles.Orientation;

import java.util.Objects;

/**
 * Controller responsible for handling the crew hiring phase
 */
public class HireCrewController {

    /**
     * Labels for GUI visualization:
     * the first one is for a question
     * the second one is to communicate an update
     */
    @FXML
    Label questionText, updateText;

    /**
     * Buttons used to hire humans or aliens and to confirm
     */
    @FXML
    Button humansButton, alienButton, ok;

    /**
     * Grid necessary to organize GUI visualization of the ship
     */
    @FXML
    private GridPane grid;

    /**
     * Image for GUI visualization, used as background
     */
    @FXML
    ImageView backgroundImage;

    /**
     * Current client manager
     */
    private ClientInterface clientInterface;

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
     * Attribute used to store the ship components matrix
     */
    private ShipComponent[][] ship;

    /**
     * Attributes used to update coordinates during iterations
     */
    private int lastX = 0, lastY = 0;
    private int x = 0, y = 0;

    /**
     * Flags that store the presence or absence of the alien
     */
    boolean shipAlreadyHasPurpleAlien = false;
    boolean shipAlreadyHasBrownAlien = false;

    /**
     * Method used to initialize the ship and the background,
     * moreover it calls the method {@link #hireCrew()} when ok is clicked
     */
    public void setGridAndBackground() {
        try {
            backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/cardboard-1b.jpg")).toExternalForm()));
            updateText.setText("Now it's time to hire crew!");
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    ImageView cell = new ImageView();
                    cell.setFitWidth(121);
                    cell.setFitHeight(121);
                    cell.setPickOnBounds(true);
                    cell.setOnMouseClicked(_ -> {
                    });
                    grid.add(cell, c, r);
                    gridImages[r][c] = cell;
                }
            }
            ok.setOnMouseClicked(_ -> {
                setAllButtonsDisabled(false);
                hideAllLabels();
                try {
                    hireCrew();
                } catch (Exception e) {
                    printError(e);
                }
            });
            setAllButtonsDisabled(false);
            ok.setVisible(true);
            ok.setDisable(false);
        }
        catch(Exception e){
            printError(e);
        }
    }

    /**
     * Method that visits all cabins and appropriately hires crew members
     * once all cabins have been visited, the player is notified
     */
    private void hireCrew(){
        lastY = y;
        boolean first_time = true;
        boolean breakTrue = false;
        while (lastY < ship.length) {
            if (first_time) {
                first_time = false;
                lastX = x;
            }
            else {
                lastX = 0;
            }
            try {
                while (lastX < ship[lastY].length) {
                    switch (ship[lastY][lastX]) {
                        case Cabin cabin:
                            highlightCabin(lastY, lastX);
                            if (cabin.getIsCentral()) {
                                cabin.setHumans(true, true);
                                updateText.setVisible(true);
                                setUpdateText("2 humans have been hired to live in the central cabin.");
                                ok.setVisible(true);
                                ok.setDisable(false);
                                x = lastX + 1;
                                if (x == ship[lastY].length) {
                                    x = 0;
                                    y = lastY + 1;
                                } else {
                                    y = lastY;
                                }
                                breakTrue = true;
                                break;
                            } else {
                                if ((!cabin.hasPurpleLifeSupport() || shipAlreadyHasPurpleAlien) && (!cabin.hasBrownLifeSupport() || shipAlreadyHasBrownAlien)) {
                                    cabin.setHumans(true, true);
                                    updateText.setVisible(true);
                                    setUpdateText("2 humans have been hired to live in the highlighted cabin.");
                                    ok.setVisible(true);
                                    ok.setDisable(false);
                                    x = lastX + 1;
                                    if (x == ship[lastY].length) {
                                        x = 0;
                                        y = lastY + 1;
                                    } else {
                                        y = lastY;
                                    }
                                    breakTrue = true;
                                    break;
                                } else {
                                    if (cabin.hasPurpleLifeSupport() && !shipAlreadyHasPurpleAlien) {
                                        hireCrewPurpleAlien(cabin);
                                        x = lastX + 1;
                                        if (x == ship[lastY].length) {
                                            x = 0;
                                            y = lastY + 1;
                                        } else {
                                            y = lastY;
                                        }
                                        breakTrue = true;
                                        break;
                                    }
                                    if (cabin.hasBrownLifeSupport() && !shipAlreadyHasBrownAlien) {
                                        hireCrewBrownAlien(cabin);
                                        x = lastX + 1;
                                        if (x == ship[lastY].length) {
                                            x = 0;
                                            y = lastY + 1;
                                        } else {
                                            y = lastY;
                                        }
                                        breakTrue = true;
                                        break;
                                    }
                                }
                            }
                        default:
                            break;
                    }
                    if (breakTrue) {
                        break;
                    }
                    lastX++;
                }
            }
            catch(Exception e){
                printError(e);
            }
            if (breakTrue) {
                break;
            }
            else {
                lastY++;
            }
        }
        if (lastY == ship.length) {
            updateText.setVisible(true);
            setUpdateText("Every cabin has been successfully filled with crew members. \nNow the ship can be placed on the board.");
            clientInterface.ship.initializeShipAttributesFromComponents();
            try{
                clientInterface.sendShip();
            }
            catch (Exception e){
                printError(e);
            }
        }
    }

    /**
     * Method that lets the player choose between purple alien and humans
     * @param cabin is the cabin to populate
     */
    public void hireCrewPurpleAlien(Cabin cabin) {
        alienButton.setText("purple alien");
        setAllButtonsDisabled(true);
        ok.setDisable(true);
        questionText.setVisible(true);
        questionText.setText("Who do you want to hire to live in the cabin? \n\n" +
                "Hiring a purple alien will provide you \n +2 FIRE POWER");
        try {
            humansButton.setOnMouseClicked(_ -> {
                humansButton.setDisable(true);
                alienButton.setDisable(true);
                ok.setDisable(false);
                updateText.setVisible(true);
                setUpdateText("2 humans have been hired to live in the highlighted cabin.");
                cabin.setHumans(true, true);
                cabin.setPurpleAlien(false);
                cabin.setBrownAlien(false);
                shipAlreadyHasPurpleAlien = false;
            });
            alienButton.setOnMouseClicked(_ -> {
                humansButton.setDisable(true);
                alienButton.setDisable(true);
                ok.setDisable(false);
                updateText.setVisible(true);
                setUpdateText("1 purple alien has been hired to live in the highlighted cabin.");
                cabin.setHumans(false, false);
                cabin.setPurpleAlien(true);
                cabin.setBrownAlien(false);
                shipAlreadyHasPurpleAlien = true;
            });
        }
        catch(Exception e){
            printError(e);
        }
    }

    /**
     * Method that lets the player choose between brown alien and humans
     * @param cabin is the cabin to populate
     */
    public void hireCrewBrownAlien(Cabin cabin) {
        alienButton.setText("brown alien");
        setAllButtonsDisabled(true);
        ok.setDisable(true);
        questionText.setVisible(true);
        questionText.setText("Who do you want to hire to live in the cabin? \n\n" +
                "Hiring a brown alien will provide you \n +2 MOTOR POWER");
        try {
            humansButton.setOnMouseClicked(_ -> {
                humansButton.setDisable(true);
                alienButton.setDisable(true);
                ok.setDisable(false);
                updateText.setVisible(true);
                setUpdateText("2 humans have been hired to live in the highlighted cabin.");
                cabin.setHumans(true, true);
                cabin.setPurpleAlien(false);
                cabin.setBrownAlien(false);
                shipAlreadyHasBrownAlien = false;
            });
            alienButton.setOnMouseClicked(_ -> {
                humansButton.setDisable(true);
                alienButton.setDisable(true);
                ok.setDisable(false);
                updateText.setVisible(true);
                setUpdateText("1 brown alien has been hired to live in the highlighted cabin.");
                cabin.setHumans(false, false);
                cabin.setPurpleAlien(false);
                cabin.setBrownAlien(true);
                shipAlreadyHasBrownAlien = true;
            });
        }
        catch(Exception e){
            printError(e);
        }
    }

    /**
     * Setter of the current client interface and initializes the ship reference
     * @param clientInterface current client interface
     */
    public void setClientInterface(ClientInterface clientInterface) {

        this.clientInterface = clientInterface;
        this.ship = clientInterface.getShip();
    }

    /**
     * Setter of the buttons enabled or disabled
     * @param allButtonsDisabled if it's true, all the buttons are enabled, if it's false disabled
     */
    public void setAllButtonsDisabled(boolean allButtonsDisabled) {
        if (allButtonsDisabled) {
            humansButton.setDisable(false);
            alienButton.setDisable(false);
            ok.setDisable(false);
            return;
        }
        humansButton.setDisable(true);
        alienButton.setDisable(true);
        ok.setDisable(true);
    }

    /**
     * Method that hides all the labels
     */
    public void hideAllLabels() {
        questionText.setVisible(false);
        updateText.setVisible(false);
    }

    /**
     * Setter of the text of the label
     * @param updateText current text to display
     */
    public void setUpdateText(String updateText) {
        this.updateText.setText(updateText);
    }

    /**
     * Method used to highlight a cabin of the ship in green
     * @param rCabin is the row coordinate of the cabin
     * @param cCabin is the column coordinate of the cabin
     */
    public void highlightCabin(int rCabin, int cCabin) {
        ShipComponent component;
        try {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    component = clientInterface.ship.getShip()[r][c];
                    if(!(component != null && component.getImage() != null && !component.getImage().isEmpty()))
                        continue;
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/" + component.getImage())));
                    gridImages[r][c].setImage(image);
                    gridImages[r][c].setOnMouseClicked(_ -> {
                    });
                    gridImages[r][c].setRotate(-90 * (Orientation.getCounterClockwise90DegRotationFromOrientation(component.getOrientation())));
                    if (r == rCabin && c == cCabin && component.getImage() != null) {
                        Blend blend = new Blend(BlendMode.MULTIPLY, null, new ColorInput(0, 0, 121, 121, Color.color(0, 1, 0, 0.5)));
                        gridImages[r][c].setEffect(blend);
                    } else {
                        gridImages[r][c].setEffect(null);
                    }
                }
            }
        }
        catch(Exception e){
            printError(e);
        }
    }

    /**
     * Method used to print the ship
     */
    public void printShip() {
        ShipComponent component;
        try {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    component = clientInterface.ship.getShip()[r][c];
                    if(component != null && component.getImage() != null && !component.getImage().isEmpty()) {
                        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/" + component.getImage())));
                        gridImages[r][c].setImage(image);
                        gridImages[r][c].setOnMouseClicked(_ -> {
                        });
                        gridImages[r][c].setRotate(-90 * (Orientation.getCounterClockwise90DegRotationFromOrientation(component.getOrientation())));
                    }
                }
            }
        }
        catch(Exception e){
            printError(e);
        }
    }

    /**
     * Method used to handle the exceptions related to printing issues
     * @param e is the current exception
     */
    private void printError(Exception e){
        e.printStackTrace();
    }
}
