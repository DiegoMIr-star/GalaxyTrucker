package View.Controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling the turn messages visualization
 */
public class TurnController implements Initializable {

    /**
     * Label for GUI visualization, to show turn messages to the client
     */
    @FXML
    private Label turnLabel;

    /**
     * Method that is required by the interface{@link Initializable}
     * After three seconds it closes the window
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(_ -> {
            Stage stage = (Stage) turnLabel.getScene().getWindow();
            stage.close();
        });
        pause.play();
    }
}
