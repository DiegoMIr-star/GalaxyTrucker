package model;

import model.Cards.*;
import model.Cards.CardVisitorProgresser.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private static class TestCard extends Card {
        public TestCard(int lvl){
            super("Test", lvl, "");
        }

        @Override
        public void apply(CardVisitor visitor) {
        }

        @Override
        public int returner(CardVisitReturner visitor) {
            return 0;
        }

        @Override
        public NextGameStateAndMessages apply(CardVisProg_state visitor) {
            return null;
        }
    }
    @Test
    void testDrawADeckOfCards(){
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new TestCard(2));
        cards.add(new TestCard(2));
        cards.add(new TestCard(2));
        cards.add(new TestCard(2));
        cards.add(new TestCard(2));

        Deck deck= new Deck(cards, 5, 0);

        //test the creation of the deck
        assertEquals(5, deck.getSize());
        assertEquals(5, deck.getNumLvl2());
        assertEquals(0, deck.getNumLvl1());

        //creating a deck of 3 cards from the initial deck
        ArrayList<Card> drawnCards = deck.peekCards(3);
        assertEquals(3, drawnCards.size());
        assertEquals(2, deck.getSize());


    }

    @Test
    void drawACard(){
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new TestCard(2));
        cards.add(new TestCard(2));
        cards.add(new TestCard(2));
        cards.add(new TestCard(1));
        cards.add(new TestCard(1));
        cards.add(new TestCard(1));
        cards.add(new TestCard(1));

        Deck deck= new Deck(cards, 3, 4);

        assertEquals(7, deck.getSize());
        assertEquals(4, deck.getNumLvl1());
        assertEquals(3, deck.getNumLvl2());
        deck.drawCard();
        assertEquals(4, deck.getNumLvl1());
        assertEquals(2, deck.getNumLvl2());
        assertEquals(6, deck.getSize());
        deck.drawCard();
        assertEquals(4, deck.getNumLvl1());
        assertEquals(1, deck.getNumLvl2());
        assertEquals(5, deck.getSize());
        deck.drawCard();
        assertEquals(4, deck.getNumLvl1());
        assertEquals(0, deck.getNumLvl2());
        assertEquals(4, deck.getSize());
        deck.drawCard();
        assertEquals(3, deck.getNumLvl1());
        assertEquals(0, deck.getNumLvl2());
        assertEquals(3, deck.getSize());
        deck.drawCard();
        assertEquals(2, deck.getNumLvl1());
        assertEquals(0, deck.getNumLvl2());
        assertEquals(2, deck.getSize());
        deck.drawCard();
        assertEquals(1, deck.getNumLvl1());
        assertEquals(0, deck.getNumLvl2());
        assertEquals(1, deck.getSize());
        deck.drawCard();
        assertEquals(0, deck.getNumLvl1());
        assertEquals(0, deck.getNumLvl2());
        assertEquals(0, deck.getSize());
        assertNull(deck.drawCard());
    }

    @Test
    void invalidDeckActions(){
        Deck deck = new Deck(new ArrayList<>(), 0, 0);
        assertThrows(IllegalArgumentException.class, () -> deck.peekCards(2));
        assertNull(deck.drawCard());
    }

    @Test
    void removalOfSpecificLevelCard(){
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new TestCard(2));
        cards.add(new TestCard(2));
        cards.add(new TestCard(1));
        cards.add(new TestCard(1));
        Deck deck= new Deck(cards, 2, 2);

        //check the updates of a card removal
        Card drawnCard= deck.getCard(1);
        if(drawnCard.lvl==1){
            assertEquals(1, deck.getNumLvl1());
            assertEquals(2, deck.getNumLvl2());
        }
        else{
            assertEquals(2, deck.getNumLvl1());
            assertEquals(1, deck.getNumLvl2());
        }
        Card drawnCard1 = deck.getCard(2);
        assertEquals(1, deck.getNumLvl1());
        assertEquals(1, deck.getNumLvl2());

    }
}