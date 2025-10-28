package View.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling the waiting room visualization before the start of the game
 */
public class WaitingRoomController implements Initializable {

    /**
     * Attribute used to store a gif used for the waiting room
     */
    @FXML
    private ImageView loadingGif;

    /**
     * Method that is required by the interface{@link Initializable}
     * It displays the gif
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Image gif = new Image(Objects.requireNonNull(getClass().getResource("/images/loading_icon.gif")).toExternalForm());
            loadingGif.setImage(gif);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
