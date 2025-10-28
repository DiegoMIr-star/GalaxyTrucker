package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling the nickname choice phase
 */
public class NicknameController implements Initializable {

    /**
     * Current client manager
     */
    private ClientInterface clientInterface;

    /**
     * Text field necessary for GUI visualization, to communicate with clients
     */
    @FXML
    private TextField nicknameField;

    /**
     * Method that is required by the interface{@link Initializable}
     * it also displays the request of insertion of the nickname
     * @param location is the location necessary to resolve relative paths for the root object
     * @param resources is the resource used to find the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nicknameField.setPromptText("Insert your nickname");
    }

    /**
     * Method used to confirm and set the chosen nickname, if it's not empty;
     * otherwise an error alert is shown asking for a valid nickname
     */
    @FXML
    private void onConfirmNickname(){
        String nickname;
        do{
            nickname = nicknameField.getText().trim();

            try {
                if (nickname.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("You have to select a nickname.");
                    alert.showAndWait().ifPresent(_ -> {
                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.toFront();
                    });
                } else {
                    System.out.println("Your nickname is: " + nickname);
                }
            }
            catch(Exception e){
                printProblem(e);
            }
        }
        while(nickname.isEmpty());
        try{
            clientInterface.setNickname(nickname);
        }
        catch(Exception e){
            printProblem(e);
        }
    }

    /**
     * Setter of the current client interface
     * @param clientInterface current client interface
     */
    public void setClientInterface(ClientInterface clientInterface) {
        this.clientInterface=clientInterface;
    }

    /**
     * Method used to handle the exceptions related to printing issues
     * @param e is the current exception
     */
    private void printProblem(Exception e){
        e.printStackTrace();
    }
}
