package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.DifferentShipComponents.ShipComponent;
import model.DifferentShipComponents.UnavailableSlot;
import model.Projectiles.Orientation;
import model.exceptions.BookingSlotsFullException;
import model.exceptions.FloatingComponentException;
import model.exceptions.IllegalPositionException;
import model.exceptions.SlotTakenException;

import java.net.URL;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling the ship component drawing phase in the GUI
 * In particular, it allows the player to place, rotate and book components
 */
public class DrawingController implements Initializable {

    /**
     * Image for GUI visualization, used as background
     */
    @FXML private ImageView backgroundImage;

    /**
     * Grid necessary to organize GUI visualization
     */
    @FXML private GridPane grid;

    /**
     * Buttons necessary for GUI visualization, to draw, rotate left, rotate right and place on board
     */
    @FXML private Button drawButton, rotateLeftButton, rotateRightButton, placeOnTheBoard, drawSmallDeck1,
                            drawSmallDeck2, drawSmallDeck3, turnHourglass, returnButton;

    /**
     * Component of GUI interface that allows to scroll
     */
    @FXML private ScrollPane scrollPane;

    /**
     * Box that contains the components drawn and not placed yet
     */
    @FXML private VBox reservedBox;

    /**
     * Attribute used to store a specific component
     */
    private ShipComponent selectedComponent;

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
     * Attribute used to store the ship layout
     */
    private ShipComponent[][] ship;

    /**
     * True if the player is resumed
     */
    private boolean resumed = false;

    /**
     * Current client manager
     */
    private ClientInterface clientInterface;

    /**
     * Flag used to indicate if the component in [0][5] was booked first than the one in [0][6]
     */
    private boolean comp05IsFirst = false;

    /**
     * Flag used to indicate if the component in [0][6] was booked first than the one in [0][5]
     */
    private boolean comp06IsFirst = false;

    /**
     * Attribute used to indicate the index of the booked component in [0][5] position
     * -1 if there isn't an index
     */
    private int bookedComp05Index = -1;

    /**
     * Attribute used to indicate the index of the booked component in [0][6] position
     * -1 if there isn't an index
     */
    private int bookedComp06Index = -1;

    /**
     * Flag used to indicate if the component in [0][5] is currently selected
     */
    private boolean bookedComp05Selected = false;

    /**
     * Flag used to indicate if the component in [0][6] is currently selected
     */
    private boolean bookedComp06Selected = false;

