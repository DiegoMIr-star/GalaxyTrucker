package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.DifferentShipComponents.Cabin;
import model.DifferentShipComponents.ComponentType;
import model.DifferentShipComponents.ShipComponent;
import model.Projectiles.Orientation;


import java.io.IOException;
import java.util.Objects;

/**
 * Controller responsible for handling give up actions of humans and aliens
 */
public class AskGiveUpCrewController {

    /**
     * Labels necessary for GUI visualization, to communicate with clients
     */
    @FXML
    private Label updateText, warningText;

    /**
     * Text field necessary for GUI visualization, to communicate with clients
     */
    @FXML
    private TextField inputField;

    /**
     * Buttons necessary for GUI visualization, to receive ok, yes and no responses from client
     */
    @FXML
    private Button okButton, noButton, yesButton;

    /**
     * Image for GUI visualization, used as background
     */
    @FXML
    private ImageView backgroundImage;

    /**
     * Grid necessary to organize GUI visualization for the ship
     */
    @FXML
    private GridPane grid;

    /**
     * Current client manager
     */
    private ClientInterface clientInterface;

    //private ShipComponent[][] ship;

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
     * Attribute necessary to store number of crew to give up
     */
    private int numCrewMatesToGiveUp;

    /**
     * Attribute necessary to store the selected cabin where the mates to eliminate are present
     */
    private Cabin selectedCabin = null;

    /**
     * Attributes necessary to store the row and the column of the selected cabin in the ship
     */
    private int selectedCabinRow = -1, selectedCabinCol = -1;

    /**
     * Attribute necessary to store the starting choice of the client
     */
    private boolean firstClickOK = true;

    /**
     * Method used to handle the initialization of the scene and the interactions with the client:
     * it assembles the ship with images, displays the ship {@link #printShipWithHighlightedElements()},
     * asks for the crew removal from a selected cabin,
     * iterates until the number of members to give up is zero
     * and at the end updates the game with modifies
     * @param clientInterface is the current client manager
     * @param numCrewMatesToGiveUp is the number of the member to give up
     */
    public void setGridAndBackground(ClientInterface clientInterface, int numCrewMatesToGiveUp) {
        try {
            backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/cardboard-1b.jpg")).toExternalForm()));
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
            this.clientInterface = clientInterface;
            this.numCrewMatesToGiveUp = numCrewMatesToGiveUp;

            updateText.setText("Now it's time to give up some crew mates!");
            printShip();
            inputField.clear();
            inputField.setDisable(true);
            inputField.setVisible(false);
            noButton.setVisible(false);
            noButton.setDisable(true);
            yesButton.setVisible(false);
            yesButton.setDisable(true);

            if (numCrewMatesToGiveUp > clientInterface.getShipCrew()) {
                warningText.setText("You had to give up more crew mates than you had, so you lost all of them.");
                clientInterface.removeAllCrewmates();
                okButton.setOnAction(_->{
                    try {
                        clientInterface.sendUpdatedShip();
                    } catch (Exception e) {
                        printProblem(e);
                    }
                });
                return;
            }

            okButton.setOnAction(_ -> {
                if (firstClickOK) {
                    firstClickOK = false;
                    updateText.setText("Now it's time to give up some crew mates! " +
                            "\nClick on the highlighted cabin you want to give up crew from, in order to remove them!" +
                            "\n\nYou still have to give up " + numCrewMatesToGiveUp + " crew mates!");
                    okButton.setDisable(true);
                    printShipWithHighlightedElements();
                    return;
                }
                try {
                    clientInterface.sendUpdatedShip();
                    Stage stage = (Stage) okButton.getScene().getWindow();
                    stage.close();
                } catch (Exception e) {
                    printProblem(e);
                }
            });
            noButton.setOnAction(_ -> {
                yesButton.setDisable(true);
                yesButton.setVisible(false);
                noButton.setDisable(true);
                noButton.setVisible(false);
            });
            yesButton.setOnAction(_ -> {
                yesButton.setDisable(true);
                yesButton.setVisible(false);
                noButton.setDisable(true);
                noButton.setVisible(false);
                if (selectedCabin != null) {
                    if (selectedCabin.getBrownAlienEquip()) {
                        clientInterface.ship.removeCrewFromCabin(selectedCabinCol, selectedCabinRow, 0, false, true);
                    } else if (selectedCabin.getPurpleAlienEquip()) {
                        clientInterface.ship.removeCrewFromCabin(selectedCabinCol, selectedCabinRow, 0, true, false);
                    }
                }
                this.numCrewMatesToGiveUp--;
                checkIfLastCrewMateHasBeenGivenUp();
            });
        }
        catch(Exception e){
            printProblem(e);
        }
    }

