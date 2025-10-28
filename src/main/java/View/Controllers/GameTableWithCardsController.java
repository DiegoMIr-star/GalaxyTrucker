package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import model.Cards.Card;
import model.DifferentShipComponents.Cabin;
import model.DifferentShipComponents.ComponentType;
import model.DifferentShipComponents.ShipComponent;
import model.Projectiles.Orientation;
import model.ShipDashboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller responsible for managing the view during the drawing a new card phase
 */
public class GameTableWithCardsController implements Initializable {

    /**
     * Button necessary for GUI visualization, to receive the request of drawing from a client
     */
    @FXML
    private Button drawCardButton;

    /**
     * Label for GUI visualization, it represents the player's nickname
     */
    @FXML
    private Label playersNickname;

    /**
     * Images for GUI visualization, in order they represent:
     * the drawn card,
     * the background of the scene,
     * the layer of the game board,
     * the ship,
     * the background layer for the card
     */
    @FXML
    private ImageView cardImage, background = new ImageView(), cardboardImage = new ImageView(), shipImage, cardBackground;

    /**
     * Tabs for GUI visualization: one for the game table and one for the ship
     */
    @FXML
    private Tab gameTableTab, shipTab;

    /**
     * Grid necessary to organize GUI visualization for the ship
     */
    @FXML
    private GridPane shipGrid;

    /**
     * Stack pane used to overlap different images
     */
    @FXML
    private StackPane stackPane = new StackPane();

    /**
     * Pane used to contain the main content of the GUI
     */
    @FXML
    private Pane rootPane;

    /**
     * Attribute used to store the ship components matrix
     */
    private ShipComponent[][] ship;

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
     * Current client manager
     */
    private ClientInterface clientInterface;

    /**
     * Attribute used to store the current card
     */
    private Card card;

    /**
     * Attribute used to store the current players
     */
    private ArrayList<ShipDashboard> players = new ArrayList<>();

    /**
     * Empty method that is required by the interface{@link Initializable}
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Method used to handle the draw a card phase
     * it displays the ship and sets the button to interact with the client
     * and simulate the action
     */
    @FXML
    public void onDrawCard() {
        ship = clientInterface.getShip();
        try {
            Image backgroundImage = new Image(String.valueOf(Objects.requireNonNull(getClass().getResource("/images/spaceBackground.jpg"))));
            background.setImage(backgroundImage);
            background.setFitHeight(700);
            background.setFitWidth(1000);
            Image cardBoardImage = new Image(String.valueOf(Objects.requireNonNull(getClass().getResource("/images/cardboard-5.png"))));
            cardboardImage.setFitHeight(500);
            cardboardImage.setFitWidth(900);
            cardboardImage.setImage(cardBoardImage);

            stackPane.getChildren().addAll(background, cardboardImage);
            rootPane.getChildren().add(stackPane);
            rootPane.setPrefSize(700, 1000);

            drawCardButton.setOnAction(_ -> {
                try {
                    clientInterface.drawCard();
                    drawCardButton.setDisable(true);
                } catch (Exception e) {
                    printProblem(e);
                }
            });
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/cardboard-1b.jpg")).toExternalForm()));
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    ImageView cell = new ImageView();
                    cell.setFitWidth(121);
                    cell.setFitHeight(121);
                    cell.setPickOnBounds(true);
                    shipGrid.add(cell, c, r);
                    gridImages[r][c] = cell;
                }
            }
            this.printShip();
            //System.out.println("Card drawn!");
        }
        catch(Exception e){
            printProblem(e);
        }
    }

    /**
     * Method used to handle the update positions and turns phase
     * it calculates the new positions and displays them on the ellipse
     * each player has a different color, based on the color of the central cabin of the ship
     * @param players is the arraylist of ship dashboard
     */
    public void updatePositionsAndTurns(ArrayList<ShipDashboard> players) {
        this.players = players;
        double centerX = 495;
        double centerY = 345;
        double radiusX = 340;
        double radiusY = 175;

        Ellipse ellipse = new Ellipse(centerX, centerY,radiusX, radiusY);
        ellipse.setStroke(null);
        ellipse.setFill(null);

        int pathLength = 24;
        Group positionsGroup = new Group();
        for (ShipDashboard ship : players) {
            int position = ship.getPosition() % pathLength;
            Color color = Color.WHITE;
            if (ship.getShip()[2][3].getType() != ComponentType.UNAVAILABLE_SLOT) {
                Cabin centralCabin = (Cabin) ship.getShip()[2][3];
                int cabinID = centralCabin.getID();

                if (cabinID+1 == 34) {
                    color = Color.GREEN;
                }
                else if (cabinID+1 == 33) {
                    color = Color.BLUE;
                }
                else if (cabinID+1 == 52) {
                    color = Color.RED;
                }
                else {
                    color = Color.YELLOW;
                }
            }


            double angle = 2 * Math.PI * position / pathLength;

            double x = centerX + radiusX * Math.cos(angle+Math.PI+Math.toRadians(20));
            double y = centerY + radiusY * Math.sin(angle+Math.PI+Math.toRadians(20));

            Circle circle = new Circle(x, y, 15, color);
            circle.setFill(color);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(2);

            positionsGroup.getChildren().add(circle);
        }

        //rootPane.setPrefSize(background.getFitWidth(), background.getFitHeight());
        rootPane.getChildren().addAll(ellipse, positionsGroup);
        //Group group = (Group) rootPane.getChildren().get(2);

    }

    /**
     * Method used to print the ship
     */
    private void printShip() {
        ShipComponent component;
        try {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    component = ship[r][c];
                    if (component != null && component.getImage() != null && !component.getImage().isEmpty()) {
                        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/" + component.getImage())));
                        gridImages[r][c].setImage(image);
                        gridImages[r][c].setRotate(-90 * (Orientation.getCounterClockwise90DegRotationFromOrientation(component.getOrientation())));
                    } else {
                        gridImages[r][c].setImage(null);
                    }
                }
            }
        }
        catch(Exception e){
            printProblem(e);
        }
    }

    /**
     * Setter of the current client interface
     * @param clientInterface current client interface
     */
    public void setClientInterface(ClientInterface clientInterface) { this.clientInterface = clientInterface; }

    /**
     * Method used to display a specific card
     * @param card is the card to display
     */
    public void showCard(Card card) {
        this.card = card;
        try {
            if (card != null && card.getImage() != null && !card.getImage().isEmpty()) {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/" + this.card.getImage())));
                cardImage.setFitHeight(cardImage.getFitHeight());
                cardImage.setFitHeight(cardImage.getFitHeight());
                cardImage.setImage(image);
            }
        }
        catch(Exception e){
            printProblem(e);
        }
    }

    /**
     * Method used to show the player's nickname
     * @param nickname is the nickname to display
     */
    public void showNickname(String nickname) {playersNickname.setText(nickname);}

    /**
     * Method used to handle the exceptions related to printing issues
     * @param e is the current exception
     */
    private void printProblem(Exception e){
        e.printStackTrace();
    }
}
