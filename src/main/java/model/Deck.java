package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import model.Cards.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * The class is the representation of the deck
 * @see Game
 */
public class Deck implements Serializable {

    /**
     * List of cards present in a deck
     */
    private final ArrayList<Card> cards;

    /**
     * Number of cards of a possible level
     */
    private int nLevel1, nLevel2;

    /**
     * Used to signal the use of a deck
     */
    private boolean beingUsed;

    /**
     * Constructor used to create the total deck,
     * containing all the possible cards of the game
     * both of level 1 and level 2
     * @param cards list of cards to add into the deck
     * @param nLevel2 number of level 2 cards
     * @param nLevel1 number of level 1 cards
     */
    public Deck(@JsonProperty("cards") ArrayList<Card> cards,@JsonProperty("nLevel2") int nLevel2,
                    @JsonProperty("nLevel1") int nLevel1 ) {
        this.cards = Objects.requireNonNullElseGet(cards, ArrayList::new);

        this.nLevel1 = nLevel1;
        this.nLevel2 = nLevel2;
        this.beingUsed = false;
    }

    /**
     * Constructor used to initialize the small decks
     * @param nLevel2 number of level 2 cards
     * @param nLevel1 number of level 1 cards
     */
    public Deck(int nLevel2, int nLevel1 ) {
        this.cards = new ArrayList<>();
        this.nLevel1 = nLevel1;
        this.nLevel2 = nLevel2;
        this.beingUsed = false;
    }

    /**
     * Getter of the number of level 1 cards
     * @return amount of cards
     */
    public int getNumLvl1() { return nLevel1; }

    /**
     * Getter of the number of level 2 cards
     * @return amount of cards
     */
    public int getNumLvl2() { return nLevel2; }

    /**
     * It returns the size of the deck
     * @return size of the deck
     */
    public int getSize(){return cards.size();}

    /**
     * Method which adds a card into the deck
     * @param card card to add
     */
    public void addCardToDeck(Card card) {
        cards.add(card);
    }

    /**
     * Method used to get a smaller and randomized deck
     * @param stack amount of cards
     * @return list of cards as new deck
     */
    public ArrayList<Card> peekCards(int stack) {
        //exception size of the parameter
        if (stack > cards.size() || stack<1) {
            throw new IllegalArgumentException("Not enough cards in the original deck or <stack> too small.");
        }
        //peek is the array that I want to return
        ArrayList<Card> peek = new ArrayList<>();
        //shuffle of the deck
        Collections.shuffle(cards);
        //add in peek and remove from deck
        for(int i = 0; i < stack; i++) {
            peek.add(cards.removeFirst());
        }
        return peek;
    }

    //method to get a single card from the deck

    /**
     * Method used to get a random card of the deck
     * @return an object card
     */
    public Card drawCard() {
        //exception case size=0
        if (cards.isEmpty()) {
            return null;
        }
        //shuffle to ensure to obtain a random card
        //Collections.shuffle(cards); for debug purposes
        if (cards.getFirst().lvl == 1) {
            nLevel1--;
        }
        else if (cards.getFirst().lvl == 2) {
            nLevel2--;
        }
        return cards.removeFirst();
    }

    /**
     * Method used to get a specific card from the deck
     * @param i place of the card in the deck
     * @return specific card
     */
    public Card getCard(int i) {
        if (cards.get(i).lvl == 1) {
            nLevel1--;
        }
        else if (cards.get(i).lvl == 2) {
            nLevel2--;
        }
        return cards.remove(i);
    }

    /**
     * Getter of the cards
     * @return list of cards of the deck
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Setter used to signal if the deck is used or not
     * @param used boolean signaling the use of the deck
     */
    public void setCurrState(boolean used) { beingUsed = used;}

    /**
     * Method used to understand if the deck is used or not
     * @return boolean signaling the use of the deck
     */
    public boolean isUsed() { return beingUsed;}
}