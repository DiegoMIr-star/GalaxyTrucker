package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.DifferentShipComponents.CargoHold;
import model.DifferentShipComponents.ComponentType;
import model.DifferentShipComponents.ShipComponent;
import model.Projectiles.Orientation;
import model.Stocks;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.Objects;

/**
 * Controller responsible for handling the add and rearrange stocks phase
 */
public class AddAndRearrangeStocksController {

    /**
     * Image for GUI visualization, used as background
     */
    @FXML
    ImageView backgroundImage;

    /**
     * Grid necessary to organize GUI visualization of the ship
     */
    @FXML
    GridPane grid;

    /**
     * Button used to let the player choose to move stocks of the ship
     */
    @FXML
    Button moveStocksButton;

    /**
     * Button used to let the player choose to add stocks
     */
    @FXML
    Button addStocksButton;

    /**
     * Button used to let the player choose to discard stocks
     */
    @FXML
    Button discardStocksButton;

    /**
     * Button used to let the player choose to exit
     */
    @FXML
    Button escButton;

    /**
     * Container of stocks that the player can add on the ship
     */
    @FXML
    ScrollPane stocksToAddScrollPane;

    /**
     * Label necessary to communicate with clients
     */
    @FXML
    Label text;

    /**
     * An horizontal box that contains all the new stocks to add
     */
    @FXML
    HBox newStocksHBox = new HBox();

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
     * Final attribute used to save the matrix of images of components that represents the ship with the chance to add elements (stocks for example)
     */
    private final StackPane[][] stackPanes = new StackPane[ROWS][COLS];

    /**
     * Attribute used to store stocks
     */
    private Stocks stocks = new Stocks();

    /**
     * Final attribute used to save the matrix of images of components that represents the ship
     */
    private ShipComponent[][] ship = new ShipComponent[ROWS][COLS];

    /**
     * Attribute that represents stocks that needs to be added
     */
    private Rectangle selectedStockToAdd;

    /**
     * Attribute used to store the selected cargo hold
     */
    private CargoHold selectedCargoHold;

    /**
     * Attributes used to store the row and the column of the selected cargo hold
     */
    private int selectedCargoHoldRow, selectedCargoHoldCol;

    /**
     * Method used to handle the initialization of the scene:
     * it sets the background image, it displays the stocks to add,
     * lets the player decides among move the already placed containers, place a new container and discard the remaining new containers
     * after the player finished and clicks esc button, it closes the scene
     * @param clientInterface is the current client manager
     * @param stocks are the current stocks to handle
     * @param ship is the current ship considered
     */
    public void setGridAndBackground(ClientInterface clientInterface, Stocks stocks, ShipComponent[][] ship) {
        try {
            backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/cardboard-1b.jpg")).toExternalForm()));
            stocksToAddScrollPane.setContent(newStocksHBox);
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    ImageView cell = new ImageView();
                    cell.setFitWidth(121);
                    cell.setFitHeight(121);
                    cell.setPickOnBounds(true);
                    StackPane stackPane = new StackPane(cell);
                    cell.setOnMouseClicked(_ -> {
                    });
                    HBox hBox = new HBox(5);
                    hBox.setAlignment(Pos.CENTER);
                    stackPane.getChildren().add(hBox);
                    grid.add(stackPane, c, r);
                    stackPanes[r][c] = stackPane;
                    gridImages[r][c] = cell;
                }
            }

            this.clientInterface = clientInterface;
            this.stocks = stocks;
            this.ship = ship;

            text.setText("You received the following containers. \n" +
                    "What would you like to do now?\n\n" +
                    "- Move the containers you have already placed\n" +
                    "- Place one of the new containers\n" +
                    "- Give up the remaining new containers");

            setStocksInScrollPane();
            printShipWithStocks();
            stocksToAddScrollPane.setDisable(true);
            escButton.setDisable(true);

            moveStocksButton.setOnAction(_ -> moveStocks());
            addStocksButton.setOnAction(_ -> addStocks());
            discardStocksButton.setOnAction(_ -> {
                try {
                    clientInterface.sendStocksToDiscard(this.stocks);
                    escButton.setDisable(false);
                    newStocksHBox.getChildren().clear();
                    text.setText("You discarded the remaining containers.");
                    moveStocksButton.setDisable(true);
                    addStocksButton.setDisable(true);
                    discardStocksButton.setDisable(true);
                    escButton.setText("OK");
                    escButton.setOnAction(_ -> {
                        try {
                            clientInterface.sendUpdatedShip();
                            Stage stage = (Stage) discardStocksButton.getScene().getWindow();
                            stage.close();
                        } catch (IOException ex) {
                            printProblem(ex);
                        }
                    });
                } catch (IOException ex) {
                    printProblem(ex);
                }
            });
            escButton.setOnAction(_ -> {
                if (newStocksHBox.getChildren().isEmpty()) {
                    try {
                        clientInterface.sendStocksToDiscard(this.stocks);
                        clientInterface.sendUpdatedShip();
                        Stage stage = (Stage) escButton.getScene().getWindow();
                        stage.close();
                        return;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                escButton.setDisable(true);
                addStocksButton.setDisable(false);
                moveStocksButton.setDisable(false);
                discardStocksButton.setDisable(false);
            });
        }catch (Exception e) {
            printProblem(e);
        }
    }

