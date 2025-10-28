package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import model.Cards.Card;
import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling the reward claiming interaction
 */
public class ClaimRewardController implements Initializable {

    /**
     * Label necessary for GUI visualization, to communicate with client
     */
    @FXML
    private Label messageLabel;

    /**
     * Button necessary for GUI visualization, to accept
     */
    @FXML
    private RadioButton yesButton;

    /**
     * Button necessary for GUI visualization, to refuse
     */
    @FXML
    private RadioButton noButton;

    /**
     * Group used to ensure mutual exclusivity of yes and no buttons
     */
    @FXML
    private final ToggleGroup choiceGroup = new ToggleGroup();

    /**
     * Current client manager
     */
    private ClientInterface clientInterface;

    /**
     * Method that is required by the interface{@link Initializable}, used to set up the group of yes and no buttons
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        yesButton.setToggleGroup(choiceGroup);
        noButton.setToggleGroup(choiceGroup);
    }

    /**
     * Method that displays messages in the GUI based on the type of the current card
     * @param card is the current card that determines which message needs to be displayed
     */
    public void handleCard(Card card) {
        switch (card) {
            case Smugglers card1:
                messageLabel.setText("You can earn "+card1.stocks + "."+"\n"
                        +"At the same time you will lose "+card1.daysLoss+" days.");
                break;
            case Pirates card2:
                messageLabel.setText("You can earn "+card2.creditsGained+" credits, losing "+card2.daysLoss+" days.");
                break;
            case Slavers card3:
                messageLabel.setText("You can earn "+card3.creditsGained+" credits, losing "+card3.daysLoss+" days.");
                break;
            default:
                messageLabel.setText("Unexpected card...");
        }
    }

    /**
     * Method that handles the confirmation and informs the client manager about the player's decision
     */
    @FXML
    private void handleConfirm(){
        Toggle selected = choiceGroup.getSelectedToggle();
        try{
            if (selected == yesButton) clientInterface.claimReward(true);
            else if (selected == noButton) clientInterface.claimReward(false);}
        catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Setter of the current client interface
     * @param clientInterface current client interface
     */
    public void setClientInterface(ClientInterface clientInterface) {this.clientInterface = clientInterface;}
}
