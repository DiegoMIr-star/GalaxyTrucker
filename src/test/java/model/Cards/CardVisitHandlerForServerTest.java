package model.Cards;

import model.Cards.Enemies.Smugglers;
import model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardVisitHandlerForServerTest {

    private CardVisitHandlerForServer visitor;
    private Smugglers card2;
    private AbandonedStation card3;
    private Epidemic card4;
    private MeteorSwarm card5;
    private OpenSpace card6;
    private Planets card7;

    @BeforeEach
    void setUp() {
        visitor = new CardVisitHandlerForServer(new Game(null,1,null,null),null);
        card2 = new Smugglers(null,1,1,1,1,"");
        card3 = new AbandonedStation(1,1,null,1,"");
        card4 = new Epidemic(1,"");
        card5 = new MeteorSwarm(1,new ArrayList<>(),"");
        card6 = new OpenSpace(1,"");
        card7 = new Planets(1,1,new ArrayList<>(),"");
    }

    @Test
    void visit() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card7));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }

    @Test
    void testVisit() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card6));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }

    @Test
    void testVisit1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card5));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }

    @Test
    void testVisit2() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card4));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }

    @Test
    void testVisit3() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card3));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }

    @Test
    void testVisit4() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card2));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }
}