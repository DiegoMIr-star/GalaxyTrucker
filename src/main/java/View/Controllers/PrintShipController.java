package View.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.DifferentShipComponents.ShipComponent;
import model.Projectiles.Orientation;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling the visualization of the ship
 */
public class PrintShipController implements Initializable {

    /**
     * Grid necessary to organize GUI visualization for the ship
     */
    @FXML
    private GridPane grid;

    /**
     * Final attribute used to save the integer that represents the number of rows
     */
    private final int ROWS = 5;

    /**
     * Final attribute used to save the integer that represents the number of columns
     */
    private final int COLS = 7;

    /**
     * Attribute used to save the matrix of images of components that represents the ship
     */
    private ImageView[][] showShip = new ImageView[0][0];

    /**
     * Method that is required by the interface{@link Initializable}
     * This method is also used to display an empty ship
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showShip = new ImageView[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                ImageView imageView = new ImageView();
                imageView.setFitWidth(60);
                imageView.setFitHeight(60);
                imageView.setPreserveRatio(true);
                showShip[r][c] = imageView;
                grid.add(imageView, c, r);
            }
        }
    }

    /**
     * Method that is used to complete the grid with the given ship components
     * @param ship is the matrix of ship components to display
     */
    public void setShip(ShipComponent[][] ship) {
        //ROWS = ship.length;
        //COLS = ship[ship.length-1].length;
        try {
            for (int r = 0; r < ship.length; r++) {
                for (int c = 0; c < ship[r].length; c++) {
                    ShipComponent component = ship[r][c];
                    if (component != null && component.getImage() != null && !component.getImage().isEmpty()) {
                        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/" + component.getImage())));
                        showShip[r][c].setImage(image);
                        showShip[r][c].setRotate(90 * (Orientation.getCounterClockwise90DegRotationFromOrientation(component.getOrientation())));
                    } else {
                        showShip[r][c].setImage(null);
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
