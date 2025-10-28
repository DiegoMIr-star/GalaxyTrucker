package Loader;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.Card;

import java.util.ArrayList;

/**
 * Class used in order to collect the different cards from json files
 */
public class ObjectsCard {

    /**
     * List of different cards loaded from json
     */
    private ArrayList<Card> cards;

    /**
     * Constructor used to load the cards from json file
     * @param cards loaded cards
     */
    @JsonCreator
    public ObjectsCard(
            @JsonProperty("cards") ArrayList<Card> cards
    ) {this.cards = cards;}

    /**
     * Getter of the list of the different cards loaded from json files
     * @return list of loaded cards
     */
    public ArrayList<Card> getCards(){ return cards;}

    /**
     * Setter of different cards
     * @param cards current list of cards
     */
    public void setCards(ArrayList<Card> cards) { this.cards = cards; }
}