    /**
     * Method used to add stocks in scroll pane, represented as rectangles with specific colors
     */
    private void setStocksInScrollPane() {
        try{
            for (int i = 0; i < stocks.getBlueStocks(); i++) {
                Rectangle stock = new Rectangle(30, 30);
                stock.setFill(javafx.scene.paint.Color.BLUE);
                newStocksHBox.getChildren().add(stock);
            }
            for (int i = 0; i < stocks.getGreenStocks(); i++) {
                Rectangle stock = new Rectangle(30, 30);
                stock.setFill(javafx.scene.paint.Color.GREEN);
                newStocksHBox.getChildren().add(stock);
            }
            for (int i = 0; i < stocks.getYellowStocks(); i++) {
                Rectangle stock = new Rectangle(30, 30);
                stock.setFill(javafx.scene.paint.Color.YELLOW);
                newStocksHBox.getChildren().add(stock);
            }
            for (int i = 0; i < stocks.getSpecialRedStocks(); i++) {
                Rectangle stock = new Rectangle(30, 30);
                stock.setFill(javafx.scene.paint.Color.RED);
                newStocksHBox.getChildren().add(stock);
            }
        }
        catch(Exception e){
            printProblem(e);
        }
    }

    /**
     * Method used to display the stocks on the allowed ship cell
     * @param cargoHold is the current container of the stocks
     * @param r is the row coordinate
     * @param c is the column coordinate
     */
    private void showStocksOnTheShipCell(CargoHold cargoHold, int r, int c) {
        Stocks stocks = cargoHold.getStocks();
        try{
            HBox hBox = (HBox) stackPanes[r][c].getChildren().get(1);

            for (int i = 0; i < stocks.getBlueStocks(); i++) {
                Rectangle stock = new Rectangle(30, 30);
                stock.setFill(javafx.scene.paint.Color.BLUE);
                hBox.getChildren().add(stock);
            }
            for (int i = 0; i < stocks.getGreenStocks(); i++) {
                Rectangle stock = new Rectangle(30, 30);
                stock.setFill(javafx.scene.paint.Color.GREEN);
                hBox.getChildren().add(stock);
            }
            for (int i = 0; i < stocks.getYellowStocks(); i++) {
                Rectangle stock = new Rectangle(30, 30);
                stock.setFill(javafx.scene.paint.Color.YELLOW);
                hBox.getChildren().add(stock);
            }
            for (int i = 0; i < stocks.getSpecialRedStocks(); i++) {
                Rectangle stock = new Rectangle(30, 30);
                stock.setFill(javafx.scene.paint.Color.RED);
                hBox.getChildren().add(stock);
            }
        }
        catch(Exception e){
            printProblem(e);
        }
    }

