package View.Controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for displaying a card for a limited time in the GUI
 */
public class CardDrownController implements Initializable {

    /**
     * Image of the card for GUI visualization
     */
    @FXML
    private ImageView imageView;

    /**
     * Empty method that is required by the interface{@link Initializable}
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Method used to display the card and after 3 seconds close it
     * @deprecated
     * @param image is the card image to show
     */
    public void setCardImage(Image image) {
        imageView.setImage(image);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(_ -> {
            Stage stage = (Stage) imageView.getScene().getWindow();
            stage.close();
        });
        delay.play();
    }
}
