package View.Controllers;

import Connections.ClientInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling the server connection in the GUI
 * In particular, it allows the player to choose between RMI and socket connection and initializes it
 */
public class ConnectionController implements Initializable {

    /**
     * Current client manager
     */
    private ClientInterface clientInterface;

    /**
     * Button necessary for GUI visualization, to choose socket connection
     */
    @FXML private RadioButton socketRadio;

    /**
     * Button necessary for GUI visualization, to choose RMI connection
     */
    @FXML private RadioButton rmiRadio;

    /**
     * Text field necessary for GUI, where the user writes the server IP address
     */
    @FXML private TextField ipField;

    /**
     * Text field necessary for GUI, where the user writes the server port number
     */
    @FXML private TextField portField;

    /**
     * Method that is required by the interface{@link Initializable}, used to set up the group of rmi and socket choice buttons
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup connectionGroup = new ToggleGroup();
        socketRadio.setToggleGroup(connectionGroup);
        rmiRadio.setToggleGroup(connectionGroup);
        socketRadio.setSelected(true);
    }

    /**
     * Method that handles the start of the connection:
     * it validates the IP address and port,
     * and if they are valid, initializes the desired connection
     */
    @FXML
    private void onConnect(){
        String IP_regex="^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}$";
        String ip;
        String portText;
        int port;

        ip = ipField.getText().trim();
        portText = portField.getText().trim();
        try {
            port = Integer.parseInt(portText);
        } catch (Exception e) {
            showAlert("Port must be an integer.");
            port=-1;
            printProblem(e);
        }
        try {
            if (!(ip.matches(IP_regex)) || port < 1024 || port >= 65536) {
                showAlert("Please insert valid IP and port.");
            }
        }
        catch (Exception e){
            printProblem(e);
        }

        try{
            if (socketRadio.isSelected()) {
                System.out.println("Socket connection with server " + ip + ":" + port);
                clientInterface.connect(ip,port,true);
            } else if (rmiRadio.isSelected()) {
                System.out.println("RMI connection with server " + ip + ":" + port);
                clientInterface.connect(ip,port,false);
            }
        }
        catch(Exception e){
            printProblem(e);
        }
    }

    /**
     * Method to show an error alert with the given message
     * @param message is the given message to display
     */
    private void showAlert(String message) {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            }
            catch (Exception e) {
                printProblem(e);
            }
        });
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