    /**
     * Method used to check if all the crew members have been given up
     * and disable the scene in positive case
     * or continues to ask for giving up members
     */
    private void checkIfLastCrewMateHasBeenGivenUp() {
        selectedCabin = null;
        if (this.numCrewMatesToGiveUp <= 0) {
            updateText.setText("You gave up all the necessary crew members!");
            printShip();
            okButton.setDisable(false);
        }
        else {
            updateText.setText("You still have to give up" + numCrewMatesToGiveUp + " crew mates!");
            printShip();
            printShipWithHighlightedElements();
        }
        inputField.setDisable(true);
        inputField.setVisible(false);
        noButton.setVisible(false);
        noButton.setDisable(true);
        yesButton.setVisible(false);
        yesButton.setDisable(true);
    }

    /**
     * Method that handles the effective give up of humans after receiving the desired number as input from the client
     */
    private void giveUpHumans() {
        try {
            int humansToGiveUp = Integer.parseInt(inputField.getText());
            if (humansToGiveUp > selectedCabin.getHumans()) {
                inputField.clear();
                warningText.setText("You don't have that many crew members!");
            } else if (humansToGiveUp <= 0) {
                inputField.clear();
                warningText.setText("Please enter a positive number!");
            }
            else if (humansToGiveUp > numCrewMatesToGiveUp) {
                inputField.clear();
                warningText.setText("You don't have to give up that many crew mates!");
            }
            else if (humansToGiveUp <= selectedCabin.getHumans() && humansToGiveUp <= numCrewMatesToGiveUp) {
                inputField.clear();
                inputField.setDisable(true);
                numCrewMatesToGiveUp-=humansToGiveUp;
                clientInterface.ship.removeCrewFromCabin(selectedCabinCol, selectedCabinRow, humansToGiveUp, false, false);
                checkIfLastCrewMateHasBeenGivenUp();
            }
        } catch(NumberFormatException e) {
            inputField.clear();
            warningText.setText("Please enter a number");
        }

    }

    /**
     * Method used to display the ship and highlight cabins with crew
     * useful to let the player choose the cabin and show the available members
     */
    private void printShipWithHighlightedElements() {
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
                    gridImages[r][c].setEffect(null);
                    if (component.getType().equals(ComponentType.CABIN) && gridImages[r][c].getImage() != null) {
                        Cabin cabin = (Cabin) component;
                        int humans = cabin.getHumans();
                        boolean purple = cabin.getPurpleAlienEquip();
                        boolean brown = cabin.getBrownAlienEquip();
                        if (humans > 0 || brown || purple) {
                            Blend blend = new Blend(BlendMode.MULTIPLY, null, new ColorInput(0, 0, 121, 121, Color.color(0, 0, 1, 0.5)));
                            gridImages[r][c].setEffect(blend);
                            int finalR = r;
                            int finalC = c;
                            gridImages[r][c].setOnMouseClicked(_ -> {
                                selectedCabin = cabin;
                                selectedCabinRow = finalR;
                                selectedCabinCol = finalC;
                                if (humans > 0) {
                                    inputField.setDisable(false);
                                    inputField.setVisible(true);
                                    yesButton.setVisible(false);
                                    yesButton.setDisable(true);
                                    noButton.setVisible(false);
                                    noButton.setDisable(true);
                                    updateText.setText("You still have to give up " + numCrewMatesToGiveUp + " crew mates!" +
                                            "\nThis cabin contains " + humans + " humans! \n\n" +
                                            "How many would you like to give up?"
                                    );
                                    inputField.setOnAction(_ -> giveUpHumans());
                                } else if (brown) {
                                    updateText.setText("You still have to give up " + numCrewMatesToGiveUp + " crew mates!" +
                                            "\nThis cabin contains 1 brown alien! \n\n" +
                                            "Do you want to give it up?"
                                    );
                                    inputField.setVisible(false);
                                    inputField.setDisable(true);
                                    yesButton.setVisible(true);
                                    yesButton.setDisable(false);
                                    noButton.setVisible(true);
                                    noButton.setDisable(false);
                                } else {
                                    updateText.setText("You still have to give up " + numCrewMatesToGiveUp + " crew mates!" +
                                            "\nThis cabin contains 1 purple alien! \n\n" +
                                            "Do you want to give it up?"
                                    );
                                    inputField.setVisible(false);
                                    inputField.setDisable(true);
                                    yesButton.setVisible(true);
                                    yesButton.setDisable(false);
                                    noButton.setVisible(true);
                                    noButton.setDisable(false);
                                }
                            });
                        }
                    }

                }
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
                    gridImages[r][c].setEffect(null);
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
