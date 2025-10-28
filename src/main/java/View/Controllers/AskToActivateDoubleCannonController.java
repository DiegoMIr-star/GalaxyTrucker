package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Projectiles.Projectile;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * Controller responsible for handling the interaction with the player related to the activation of double cannons,
 * in case of an incoming projectile
 */
public class AskToActivateDoubleCannonController {

    /**
     * Buttons necessary for GUI visualization, to receive responses from clients, in this case yes, no and ok
     */
    @FXML private Button yes, no, ok;

    /**
     * Label used to display a message to the client
     */
    @FXML private Label message;

    /**
     * Current client manager
     */
    private ClientInterface client;

    /**
     * Attribute used to store the current projectile to handle
     */
    private Projectile projectile;

    /**
     * Boolean array used to store:
     * it is true if the player desires to activate cannons
     */
    private boolean[] res;

    /**
     * Method that sets up the scene and handles the player's activation choice,
     * it also manages the scene and buttons accordingly to the choice
     * @param client is the current client who has to make the decision
     * @param projectile is the current projectile to handle
     * @param res is the choice of the client
     */
    public void init(ClientInterface client, Projectile projectile, boolean[] res) {
        this.client = client;
        this.projectile = projectile;
        this.res = res;

        message.setText("The " + projectile + " is directed right toward your ship, but it's not too late. \nYou have a double cannon ready to blow the asteroid to smithereens, but it will have to use one of your " + client.getShipBatteries() +" batteries. \n\nDo you want to take the opportunity? ");
        ok.setDisable(true);
        ok.setVisible(false);
        try {
            yes.setOnAction(_ -> {
                message.setText("You activated the double cannon and destroyed the " + projectile + ".");
                res[0] = true;
                yes.setVisible(false);
                no.setVisible(false);
                ok.setVisible(true);
                yes.setDisable(true);
                no.setDisable(true);
                ok.setDisable(false);
            });
            no.setOnAction(_ -> {
                message.setText("You decided to do nothing and watch the big asteroid crash into your ship.");
                res[0] = false;
                yes.setVisible(false);
                no.setVisible(false);
                ok.setVisible(true);
                yes.setDisable(true);
                no.setDisable(true);
                ok.setDisable(false);
            });
            ok.setOnAction(_ -> {
                Stage stage = (Stage) ok.getScene().getWindow();
                stage.close();
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
