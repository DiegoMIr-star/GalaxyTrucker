package View.Controllers;

import Connections.ClientInterface;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Cards.Planets;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller responsible for handling the landing phase on planets
 */
public class PlanetsLandingController {

    /**
     * Labels necessary for GUI visualization, to communicate with clients
     */
    @FXML
    Label updateText, warningText;

    /**
     * Text field necessary for GUI visualization, to communicate with clients
     */
    @FXML
    TextField inputField;

    /**
     * Button necessary for GUI visualization, to receive yes or no and ok responses from client
     */
    @FXML
    Button yesButton, noButton, okButton;

    /**
     * Container used to show available planet indexes
     */
    @FXML
    HBox hBox = new HBox(20);

    /**
     * Attribute necessary to store free planet indexes
     */
    private ArrayList<Integer> freePlanetsIndexes;

    /**
     * Attribute necessary to display planets with adjusted indexes
     */
    private final ArrayList<Integer> printablePlanetsIndexes = new ArrayList<>();

    /**
     * Current client manager
     */
    private ClientInterface clientInterface;

    /**
     * Current Planets card considered
     */
    private Planets card;

    /**
     * Current planet chosen by the player
     * -3 means that the planet is not selected
     */
    private int planetIndexChosen = -3;

    /**
     * Method used to display the landing request for the player
     * based on player's choice, if he wants to land, it allows to select a planet {@link #askWhichPlanetWantsToLand()}
     * otherwise, if the answer is negative, it skips the landing
     * @param clientInterface is the client manager to allow to communicate with the player
     * @param freePlanetsIndexes is the list of indexes of free planets
     * @param card is the current Planets card
     */
    public void askIfPlayerWantsToLand(ClientInterface clientInterface, ArrayList<Integer> freePlanetsIndexes, Planets card) {
        this.clientInterface = clientInterface;
        this.freePlanetsIndexes = freePlanetsIndexes;
        this.card = card;

        if (freePlanetsIndexes.isEmpty()) {
            updateText.setText("Unfortunately all the planets have been already occupied." +
                    "\nMaybe the next time you will be luckier!");
            inputField.setDisable(true);
            inputField.setVisible(false);
            yesButton.setDisable(true);
            noButton.setDisable(true);
            yesButton.setVisible(false);
            noButton.setVisible(false);

            okButton.setDisable(false);
            okButton.setVisible(true);
            okButton.setOnAction(_ -> {
                okButton.setDisable(true);
                try {
                    clientInterface.landOnPlanet(-1);
                } catch (IOException e) {
                    printProblem(e);
                }
                Stage stage = (Stage) okButton.getScene().getWindow();
                stage.close();
            });
            return;
        }

        for (Integer freePlanetsIndex : freePlanetsIndexes) {
            printablePlanetsIndexes.add(freePlanetsIndex + 1);
        }

        updateText.setText("Now you have the opportunity to land on a planet!\n" +
                "Landing on a planet costs "+card.daysLost+" days.\n\n Do you want to land on a planet?");
        inputField.setDisable(true);
        inputField.setVisible(false);
        okButton.setDisable(true);
        okButton.setVisible(false);

        yesButton.setOnAction(_ -> {
            yesButton.setDisable(true);
            noButton.setDisable(true);
            yesButton.setVisible(false);
            noButton.setVisible(false);
            inputField.setDisable(false);
            inputField.setVisible(true);
            askWhichPlanetWantsToLand();
        });
        noButton.setOnAction(_ -> {
            yesButton.setDisable(true);
            noButton.setDisable(true);
            try {
                clientInterface.landOnPlanet(-1);
                Stage stage = (Stage) noButton.getScene().getWindow();
                stage.close();
            } catch (IOException e) {
                printProblem(e);
            }
        });
    }

    /**
     * Method to handle effective landing choice, displaying the proposal
     * Once the player chooses the index, if it's valid, it lets the player land on the desired planet
     */
    private void askWhichPlanetWantsToLand() {
        updateText.setText("You have chosen to land on a planet!\n\n" +
                "Please insert one of the following indexes to land on the corresponding planet!" +
                "\n\n!!! Planets are enumerated from top to bottom of the card.");
        try {
            hBox.setAlignment(Pos.CENTER);
            for (Integer printablePlanetsIndex : printablePlanetsIndexes) {
                hBox.getChildren().add(new Label(printablePlanetsIndex.toString()));
            }
            inputField.clear();
            inputField.setOnAction(_ -> {
                try {
                    planetIndexChosen = Integer.parseInt(inputField.getText());
                } catch (NumberFormatException e) {
                    warningText.setText("Please enter a valid planet index");
                    inputField.clear();
                }
                if (!printablePlanetsIndexes.contains(planetIndexChosen)) {
                    warningText.setText("The planet you have chosen doesn't exist!");
                    inputField.clear();
                }
                else if (planetIndexChosen > 0 && planetIndexChosen <= card.planetStocks.size() && !printablePlanetsIndexes.contains(planetIndexChosen)) {
                    warningText.setText("Another player has already landed on that planet.");
                    inputField.clear();
                }
                else {
                    try {
                        clientInterface.landOnPlanet(planetIndexChosen-1);
                    } catch (IOException e) {
                        printProblem(e);
                    }
                    Stage stage = (Stage) yesButton.getScene().getWindow();
                    stage.close();
                }
            });
        }
        catch(Exception e) {
            printProblem(e);
        }
    }

    /**
     * Setter of a message and disables all the input buttons and field
     * @param message is the current message to display
     */
    public void setText(String message) {
        warningText.setText(message);
        inputField.setDisable(true);
        inputField.setVisible(false);
        noButton.setDisable(true);
        yesButton.setDisable(true);
        noButton.setVisible(false);
        yesButton.setVisible(false);
    }

    /**
     * Method used to handle the exceptions related to printing issues
     * @param e is the current exception
     */
    private void printProblem(Exception e) {
        e.printStackTrace();
    }
}