    /**
     * Empty method that is required by the interface{@link Initializable}
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){}

    /**
     * Main method to handles the effective setup of the board, managing drawings, rotations, positioning and hourglasses
     */
    public void postInitialize(){
        try {
            backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/cardboard-1b.jpg")).toExternalForm()));
            scrollPane.setContent(reservedBox);
            ship = clientInterface.getShip();

            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    ImageView cell = new ImageView();
                    cell.setFitWidth(121);
                    cell.setFitHeight(121);
                    cell.setPickOnBounds(true);
                    int finalR = r, finalC = c;
                    cell.setOnMouseClicked(_ -> {
                        if (finalR == 0 && (finalC == 5 && bookedComp05Index != -1 || finalC == 6 && bookedComp06Index != -1)) {
                            if (finalC == 5) {
                                selectedComponent = clientInterface.getBookedComponent(bookedComp05Index);
                                bookedComp05Selected = true;
                            } else {
                                selectedComponent = clientInterface.getBookedComponent(bookedComp06Index);
                                bookedComp06Selected = true;
                            }
                        } else placeComponent(finalR, finalC);
                    });
                    grid.add(cell, c, r);
                    gridImages[r][c] = cell;
                }
            }
            if(resumed){
                for(int r = 0; r < ROWS; r++){
                    for(int c = 0; c < COLS; c++){
                        ShipComponent component = ship[r][c];
                        if(component != null && !(component instanceof UnavailableSlot)){
                            Image image = new Image(Objects.requireNonNull(getClass().getResource("/"+component.getImage())).toExternalForm());
                            gridImages[r][c].setRotate(-90 * (Orientation.getCounterClockwise90DegRotationFromOrientation(component.getOrientation())));
                            gridImages[r][c].setImage(image);
                        }
                    }
                }
            }
        }
        catch(Exception e){
            printError(e);
        }

        drawButton.setOnAction(_ -> {
            try {
                clientInterface.drawComponent();
            } catch (Exception e) {
                System.out.println("Problem in drawing component "+e);
                printError(e);
            }
        });
        rotateLeftButton.setOnAction(_ -> {
            if (selectedComponent != null) {
                selectedComponent.counterClockwiseRotation();
                updateSelectedComponent();
            }
        });
        rotateRightButton.setOnAction(_ -> {
            if (selectedComponent != null) {
                selectedComponent.clockwiseRotation();
                updateSelectedComponent();
            }
        });
        returnButton.setOnAction(_ -> {
            if(selectedComponent != null){
                try {
                    clientInterface.returnComponent(selectedComponent);
                    for (Iterator<Node> it = reservedBox.getChildren().iterator(); it.hasNext(); ) {
                        Node node = it.next();
                        if (node instanceof ImageView iv && iv.getUserData() == selectedComponent) {
                            it.remove();
                            break;
                        }
                    }
                    selectedComponent = null;
                }
                catch(Exception e){
                    printError(e);
                }
            }
        });
        placeOnTheBoard.setOnAction(_ -> {
            clientInterface.finalizeShip();
            drawButton.setDisable(true);
            rotateLeftButton.setDisable(true);
            rotateRightButton.setDisable(true);
            drawSmallDeck1.setDisable(true);
            drawSmallDeck2.setDisable(true);
            drawSmallDeck3.setDisable(true);
            placeOnTheBoard.setDisable(true);
            returnButton.setDisable(true);
            try {
                if (!clientInterface.isShipPlaced()) {
                    clientInterface.placeShip(selectedComponent);
                }
            } catch (Exception e) {
                printError(e);
            }
        });
        drawSmallDeck1.setOnAction(_ -> {
            try {
                clientInterface.requestSmallDeck(1);
            } catch (Exception e) {
                printError(e);
            }
        });
        drawSmallDeck2.setOnAction(_ -> {
            try {
                clientInterface.requestSmallDeck(2);
            } catch (Exception e) {
                printError(e);
            }
        });
        drawSmallDeck3.setOnAction(_ -> {
            try {
                clientInterface.requestSmallDeck(3);
            } catch (Exception e) {
                printError(e);
            }
        });
        turnHourglass.setOnAction(_ -> {
            try{
                if(clientInterface.canRestartTimer()) {
                    clientInterface.startTimer();
                    clientInterface.setCanRestartTimer(false);
                }
            } catch (Exception e) {
                printError(e);
            }
        });
    }

    /**
     * Setter of resumed
     */
    public void setResumed(boolean resumed){this.resumed = resumed;}

    /**
     * Setter of the current client interface
     * @param clientInterface current client interface
     */
    public void setClientInterface(ClientInterface clientInterface) {this.clientInterface = clientInterface;}

    /**
     * Method used to handle the component drawn
     * It displays it and allows the player to place it
     * @param component is the current component to handle
     */
    public void handleComponent(ShipComponent component) {
        try{
            ImageView view = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/"+component.getImage()))));
            selectedComponent = component;
            bookedComp05Selected = false;
            bookedComp06Selected = false;
            view.setFitWidth(110);
            view.setFitHeight(110);
            view.setUserData(component);
            view.setOnMouseClicked(_ ->{
                selectedComponent = (ShipComponent) view.getUserData();
                System.out.println("Component selected: " + selectedComponent);
                reservedBox.getChildren().forEach(node -> node.setStyle(""));
                view.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");
            });
            reservedBox.getChildren().add(view);
        }
        catch(Exception e){
            printError(e);
        }
    }

    /**
     * Method used to place the component on the ship grid or book it
     * @param r is the row coordinate where placing is desired
     * @param c  is the column coordinate where placing is desired
     */
    private void placeComponent(int r, int c) {
        if (selectedComponent != null) {
            try {
                if (r == 0 && (c == 5 || c == 6)) {
                    if (c == 5) {
                        if (comp06IsFirst) {
                            bookedComp05Index = 1;
                        }
                        else {
                            comp05IsFirst = true;
                            bookedComp05Index = 0;
                        }
                    }
                    else {
                        if (comp05IsFirst) {
                            bookedComp06Index = 1;
                        }
                        else {
                            comp06IsFirst = true;
                            bookedComp06Index = 0;
                        }
                    }
                    try {
                        clientInterface.bookComponent(selectedComponent);
                    }
                    catch (SlotTakenException | BookingSlotsFullException _) {
                    }
                }
                else {
                    clientInterface.addComponent(selectedComponent, c, r);
                }
                ShipComponent toBeAdded = selectedComponent;
                ship = clientInterface.getShip();
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/" + selectedComponent.getImage())));
                gridImages[r][c].setImage(image);
                gridImages[r][c].setRotate(-90 * (Orientation.getCounterClockwise90DegRotationFromOrientation(selectedComponent.getOrientation())));
                if (r != 0 || c != 5 && c != 6) {
                    gridImages[r][c].setOnMouseClicked(_ -> {});
                }
                //ImageView view = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/blackSquare.png"))));
                if (bookedComp05Selected) {
                    gridImages[0][5].setImage(null);
                    clientInterface.removeBookedComponent(bookedComp05Index);
                    if (comp05IsFirst && bookedComp06Index != -1) {
                        comp06IsFirst = true;
                        bookedComp06Index = 0;
                    }
                    bookedComp05Index = -1;
                    comp05IsFirst = false;
                    bookedComp05Selected = false;
                }
                else if (bookedComp06Selected) {
                    gridImages[0][6].setImage(null);
                    clientInterface.removeBookedComponent(bookedComp06Index);
                    if (comp06IsFirst && bookedComp05Index != -1) {
                        comp05IsFirst = true;
                        bookedComp05Index = 0;
                    }
                    bookedComp06Index = -1;
                    comp06IsFirst = false;
                    bookedComp06Selected = false;
                }
                else {
                    for (Iterator<Node> it = reservedBox.getChildren().iterator(); it.hasNext(); ) {
                        Node node = it.next();
                        if (node instanceof ImageView iv && iv.getUserData() == selectedComponent) {
                            it.remove();
                            break;
                        }
                    }
                }
                selectedComponent = null;
                clientInterface.addComponent(toBeAdded, c, r);
            }
            catch(IllegalPositionException | SlotTakenException | FloatingComponentException _){}
        }
    }

    /**
     * Method used to modify the rotation of selected component
     */
    private void updateSelectedComponent() {
        if (bookedComp05Selected) {
            ImageView iv = gridImages[0][5];
            int rotationDegrees = -90 * Orientation.getCounterClockwise90DegRotationFromOrientation(selectedComponent.getOrientation());
            iv.setRotate(rotationDegrees);
        }
        else if (bookedComp06Selected) {
            ImageView iv = gridImages[0][6];
            int rotationDegrees = -90 * Orientation.getCounterClockwise90DegRotationFromOrientation(selectedComponent.getOrientation());
            iv.setRotate(rotationDegrees);
        }
        else {
            for (Node node : reservedBox.getChildren()) {
                if (node instanceof ImageView iv) {
                    if (iv.getUserData() == selectedComponent) {
                        int rotationDegrees = -90 * Orientation.getCounterClockwise90DegRotationFromOrientation(selectedComponent.getOrientation());
                        iv.setRotate(rotationDegrees);
                        break;
                    }
                }
            }

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
