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
 * Controller responsible for handling the state messages visualization
 */
public class StateController implements Initializable{

    /**
     * Label for GUI visualization, to show messages to the client
     */
    @FXML
    private Label messageLabel;

    /**
     * Empty method that is required by the interface{@link Initializable}
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Setter that is used to show the specific text for 3 seconds
     * @param text is the current text to display
     */
    public void setText(String text) {
        messageLabel.setText(text);

        try {
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(_ -> {
                Stage stage = (Stage) messageLabel.getScene().getWindow();
                stage.close();
            });
            delay.play();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
