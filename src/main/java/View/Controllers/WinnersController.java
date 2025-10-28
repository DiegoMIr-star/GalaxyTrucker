package View.Controllers;

import Connections.ClientInterface;
import View.GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ShipDashboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling the winners communication phase
 */
public class WinnersController implements Initializable {

    /**
     * Label for GUI visualization, to communicate the winner or the number of winners
     */
    @FXML
    private Label resultLabel;

    /**
     * Labels for GUI visualization, to communicate the winners
     */
    @FXML
    private Label winnerLabel;

    /**
     * Box for GUI visualization, that contains winners
     */
    @FXML
    private VBox winnersBox;

    /**
     * Labels for GUI visualization, to communicate current player's result
     */
    @FXML
    private Label personalResult;

    /**
     * Button for GUI visualization, to disconnect
     */
    @FXML
    private RadioButton disconnectRadio;

    /**
     * Button for GUI visualization, to join a new game
     */
    @FXML
    private RadioButton newGameRadio;

    /**
     * Group used to avoid that both disconnected button and new game button are selected
     */
    private final ToggleGroup toggleGroup = new ToggleGroup();

    /**
     * Current client manager
     */
    private ClientInterface clientInterface;

    /**
     * current reference to the gui
     */
    private GUI gui;

    /**
     * Method that is required by the interface{@link Initializable}
     * It also sets the toggle group for disconnected button and new game button
     * @param location is the location necessary to resolve relative paths for the root object
     * @param resources is the resource used to find the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disconnectRadio.setToggleGroup(toggleGroup);
        newGameRadio.setToggleGroup(toggleGroup);
        disconnectRadio.setSelected(true);
    }

    /**
     * Method that is used to display the game results, both general and personal(of the single player)
     * @param winners is the list of winners of the game
     * @param nickname is the nickname of the current player
     */
    public void setEndGameData(List<ShipDashboard> winners, String nickname) {
        boolean multiple = winners.size() > 1;

        resultLabel.setText(multiple ? "This game has multiple winners!" : "The winner is:");
        winnerLabel.setText(multiple ? "The winners are:" : "");

        try {
            winnersBox.getChildren().clear();
            for (int i = 0; i < winners.size(); i++) {
                Label label = new Label((i + 1) + ". " + winners.get(i).getNickname());
                label.setStyle("-fx-font-size: 16px; -fx-alignment: center;");
                winnersBox.getChildren().add(label);
            }
            ArrayList<String> winnersList = new ArrayList<>();
            for (ShipDashboard winner : winners) {
                winnersList.add(winner.getNickname());
            }
            personalResult.setText(winnersList.contains(nickname) ? "You have won!" : "You have lost.");
        }
        catch(Exception e) {
            printProblem(e);
        }
    }

    /**
     * Method used to handle the choice of toggle group:
     * it communicates the decision to the client manager
     */
    @FXML
    private void onConfirmAction(){
        try{
            if (disconnectRadio.isSelected()) {
                clientInterface.sendEndGame(1);
                Stage stage = (Stage) disconnectRadio.getScene().getWindow();
                stage.close();
            }
            if (newGameRadio.isSelected()) {
                clientInterface.sendEndGame(2);
                Stage stage = (Stage) newGameRadio.getScene().getWindow();
                stage.close();
                gui.askingNickname();
            }
        }
        catch(Exception e){
            printProblem(e);
        }
    }

    /**
     * Setter of the current client interface
     * @param clientInterface current client interface
     */
    public void setClientInterface(ClientInterface clientInterface, GUI gui) {
        this.clientInterface = clientInterface;
        this.gui = gui;
    }

    /**
     * Method used to handle the exceptions related to printing issues
     * @param e is the current exception
     */
    private void printProblem(Exception e){
        e.printStackTrace();
    }
}
