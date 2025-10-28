
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
import javafx.stage.Stage;
import model.DifferentShipComponents.Cannon;
import model.DifferentShipComponents.ComponentType;
import model.DifferentShipComponents.ShipComponent;
import model.Projectiles.Orientation;

import java.util.Objects;

/**
 * Controller responsible for handling activation of double cannons requests
 */
public class AskDoubleCannonController {

    /**
     * Label necessary for GUI visualization, to communicate with clients
     */
    @FXML
    private Label batteriesAndPowerUpdateLabel, infoLabel;

    /**
     * Buttons necessary for GUI visualization, to receive responses from clients
     */
    @FXML
    private Button okButton, finishButton;

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

    /**
     * Attribute used to save the double that represents dynamic power of a player
     */
    private double dynamicPower = 0;

    /**
     * Attribute used to save the integer that represents the number of used batteries of a player
     */
    private int usedBatteries = 0;

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
     * Method used to handle the initialization of the scene:
     * it assembles the ship with images, displays the ship and all information regarding the activation of double cannons
     * @param clientInterface is the current client manager
     */
    public void setGridAndBackgroundAndInitializeScene(ClientInterface clientInterface) {
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
        catch (Exception e) {
            printProblem(e);
        }

        this.clientInterface = clientInterface;

        printShip();

        batteriesAndPowerUpdateLabel.setText("You have the chance to activate double cannons by spending \nbatteries to increase your fire power."+
            "\nYour current fire power is: " + clientInterface.getShipFirePower() + ", and you have " + clientInterface.getShipBatteries() + " batteries left.");

        if (clientInterface.getShipBatteries() <= 0) {
            infoLabel.setText("Unfortunately you don't have any batteries to activate double cannons.");
            finishButton.setDisable(true);
            finishButton.setVisible(false);
            okButton.setOnAction(_ -> {
                okButton.setDisable(true);
                try{
                    clientInterface.sendDynamicFirePower(0);
                }
                catch(Exception e){
                    printProblem(e);
                }
                Stage stage = (Stage) finishButton.getScene().getWindow();
                stage.close();
            });
            return;
        }
        if (clientInterface.getShipDoubleCannons() <= 0) {
            infoLabel.setText("Unfortunately you don't have any double cannons to activate.");
            okButton.setOnAction(_ -> {
                okButton.setDisable(true);
                try{
                    clientInterface.sendDynamicFirePower(0);
                }
                catch(Exception e){
                    printProblem(e);
                }
                Stage stage = (Stage) finishButton.getScene().getWindow();
                stage.close();
            });
            return;
        }

        okButton.setOnAction(_ -> {
            okButton.setDisable(true);
            okButton.setVisible(false);
            highlightAndEnableDoubleCannons();
            finishButton.setDisable(false);
            finishButton.setVisible(true);
            infoLabel.setText("Click on the highlighted double cannons to activate them!\n" +
                    "When you have finished, click on FINISH.");
        });

        finishButton.setDisable(true);
        finishButton.setVisible(false);
        finishButton.setOnAction(_ -> {
            try {
                finishActivateCannon();
                finishButton.setDisable(true);
            } catch (Exception e) {
                printProblem(e);
            }
        });
    }

    /**
     * Method used to handle the end of the scene related to the activation of double cannons
     */
    private void finishActivateCannon(){
        clientInterface.useShipBatteries(usedBatteries);
        try{
            clientInterface.sendUpdatedShip();
            clientInterface.sendDynamicFirePower(dynamicPower);
        }
        catch(Exception e){
            printProblem(e);
        }
        Stage stage = (Stage) finishButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Method used to handle the effectively updates the state after the activation
     * @param r is the row coordinate of the double cannon to active
     * @param c is the column coordinate of the double cannon to activate
     */
    private void activateCannonAndUpdateFirePower(int r, int c) {
        Cannon cannon = (Cannon) clientInterface.ship.getShip()[r][c];
        usedBatteries++;
        dynamicPower += (cannon.getOrientation() == Orientation.NORTH ? 2 : 1);
        batteriesAndPowerUpdateLabel.setText("Your current fire power is: " + (clientInterface.getShipFirePower() + dynamicPower) + ", and you have " +
                (clientInterface.ship.getBatteries()-usedBatteries) + " batteries left.");

    }

    /**
     * Method used to notify the player that has consumed all the batteries and displays the current dynamic power
     */
    private void handleLastBatteryUsed() {
        infoLabel.setText("You have used all your batteries!\n\n" +
                "Your final fire power is: " + (clientInterface.getShipFirePower() + dynamicPower) +
                "\n\nClick on FINISH when you are ready to continue.");
        printShip();
        finishButton.setDisable(false);
        finishButton.setVisible(true);

    }

    /**
     * Method used to highlight double cannons to let the player choose:
     * if there is the last battery available, it activates the cannon and notifies about the end of batteries {@link #handleLastBatteryUsed()}
     * if batteries available are more than one, it activates the cannon
     */
    private void highlightAndEnableDoubleCannons() {
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
                    if (component.getType().equals(ComponentType.CANNON) && gridImages[r][c].getImage() != null) {
                        Cannon cannon = (Cannon) component;
                        if (cannon.isDouble()) {
                            Blend blend = new Blend(BlendMode.MULTIPLY, null, new ColorInput(0, 0, 121, 121, Color.color(0, 0, 1, 0.5)));
                            gridImages[r][c].setEffect(blend);
                            int finalR = r, finalC = c;
                            gridImages[r][c].setOnMouseClicked(_ -> {
                                if (usedBatteries + 1 == clientInterface.getShipBatteries()) {
                                    gridImages[finalR][finalC].setEffect(null);
                                    gridImages[finalR][finalC].setOnMouseClicked(null);
                                    activateCannonAndUpdateFirePower(finalR, finalC);
                                    handleLastBatteryUsed();
                                    return;
                                }
                                gridImages[finalR][finalC].setEffect(null);
                                gridImages[finalR][finalC].setOnMouseClicked(null);
                                activateCannonAndUpdateFirePower(finalR, finalC);
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
        try{
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    component = clientInterface.ship.getShip()[r][c];
                    if(!(component != null && component.getImage() != null && !component.getImage().isEmpty()))
                        continue;
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/"+component.getImage())));
                    gridImages[r][c].setImage(image);
                    gridImages[r][c].setOnMouseClicked(_ -> {});
                    gridImages[r][c].setRotate(-90*(Orientation.getCounterClockwise90DegRotationFromOrientation(component.getOrientation())));
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