    /**
     * Method used to move stocks from a cell to another
     */
    private void moveStocks() {
        escButton.setDisable(false);
        moveStocksButton.setDisable(true);
        addStocksButton.setDisable(true);
        discardStocksButton.setDisable(true);
        newStocksHBox.setDisable(true);
        stocksToAddScrollPane.setDisable(true);

        try {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (r == 0 && c == 0) {
                        gridImages[r][c].setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/trash.png")).toExternalForm()));
                        gridImages[r][c].setFitHeight(100);
                        gridImages[r][c].setFitWidth(100);
                        stackPanes[r][c].setOnMouseClicked(mouseEvent -> {
                            if (selectedStockToAdd != null && selectedCargoHold != null) {
                                HBox hbox = (HBox) stackPanes[selectedCargoHoldRow][selectedCargoHoldCol].getChildren().get(1);
                                hbox.getChildren().remove(selectedStockToAdd);
                                clientInterface.addGarbage(1);
                                if (selectedCargoHold.getStocks().numberOfStocks() > 0) {
                                    if (selectedStockToAdd.getFill() == javafx.scene.paint.Color.RED) {
                                        if (selectedCargoHold.canContainSpecial()) {
                                            selectedCargoHold.addStocks(0, 0, 0, -1);
                                        }
                                    } else {
                                        if (selectedStockToAdd.getFill() == Color.BLUE) {
                                            selectedCargoHold.addStocks(-1, 0, 0, 0);
                                        }
                                        if (selectedStockToAdd.getFill() == javafx.scene.paint.Color.GREEN) {
                                            selectedCargoHold.addStocks(0, -1, 0, 0);
                                        }
                                        if (selectedStockToAdd.getFill() == javafx.scene.paint.Color.YELLOW) {
                                            selectedCargoHold.addStocks(0, 0, -1, 0);
                                        }
                                    }
                                }
                                selectedStockToAdd = null;
                                selectedCargoHold = null;
                                selectedCargoHoldRow = -1;
                                selectedCargoHoldCol = -1;
                                moveStocks();
                            }
                        });
                    }
                    HBox hBox = (HBox) stackPanes[r][c].getChildren().get(1);
                    for (Node node : hBox.getChildren()) {
                        Rectangle stock = (Rectangle) node;
                        int finalR = r, finalC = c;
                        stock.setOnMouseClicked(_ -> {
                            if (selectedStockToAdd == null && selectedCargoHold == null) {
                                stock.setStroke(Color.WHITE);
                                stock.setStrokeWidth(3);
                                selectedStockToAdd = stock;
                                selectedCargoHold = (CargoHold) clientInterface.ship.getShip()[finalR][finalC];
                                selectedCargoHoldRow = finalR;
                                selectedCargoHoldCol = finalC;
                                ShipComponent component;
                                try {
                                    for (int r1 = 0; r1 < ROWS; r1++) {
                                        for (int c1 = 0; c1 < COLS; c1++) {
                                            component = clientInterface.ship.getShip()[r1][c1];
                                            int finalR1 = r1, finalC1 = c1;
                                            if (component.isType(ComponentType.CARGO_HOLD) && component != selectedCargoHold) {
                                                HBox hBox1 = (HBox) stackPanes[finalR1][finalC1].getChildren().get(1);
                                                for (Node node1 : hBox1.getChildren()) {
                                                    Rectangle stock1 = (Rectangle) node1;
                                                    stock1.setOnMouseClicked(null);
                                                }
                                                ShipComponent finalComponent1 = component;
                                                stackPanes[r1][c1].setOnMouseClicked(_ -> {
                                                    if (selectedStockToAdd != null && checkIfSuitableMoveStocks((CargoHold) finalComponent1)) {
                                                        selectedStockToAdd.setStroke(javafx.scene.paint.Color.BLACK);
                                                        selectedStockToAdd.setStrokeWidth(2);
                                                        HBox hbox = (HBox) stackPanes[finalR][finalC].getChildren().get(1);
                                                        try {
                                                            if(!hBox1.getChildren().contains(selectedStockToAdd)){
                                                                hBox1.getChildren().add(selectedStockToAdd);
                                                                hbox.getChildren().remove(selectedStockToAdd);
                                                            }
                                                        } catch (Exception e) {
                                                            printProblem(e);
                                                        }
                                                        selectedStockToAdd = null;
                                                        selectedCargoHold = null;
                                                        selectedCargoHoldRow = -1;
                                                        selectedCargoHoldCol = -1;
                                                        moveStocks();
                                                    }
                                                });
                                            }
                                        }
                                    }
                                } catch (Exception ex) {
                                    printProblem(ex);
                                }
                            }
                        });
                    }
                }
            }
        } catch (Exception ex) {
            printProblem(ex);
        }
    }

    /**
     * Method used to add new stocks on a ship cell
     * it also handles all the checks related to the positioning of the stocks
     */
    private void addStocks() {
        escButton.setDisable(false);
        moveStocksButton.setDisable(true);
        addStocksButton.setDisable(true);
        discardStocksButton.setDisable(true);
        newStocksHBox.setDisable(false);
        stocksToAddScrollPane.setDisable(false);
        try {
            for (Node node : newStocksHBox.getChildren()) {
                Rectangle stock = (Rectangle) node;
                stock.setOnMouseClicked(_ -> {
                    stock.setStroke(Color.BROWN);
                    stock.setStrokeWidth(3);
                    selectedStockToAdd = stock;
                    ShipComponent component;
                    try {
                        for (int r = 0; r < ROWS; r++) {
                            for (int c = 0; c < COLS; c++) {
                                if (r == 0 && c == 0) {
                                    stackPanes[r][c].setOnMouseClicked(null);
                                }
                                component = clientInterface.ship.getShip()[r][c];
                                int finalR = r, finalC = c;
                                if (component.isType(ComponentType.CARGO_HOLD)) {
                                    ShipComponent finalComponent = component;
                                    stackPanes[r][c].setOnMouseClicked(_ -> {
                                        if (checkIfSuitable((CargoHold) finalComponent)) {
                                            stock.setStroke(javafx.scene.paint.Color.BLACK);
                                            stock.setStrokeWidth(2);
                                            HBox hBox = (HBox) stackPanes[finalR][finalC].getChildren().get(1);
                                            try {
                                                if (!hBox.getChildren().contains(selectedStockToAdd)) {
                                                    hBox.getChildren().add(selectedStockToAdd);
                                                    newStocksHBox.getChildren().remove(selectedStockToAdd);
                                                }
                                            } catch (Exception ex) {
                                                printProblem(ex);
                                            }
                                            stackPanes[finalR][finalC].setOnMouseClicked(null);
                                            selectedStockToAdd = null;
                                            checkIfFinishedStocksToAdd();
                                        }
                                    });
                                }
                            }
                        }
                    } catch (Exception ex) {
                        printProblem(ex);
                    }
                });
            }
        } catch (Exception ex) {
            printProblem(ex);
        }
    }

    /**
     * Method used in {@link #addStocks()} to check if the stocks to add are finished or not,
     * in positive case this method enable esc button and disables all the other buttons
     */
    private void checkIfFinishedStocksToAdd() {
        if (newStocksHBox.getChildren().isEmpty()) {
            text.setText("You have added all the stocks to your ship!");
            addStocksButton.setDisable(true);
            moveStocksButton.setDisable(true);
            discardStocksButton.setDisable(true);
            stocksToAddScrollPane.setDisable(true);
            escButton.setDisable(false);
            escButton.setText("OK");
        }
    }

    /**
     * Method used to verify if the selected stocks can be moved to a specific cargo hold
     * if the check is positive, this method updates the state of the game
     * @param cargoHold is the current cargo hold to check
     * @return a boolean that is true if the check is positive, false otherwise
     */
    private boolean checkIfSuitableMoveStocks(CargoHold cargoHold) {
        try {
            if (cargoHold.getStocks().numberOfStocks() < cargoHold.getMaxCapacity()) {
                if (selectedStockToAdd.getFill() == javafx.scene.paint.Color.RED) {
                    if (cargoHold.canContainSpecial()) {
                        cargoHold.addStocks(0, 0, 0, 1);
                        selectedCargoHold.addStocks(0, 0, 0, -1);
                        return true;
                    }
                } else {
                    if (selectedStockToAdd.getFill() == Color.BLUE) {
                        cargoHold.addStocks(1, 0, 0, 0);
                        selectedCargoHold.addStocks(-1, 0, 0, 0);
                        return true;
                    }
                    if (selectedStockToAdd.getFill() == javafx.scene.paint.Color.GREEN) {
                        cargoHold.addStocks(0, 1, 0, 0);
                        selectedCargoHold.addStocks(0, -1, 0, 0);
                        return true;
                    }
                    if (selectedStockToAdd.getFill() == javafx.scene.paint.Color.YELLOW) {
                        cargoHold.addStocks(0, 0, 1, 0);
                        selectedCargoHold.addStocks(0, 0, -1, 0);
                        return true;
                    }
                }
            }
        }catch (Exception e) {
            printProblem(e);
        }
        return false;
    }

    /**
     * Method used to verify if the new selected stocks can be added to the specific cargo hold
     * if the check is positive, this method updates the state of the game
     * @param cargoHold is the current cargo hold to check
     * @return a boolean that is true if the check is positive, false otherwise
     */
    private boolean checkIfSuitable(CargoHold cargoHold) {
        try {
            if (cargoHold.getStocks().numberOfStocks() < cargoHold.getMaxCapacity()) {
                if (selectedStockToAdd.getFill() == javafx.scene.paint.Color.RED) {
                    if (cargoHold.canContainSpecial() && stocks.getSpecialRedStocks() > 0) {
                        cargoHold.addStocks(0, 0, 0, 1);
                        stocks.remove(1, 0, 0, 0);
                        return true;
                    }
                } else {
                    if (selectedStockToAdd.getFill() == Color.BLUE && stocks.getBlueStocks() > 0) {
                        cargoHold.addStocks(1, 0, 0, 0);
                        stocks.remove(0, 0, 0, 1);
                        return true;
                    }
                    if (selectedStockToAdd.getFill() == javafx.scene.paint.Color.GREEN && stocks.getGreenStocks() > 0) {
                        cargoHold.addStocks(0, 1, 0, 0);
                        stocks.remove(0, 0, 1, 0);
                        return true;
                    }
                    if (selectedStockToAdd.getFill() == javafx.scene.paint.Color.YELLOW && stocks.getYellowStocks() > 0) {
                        cargoHold.addStocks(0, 0, 1, 0);
                        stocks.remove(0, 1, 0, 0);
                        return true;
                    }
                }
            }
        }catch (Exception e) {
            printProblem(e);
        }
        return false;
    }

    /**
     * Method used to display the ship with stocks placed on it
     */
    private void printShipWithStocks() {
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
                    if (component.isType(ComponentType.CARGO_HOLD)) {
                        showStocksOnTheShipCell((CargoHold) component, r, c);
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
