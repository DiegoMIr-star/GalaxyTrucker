package View.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for managing the view of generic messages
 */
public class GenericMessageController implements Initializable {

    /**
     * Label for GUI visualization, it represents the message
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
     * Method used to show a specific message
     * @param message is the message to display
     */
    public void showMessage(String message) {
        messageLabel.setText(message);
    }
}
