package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Cards.AbandonedShip;
import model.Cards.AbandonedStation;
import model.Cards.Card;

/**
 * Controller responsible for handling docking requests
 */
public class AskDockingController {

    /**
     * Buttons necessary for GUI visualization, to receive responses from clients
     */
    @FXML
    Button yesButton, noButton, okButton;

    /**
     * Label necessary for GUI visualization, to communicate with clients
     */
    @FXML
    Label text;

    /**
     * Current client manager
     */
    private ClientInterface clientInterface;

    /**
     * Method used to activate the card functionalities based on the card considered, either Abandoned Ship or Abandoned Station
     * If the player has sufficient crew, it asks player to accept or not the card's offer
     * @param card is the specific card considered
     */
    public void startCard(Card card) {
        okButton.setDisable(true);
        okButton.setVisible(false);
        switch (card) {
            case AbandonedShip abandonedShip:
                text.setText("While admiring the emptiness of space from your cabin's glass\n" +
                        "walls, you spot what appears to be an abandoned spaceship drifting\n" +
                        "slowly. " + abandonedShip.crewLoss + " of the members of your crew offer to pay you\n" +
                        abandonedShip.creditsGained + " credits to let them go explore the cosmos with this newfound ship.\n" +
                        "Will you accept their offer?");
                break;
            case AbandonedStation abandonedStation:
                if(clientInterface.getShipCrew() < abandonedStation.requiredCrew) {
                    text.setText("While emptying the waste chute, your eye catches a weirdly shaped\nmoon in the distance. Once you get closer, you realize that it\nis actually a space station. " +
                            "Since it seems abandoned, you\nconsider docking it to look for any residual goods left behind.\n" +
                            "Doing so would make you lose " + abandonedStation.daysLoss + " days, but you'd gain\n" + abandonedStation.stocks + "." +
                            "\nUnfortunately you don't have a big enough crew to raid the station :(");
                    yesButton.setDisable(true);
                    yesButton.setVisible(false);
                    noButton.setDisable(true);
                    noButton.setVisible(false);

                    okButton.setDisable(false);
                    okButton.setVisible(true);
                    return;
                }
                //else
                text.setText("While emptying the waste chute, your eye catches a weirdly shaped\nmoon in the distance. Once you get closer, you realize that it\nis actually a space station. " +
                        "Since it seems abandoned, you\nconsider docking it to look for any residual goods left behind.\n" +
                        "Doing so would make you lose " + abandonedStation.daysLoss + " days, but you'd gain\n" + abandonedStation.stocks + "." +
                        "Will you accept their offer?");
            default:
                break;
        }
        yesButton.setOnAction(_ -> {
            try {
                yesButton.setDisable(true);
                noButton.setDisable(true);
                clientInterface.sendCardActivationRequest(true);
                Stage stage = (Stage) yesButton.getScene().getWindow();
                stage.close();
            } catch (Exception ex) {
                printError(ex);
            }
        });
        noButton.setOnAction(_ -> {
            try {
                yesButton.setDisable(true);
                noButton.setDisable(true);
                clientInterface.sendCardActivationRequest(false);
                Stage stage = (Stage) noButton.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                printError(e);
            }
        });
        okButton.setOnAction(_ -> {
            try {
                clientInterface.sendCardActivationRequest(false);
                okButton.setDisable(true);
                Stage stage = (Stage) okButton.getScene().getWindow();
                stage.close();
            }catch (Exception e) {
                printError(e);
            }
        });

    }

    /**
     * Setter of the client manager
     * @param clientInterface is the considered client
     */
    public void setClientInterface(ClientInterface clientInterface) {
        this.clientInterface = clientInterface;
    }

    /**
     * Method used to handle the exception related to printing issues
     * @param e is the current exception
     */
    private void printError(Exception e){
        e.printStackTrace();
    }
}
