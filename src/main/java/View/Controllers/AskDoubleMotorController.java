package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.DifferentShipComponents.ComponentType;
import model.DifferentShipComponents.Engine;
import model.DifferentShipComponents.ShipComponent;
import model.Projectiles.Orientation;

import java.util.Objects;

/**
 * Controller responsible for handling activation of double motors requests
 */
public class AskDoubleMotorController {

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
     * Button necessary for GUI visualization, to receive ok response from client
     */
    @FXML
    Button okButton;

    /**
     * Current client manager
     */
    private ClientInterface client;

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
     * Attribute necessary to store number of double motors desired to be activated from the player
     * and consequently the number of batteries to use
     */
    private int num = 0;

    /**
     * Attribute necessary to store the starting choice of the client
     */
    private boolean firstClickOK = true;

    /**
     * Method used to handle the initialization of the scene:
     * it assembles the ship with images, displays the ship,
     * asks for the activation {@link #startAskDoubleMotor()}
     * and at the end updates the game with modifies
     */
    public void setGridAndBackground() {
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
        }
        catch(Exception e){
            printError(e);
        }
        printShip();
        okButton.setOnAction(_ -> {
            if (firstClickOK) {
                firstClickOK = false;
                printShipWithHighlightedElements();
                startAskDoubleMotor();
                return;
            }
            inputField.clear();
            client.useShipBatteries(num);
            try {
                client.sendUpdatedShip();
            } catch (Exception e) {
                printError(e);
            }
            try {
                client.sendDynamicMotorPower(num);
            } catch (Exception e) {
                printError(e);
            }
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Method used to handle the request of activation, checking if it's possible or not
     * @param motors represents number of motors of the player
     * @param batteries represents number of batteries of the player
     */
    private void activateDoubleMotors(int motors, int batteries) {
        try{
            int num = Integer.parseInt(inputField.getText());

            if(num<0){
                warningText.setText("Please enter a number >= 0");
                inputField.clear();
                return;
            }

            if(num>motors){
                warningText.setText("You don't have that many motors!");
                inputField.clear();
            }
            else if(num>batteries){
                warningText.setText("You don't have enough batteries to activate " + num + " motors.");
                inputField.clear();
            }
            else{
                this.num = num;
                updateText.setText("You have activated " + num + " double motors!");
                inputField.setDisable(true);
                //inputField.setVisible(false);
                okButton.setVisible(true);
                okButton.setDisable(false);
            }
        }catch(Exception e){
            updateText.setText("Invalid input");
            inputField.clear();
            printError(e);
        }
    }

    /**
     * Method used to notify the client of the request of activation
     * @param client is the current client manager
     */
    public void initializeMeth(ClientInterface client) {
        this.client = client;

        updateText.setText("You have the chance to activate double motors by spending batteries. ");
        inputField.setDisable(true);
        //inputField.setVisible(false);
        okButton.setVisible(true);
        okButton.setDisable(false);
    }

    /**
     * Method used to inform the client about the current state of motor power, double motors to activate and batteries available
     */
    private void startAskDoubleMotor() {
        if (client.getShipDoubleMotors() <= 0) {
            updateText.setText("You have no double motors to activate.");
            inputField.setDisable(true);
            //inputField.setVisible(false);
            okButton.setVisible(true);
            okButton.setDisable(false);
            return;
        }
        if (client.getShipBatteries() <= 0) {
            updateText.setText("You have no batteries available.");
            inputField.setDisable(true);
            //inputField.setVisible(false);
            okButton.setVisible(true);
            okButton.setDisable(false);
            return;
        }

        double motorSpeed = client.getShipMotorPower();
        int doubleMotor = client.getShipDoubleMotors();
        int batteries= client.getShipBatteries();
        updateText.setText("You have the chance to activate double motors by spending batteries. " +
                "\nCurrent motor power: " + motorSpeed + "\n\n You have " + doubleMotor + " double motors and " + batteries + " batteries." +
                "\nHow many double motors would you like to activate?");

        inputField.setDisable(false);
        okButton.setDisable(true);

        inputField.setOnAction(_ -> activateDoubleMotors(client.getShipDoubleMotors(), client.getShipBatteries())
        );
    }

    /**
     * Method used to display the ship and highlight double motors
     */
    private void printShipWithHighlightedElements() {
        ShipComponent component;
        try {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    component = client.ship.getShip()[r][c];
                    if(!(component != null && component.getImage() != null && !component.getImage().isEmpty()))
                        continue;
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/" + component.getImage())));
                    gridImages[r][c].setImage(image);
                    gridImages[r][c].setOnMouseClicked(_ -> {
                    });
                    gridImages[r][c].setRotate(-90 * (Orientation.getCounterClockwise90DegRotationFromOrientation(component.getOrientation())));
                    if (component.getType().equals(ComponentType.ENGINE) && gridImages[r][c].getImage() != null) {
                        Engine engine = (Engine) component;
                        if (engine.isDouble()) {
                            Blend blend = new Blend(BlendMode.MULTIPLY, null, new ColorInput(0, 0, 121, 121, Color.color(0, 0, 1, 0.5)));
                            gridImages[r][c].setEffect(blend);
                        }
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
    private void printShip() {
        ShipComponent component;
        try {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    component = client.ship.getShip()[r][c];
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
