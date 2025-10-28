package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling the first player connection in the GUI
 * In particular, it allows the first player to choose the number of players
 */
public class FirstPlayerController implements Initializable {

    /**
     * Current client manager
     */
    private ClientInterface clientInterface;

    /**
     * Buttons necessary for GUI visualization, to receive the response from client about the choice of number of players
     * they can be 2, 3 or 4 players
     */
    @FXML private RadioButton radio2;
    @FXML private RadioButton radio3;
    @FXML private RadioButton radio4;

    /**
     * Group necessary for GUI to ensure mutual exclusivity of the choice of number of players
     */
    private ToggleGroup group;

    /**
     * Method that is required by the interface{@link Initializable}, used to set up the number of players choice buttons
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        group = new ToggleGroup();
        radio2.setToggleGroup(group);
        radio3.setToggleGroup(group);
        radio4.setToggleGroup(group);
        radio2.setSelected(true);
    }

    /**
     * Method linked with the choice of number of player
     * it checks the choice, and if it's valid, communicates it to the client manager
     */
    @FXML
    private void onConfirm(){
        RadioButton selected = (RadioButton) group.getSelectedToggle();
        int players = 0;

        if (selected == radio2) players = 2;
        else if (selected == radio3) players = 3;
        else if (selected == radio4) players = 4;

        if (players == 0) {
            showAlert();
        } else {
            System.out.println("Number of players confirmed: " + players);
            try{
                clientInterface.setPlayersNumber(players);
            }
            catch(Exception e){
                printProblem(e);
            }
        }
    }

    /**
     * Method that sends an alert to player in case of no valid number selected
     */
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please select the number of players.");
        try {
            alert.showAndWait().ifPresent(_ -> {
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.toFront();
            });
        }
        catch (Exception e) {
            printProblem(e);
        }
    }

    /**
     * Setter of the current client interface
     * @param clientInterface current client interface
     */
    public void setClientInterface(ClientInterface clientInterface) {
        this.clientInterface = clientInterface;
    }

    /**
     * Method used to handle the exceptions related to printing issues
     * @param e is the current exception
     */
    private void printProblem(Exception e){
        e.printStackTrace();
    }
}