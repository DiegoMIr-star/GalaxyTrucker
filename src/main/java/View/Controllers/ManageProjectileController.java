package View.Controllers;

import Connections.ClientInterface;
import View.GUI;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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
import model.DifferentShipComponents.*;
import model.Projectiles.Orientation;
import model.Projectiles.Projectile;
import model.ShipDashboard;
import model.exceptions.IllegalPositionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static View.ColorManagement.ConsoleColor.*;

/**
 * Controller responsible for handling the manage projectile phase
 */
public class ManageProjectileController {

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
     * Attribute used to store the current projectile's trajectory
     */
    private int trajectory;

    /**
     * Attribute used to store the current projectile
     */
    private Projectile projectile;

    /**
     * Final attribute used to store the coordinates of the component hit
     */
    private final int[] hitCoordinates = new int[2];

    /**
     * Flag that is true if the player hasn't clicked the ok button for the first time
     */
    private boolean firstClickOK = true;

    /**
     * Attribute used to save the index of the piece kept after the break
     */
    private int keptShipPieceIndex = 0;

    /**
     * Main method to handle this game phase:
     * it sets up the scene;
     * based on the type of projectile, it calls {@link #handleBigMeteor(Projectile, ShipDashboard)}, {@link #handleSmallMeteor(Projectile, ShipDashboard)}, {@link #handleHeavyShot(Projectile, ShipDashboard)} or {@link #handleLightShot(Projectile, ShipDashboard)};
     * then updates the ship and closes the scene
     * @param clientInterface is the current client
     * @param projectile is the projectile to handle
     * @param trajectory is the integer that represents the trajectory
     */
    public void handleProjectile(Projectile projectile, int trajectory, ClientInterface clientInterface){
        this.clientInterface = clientInterface;
        this.projectile = projectile;
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
            printShip();

            updateText.setText("A " + projectile + " is incoming towards your ship to " +projectile.getDirection());
            this.trajectory = clientInterface.ship.convertTrajectoryToShipSpace(trajectory, projectile);
            yesButton.setDisable(true);
            noButton.setDisable(true);
            yesButton.setVisible(false);
            noButton.setVisible(false);
            inputField.setDisable(true);
            inputField.setVisible(false);
            okButton.setOnAction(_ -> {
                if (firstClickOK) {
                    firstClickOK = false;
                    switch(projectile.getType()) {
                        case SMALL_ASTEROID -> handleSmallMeteor(projectile, clientInterface.ship);
                        case BIG_ASTEROID -> handleBigMeteor(projectile, clientInterface.ship);
                        case LIGHT_SHOT -> handleLightShot(projectile, clientInterface.ship);
                        case HEAVY_SHOT -> handleHeavyShot(projectile, clientInterface.ship);
                    }
                    return;
                }
                clientInterface.ship.updateShip(clientInterface.ship.getBrokenPieces().get(keptShipPieceIndex));
                try {
                    clientInterface.sendUpdatedShip();
                    Stage stage = (Stage) okButton.getScene().getWindow();
                    stage.close();
                } catch (IOException ex) {
                    printProblem(ex);
                }
            });

        } catch (Exception e) {
            printProblem(e);
        }
    }

    /**
     * Method used to handle small meteor effect:
     * it checks and notifies the player if the meteor misses the ship,
     * if the hit side has some connectors and if a shield is available, asks to activate it {@link #askShield()},
     * if the meteor breaks the component or hits a smooth side
     * @param ship is the current ship attacked
     * @param smallMeteor is the current meteor
     */
    private void handleSmallMeteor(Projectile smallMeteor, ShipDashboard ship) {
        Orientation meteorDirection = smallMeteor.getDirection();
        ship.getHitComponent(smallMeteor, trajectory, hitCoordinates);
        //if no component was hit
        if(hitCoordinates[0] == -1 || hitCoordinates[1] == -1) {
            updateText.setText("The meteor missed the ship, phew!");
            okButton.setDisable(false);
            okButton.setVisible(true);
            return;
        }
        printShipWithHighlightedElement(hitCoordinates[1], hitCoordinates[0]);
        ShipComponent hitComponent = ship.getShip()[hitCoordinates[1]][hitCoordinates[0]];

//if small meteor hits component with exposed connectors
        if((meteorDirection == Orientation.SOUTH && hitComponent.getNorthSide() != Side.BlankSide) ||
                (meteorDirection == Orientation.WEST && hitComponent.getEastSide() != Side.BlankSide) ||
                (meteorDirection == Orientation.NORTH && hitComponent.getSouthSide() != Side.BlankSide) ||
                (meteorDirection == Orientation.EAST && hitComponent.getWestSide() != Side.BlankSide)) {
            if (getShieldsProtectingAgainst(clientInterface.getShip(), smallMeteor).isEmpty()) {
                updateText.setText("Unfortunately your ship doesn't have any shields protecting that side.\n" +
                        "Brace for impact");
                try {
                    breakComponent(hitCoordinates[0], hitCoordinates[1], ship);
                } catch (Exception e) {
                    printProblem(e);
                }
                yesButton.setDisable(true);
                noButton.setDisable(true);
                okButton.setDisable(false);
                okButton.setVisible(true);
                return;
            }
            askShield();
        }
        else{
            updateText.setText("The small meteor bounced on a smooth side and left nothing but a minuscule scratch");
            okButton.setDisable(false);
            okButton.setVisible(true);
        }
    }

    /**
     * Method used to handle big meteor effect:
     * it checks if there are eligible cannons and enough batteries, in order to give the opportunity to activate a double cannon
     * @param ship is the current ship attacked
     * @param bigMeteor is the current meteor
     */
    private void handleBigMeteor(Projectile bigMeteor, ShipDashboard ship) {
        Orientation meteorDirection = bigMeteor.getDirection();
        ship.getHitComponent(bigMeteor, trajectory, hitCoordinates);
        //if no component was hit
        if(hitCoordinates[0] == -1 || hitCoordinates[1] == -1) {
            updateText.setText("The meteor missed the ship, phew!");
            okButton.setDisable(false);
            okButton.setVisible(true);
            return;
        }
        printShipWithHighlightedElement(hitCoordinates[1], hitCoordinates[0]);
        ShipComponent hitComponent = ship.getShip()[hitCoordinates[1]][hitCoordinates[0]];

        ArrayList<Cannon> eligibleCannons = ship.getEligibleCannons(bigMeteor, trajectory);
        for(Cannon cannon : eligibleCannons) {
            if(!cannon.isDouble()) {
                updateText.setText("Your ship has a regular cannon pointing at the big meteor, \nso it was able to destroy it before it caused any damage.");
                okButton.setDisable(false);
                okButton.setVisible(true);
                return;
            }
        }
        //if this line is reached, then the list is either empty or only contains double cannons
        if(eligibleCannons.isEmpty())
        {
            updateText.setText("Your ship doesn't have any cannons able to shoot the big meteor. \nBrace for impact.");
            try {
                breakComponent(hitCoordinates[0], hitCoordinates[1], ship);
            } catch (Exception e) {
                printProblem(e);
            }
            return;
        }
        //else. The list must contain only double cannons
        if(clientInterface.getShipBatteries() == 0){
            updateText.setText("You have a double cannon pointing at the big meteor, \n" +
                    "but unfortunately you don't have any batteries to activate it with.\n" +
                     "Brace for impact.");
            try {
                breakComponent(hitCoordinates[0], hitCoordinates[1], ship);
            } catch(Exception e) {
                printProblem(e);
            }
            return;
        }
        askToActivateDoubleMotor(bigMeteor);
    }

    /**
     * Method used to handle light shot effect:
     * it checks if there are shields available, and in the positive scenario, asks to activate the shield {@link #askShield()}
     * @param ship is the current ship attacked
     * @param lightShot is the current shot to handle
     */
    private void handleLightShot(Projectile lightShot, ShipDashboard ship) {
        ship.getHitComponent(lightShot, trajectory, hitCoordinates);
        //if no component was hit
        if(hitCoordinates[0] == -1 || hitCoordinates[1] == -1) {
            updateText.setText("The shot missed the ship, phew!");
            okButton.setDisable(false);
            okButton.setVisible(true);
            return;
        }
        printShipWithHighlightedElement(hitCoordinates[1], hitCoordinates[0]);
        if (getShieldsProtectingAgainst(clientInterface.getShip(), lightShot).isEmpty()) {
            updateText.setText("Unfortunately your ship doesn't have any shields protecting that side.\n" +
                    "Brace for impact");
            try {
                breakComponent(hitCoordinates[0], hitCoordinates[1], ship);
            } catch (Exception e) {
                printProblem(e);
            }
            yesButton.setDisable(true);
            noButton.setDisable(true);
            okButton.setDisable(false);
            okButton.setVisible(true);
            return;
        }
        askShield();
    }

    /**
     * Method used to handle heavy shot effect:
     * the component is damaged, if it's hit
     * @param ship is the current ship attacked
     * @param heavyShot is the current shot to handle
     */
    private void handleHeavyShot(Projectile heavyShot, ShipDashboard ship) {
        ship.getHitComponent(heavyShot, trajectory, hitCoordinates);
        //if no component was hit
        if(hitCoordinates[0] == -1 || hitCoordinates[1] == -1) {
            updateText.setText("The shot missed the ship, phew!");
            okButton.setDisable(false);
            okButton.setVisible(true);
            return;
        }
        printShipWithHighlightedElement(hitCoordinates[1], hitCoordinates[0]);
        updateText.setText("The heavy shot hit your ship");
        try {
            breakComponent(hitCoordinates[0], hitCoordinates[1], ship);
        } catch (Exception e) {
            printProblem(e);
        }
        okButton.setDisable(false);
        okButton.setVisible(true);
    }

    /**
     * Method used to ask player to activate the shield:
     * it asks to choose to activate the shield,
     * it checks if there are batteries available, and in the positive scenario, it activates the shield
     * otherwise, the ship is damaged
     */
    private void askShield() {
        updateText.setText("Do you want to activate a shield to protect your ship? Cost: 1 battery");
        okButton.setDisable(true);
        okButton.setVisible(false);
        yesButton.setDisable(false);
        noButton.setDisable(false);
        yesButton.setVisible(true);
        noButton.setVisible(true);
        yesButton.setOnAction(_ -> {
            if(clientInterface.getShipBatteries() == 0){
                updateText.setText("Unfortunately you don't have batteries to activate any shields.");
                yesButton.setDisable(true);
                noButton.setDisable(true);
                okButton.setDisable(false);
                okButton.setVisible(true);
                try {
                    breakComponent(hitCoordinates[0], hitCoordinates[1], clientInterface.ship);
                } catch (Exception e) {
                    printProblem(e);
                }
                return;
            }
            updateText.setText("You activated a shield and protected your ship");
            clientInterface.useShipBatteries(1);
            yesButton.setDisable(true);
            noButton.setDisable(true);
            okButton.setDisable(false);
            okButton.setVisible(true);
        });
        noButton.setOnAction(_ -> {
            updateText.setText("Your ship has been hit and damaged");
            yesButton.setDisable(true);
            noButton.setDisable(true);
            try {
                breakComponent(hitCoordinates[0], hitCoordinates[1], clientInterface.ship);
            } catch (Exception e1) {
                printProblem(e1);
            }
        });
    }

    /**
     * Method used to remove a specific component from the ship:
     * it checks if the coordinates are valid,
     * it adds the piece in the garbage,
     * it prints the updated ship without that component and calls {@link #askShipPiece(ArrayList)}
     * @param ship is the current ship with the component to remove
     * @param x is the column coordinate of the component to remove
     * @param y is the row coordinate of the component to remove
     */
    public void breakComponent (int x, int y, ShipDashboard ship) throws IllegalPositionException {
        ArrayList<ShipComponent[][]> brokenShipPieces;

        if(y<0||y>=ship.getShip().length||x<0||x>=ship.getShip()[y].length) {
            throw new IllegalPositionException("The provided coordinates are outside of the index range");
        }
        if(ship.isUnavailableSlot(x,y))
            return;

        ship.getShip()[y][x] = new UnavailableSlot();
        clientInterface.addGarbage(1);

        brokenShipPieces = ship.getBrokenPieces();
        printShipWithoutBrokenComponent(y, x);
        askShipPiece(brokenShipPieces);
    }

    /**
     * Method used to ask player to choose the ship part of the broken ship to keep
     * @param shipPieces is the list of ship fragments of the broken ship
     */
    private void askShipPiece(ArrayList<ShipComponent[][]> shipPieces){
        if(shipPieces.size() == 1){
            keptShipPieceIndex = 0;
            okButton.setDisable(false);
            okButton.setVisible(true);
            return;
        }
        //printShipPieces();
        updateText.setText("Your ship has been broken off into " + shipPieces.size() + " pieces.\n" +
                "Which ship piece do you want to continue your journey with? The rest will be added to your garbage pile.");

        inputField.setDisable(false);
        inputField.setVisible(true);
        inputField.setOnAction(_ -> {
            try {
                int index = Integer.parseInt(inputField.getText());
                if (index <= 0 || index > shipPieces.size()) {
                    warningText.setText("That ship piece doesn't exist");
                    inputField.clear();
                }
                else {
                    keptShipPieceIndex = index-1;
                    inputField.setDisable(true);
                    okButton.setDisable(false);
                    okButton.setVisible(true);
                }

            } catch (NumberFormatException ex) {
                warningText.setText("Please enter a number");
                inputField.clear();
            }
        });
    }

    /**
     * Method that handles the player's activation choice,
     * it also manages the scene and buttons accordingly to the choice
     * @param bigMeteor is the current projectile to handle
     */
    public void askToActivateDoubleMotor(Projectile bigMeteor) {
        updateText.setText("The " + bigMeteor + " is directed right toward your ship, but it's not too late. \nYou have a double cannon ready to blow the asteroid to smithereens,\nbut it will have to use one of your "
                + clientInterface.getShipBatteries() +" batteries. \n\nDo you want to take the opportunity? ");
        okButton.setDisable(true);
        okButton.setVisible(false);
        yesButton.setDisable(false);
        noButton.setDisable(false);
        yesButton.setVisible(true);
        noButton.setVisible(true);
        try {
            yesButton.setOnAction(_ -> {
                updateText.setText("You activated the double cannon and destroyed the " + bigMeteor + ".");
                clientInterface.useShipBatteries(1);
                yesButton.setVisible(false);
                noButton.setVisible(false);
                okButton.setVisible(true);
                yesButton.setDisable(true);
                noButton.setDisable(true);
                okButton.setDisable(false);
            });
            noButton.setOnAction(_ -> {
                updateText.setText("You decided to do nothing and watch the \nbig asteroid crash into your ship.");
                try {
                    breakComponent(hitCoordinates[0], hitCoordinates[1], clientInterface.ship);
                } catch (Exception e1) {
                    printProblem(e1);
                }
                yesButton.setVisible(false);
                noButton.setVisible(false);
                okButton.setVisible(true);
                yesButton.setDisable(true);
                noButton.setDisable(true);
                okButton.setDisable(false);
            });
            okButton.setOnAction(_ -> {
                try {
                    clientInterface.sendUpdatedShip();
                } catch (Exception e1) {
                    printProblem(e1);
                }
                Stage stage = (Stage) okButton.getScene().getWindow();
                stage.close();
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method used to check the ship for shields that can protect against the projectile
     * @param ship is the current ship
     * @param projectile is the current projectile to handle
     * @return a list of shields that can protect the ship
     */
    private ArrayList<Shield> getShieldsProtectingAgainst(ShipComponent[][] ship, Projectile projectile){
        ArrayList<Shield> shields = new ArrayList<>();
        for (ShipComponent[] shipComponents : ship) {
            for (ShipComponent shipComponent : shipComponents) {
                switch (shipComponent) {
                    case Shield shield:
                        switch (projectile.getDirection()) {
                            case Orientation.NORTH:
                                if (isSideShield(shield.getSouthSide()))
                                    shields.add(shield);
                                break;
                            case Orientation.EAST:
                                if (isSideShield(shield.getWestSide()))
                                    shields.add(shield);
                                break;
                            case Orientation.SOUTH:
                                if (isSideShield(shield.getNorthSide()))
                                    shields.add(shield);
                                break;
                            case Orientation.WEST:
                                if (isSideShield(shield.getEastSide()))
                                    shields.add(shield);
                                break;
                        }
                    default:
                        break;
                }
            }
        }
        return shields;
    }

    /**
     * Method used to check if the considered side has a shield
     * @param side the current side considered
     * @return true if there's the shield
     */
    private boolean isSideShield(Side side){
        return (side == Side.ShieldProtection || side == Side.ShieldAndSingleConnector ||
                side == Side.ShieldAndDoubleConnector || side == Side.ShieldAndUniversalConnector);
    }

    /**
     * Method used to print the ship with a highlighted component
     * @param hitC the highlighted column coordinate
     * @param hitR the highlighted row coordinate
     */
    private void printShipWithHighlightedElement(int hitR, int hitC) {
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
                    if (r == hitR && c == hitC && gridImages[r][c].getImage() != null) {
                        Blend blend = new Blend(BlendMode.MULTIPLY, null, new ColorInput(0, 0, 121, 121, Color.color(1, 0, 0, 0.5)));
                        gridImages[r][c].setEffect(blend);
                    }

                }
            }
        } catch (Exception e) {
            printProblem(e);
        }
    }

    /**
     * Method used to print the ship without a specific component
     * @param hitC the column coordinate of the piece to remove
     * @param hitR the row coordinate of the piece to remove
     */
    private void printShipWithoutBrokenComponent(int hitR, int hitC) {
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
                    if (r == hitR && c == hitC && gridImages[r][c].getImage() != null) {
                        gridImages[r][c].setImage(null);
                    }

                }
            }
        } catch (Exception e) {
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
