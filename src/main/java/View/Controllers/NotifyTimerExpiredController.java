package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling notifies clients related to the hourglasses updates on expiring
 */
public class NotifyTimerExpiredController implements Initializable {

    /**
     * Label for GUI visualization, to show messages to the client
     */
    @FXML
    private Label messageLabel;

    /**
     * Current client manager
     */
    private ClientInterface clientInterface;

    /**
     * Empty method that is required by the interface{@link Initializable}
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Method used to set text to notify clients on the state of hourglasses
     * in particular, if it's not the last hourglass, it offers the chance to start a new one
     * if it's the last, it communicates that the ship has been placed
     * @param isLast is a boolean that is true if the current hourglass is the last one
     */
    public void setText(boolean isLast){
        if(!isLast){
            messageLabel.setText("The timer has expired! You can now restart it!");
            messageLabel.setStyle("-fx-text-fill: black;");
        }
        else{
            messageLabel.setText("The LAST timer has expired! Your ship is being placed on the board...");
            try{
                clientInterface.sendShip();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * Setter of the current client interface
     * @param clientInterface current client interface
     */
    public void setInterface(ClientInterface clientInterface){
        this.clientInterface = clientInterface;
    }
}
