package View.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;

/**
 * Controller responsible for handling the initial scene of the game
 */
public class BeginController implements Initializable {

    /**
     * Main label for GUI visualization
     */
    @FXML private Label mainLabel;

    /**
     * Label necessary for GUI visualization, to display the number of players in the game
     */
    @FXML private Label playersCountLabel;

    /**
     * Container for GUI visualization, to display the list of nicknames
     */
    @FXML private VBox namesBox;

    /**
     * Empty method that is required by the interface{@link Initializable}
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Method used to set up the start of the game and displays connected players information:
     * it shows the number of players and their nicknames
     * @param playersNickname is the list of current nicknames
     */
    public void setupPlayers(ArrayList<String> playersNickname) {
        try {
            mainLabel.setText("The game can begin!");
            playersCountLabel.setText("There are " + playersNickname.size() + " players in this game!");

            for (String name : playersNickname) {
                Label nameLabel = new Label("- " + name);
                nameLabel.setStyle("-fx-font-size: 14px;");
                namesBox.getChildren().add(nameLabel);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
