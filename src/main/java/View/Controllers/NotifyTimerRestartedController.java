package View.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling notifies clients related to the hourglasses updates on restarting
 */
public class NotifyTimerRestartedController implements Initializable {

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
     * Method used to set text to notify clients on the state of hourglasses after restarting,
     * different messages are displayed in case of last hourglass or not last
     * @param isLast is a boolean that is true if the current hourglass is the last one
     */
    public void setText(boolean isLast){
        if(!isLast){
            messageLabel.setText("The timer has been restarted!");
            messageLabel.setStyle("-fx-text-fill: green;");
        }
        else{
            messageLabel.setText("The LAST timer has started! Time to hurry!");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
