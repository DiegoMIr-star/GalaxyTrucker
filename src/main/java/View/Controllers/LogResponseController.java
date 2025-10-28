package View.Controllers;

import Connections.Messages.LogResponseMessage;
import View.GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling login responses and displaying related messages
 */
public class LogResponseController implements Initializable {

    /**
     * Label for GUI visualization, to show messages to the client
     */
    @FXML
    private Label messageLabel;

    /**
     * Attribute that references to the GUI instance
     */
    private GUI gui;

    /**
     * Empty method that is required by the interface{@link Initializable}
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Method used to display messages during the login phase
     * @param message is the current message containing login information
     */
    public void showMessageFrom(LogResponseMessage message) {
        if (message.isConnected()) {
            messageLabel.setText("Connection established!");
        } else {
            messageLabel.setText("Connection lost...");
            System.exit(0);
        }
        if(!message.getNicknameStatus()){
            messageLabel.setText("The nickname you have selected is already used...");
            gui.askingNickname();
        }
    }

    /**
     * Setter of the GUI reference
     * @param gui to associate with this controller
     */
    public void setGUI(GUI gui){ this.gui = gui; }
}
