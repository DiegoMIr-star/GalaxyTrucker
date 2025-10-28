package View.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Deck;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling the visualization of the small deck
 */
public class SmallDeckController implements Initializable {

    /**
     * Attributes used to store 3 images that represent 3 card of the small deck
     */
    public ImageView card3;
    public ImageView card2;
    public ImageView card1;

    /**
     * Empty method that is required by the interface{@link Initializable}
     * @param url is the location necessary to resolve relative paths for the root object
     * @param resourceBundle is the resource used to find the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Method that is used to display the 3 images of the small deck
     * @param deck is the current deck to display
     */
    public void setDeck(Deck deck) {
        try {
            card1.setImage(new Image("cards/" + deck.getCards().get(0).getImage()));
            card2.setImage(new Image("cards/" + deck.getCards().get(1).getImage()));
            card3.setImage(new Image("cards/" + deck.getCards().get(2).getImage()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
