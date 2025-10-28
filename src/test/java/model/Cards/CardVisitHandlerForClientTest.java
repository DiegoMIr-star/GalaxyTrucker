package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardVisitHandlerForClientTest {

    private CardVisitHandlerForClient visitor;
    private CombatZone combatZone;
    private Slavers slavers;
    private AbandonedShip abandonedShip;
    private Pirates card1;
    private Smugglers card2;
    private AbandonedStation card3;
    private Epidemic card4;
    private MeteorSwarm card5;
    private OpenSpace card6;
    private Planets card7;
    private Stardust card8;

    @BeforeEach
    void setUp() {
        visitor = new CardVisitHandlerForClient();
        combatZone = new CombatZone(1,5,5,5,null,"");
        slavers = new Slavers(1,1,4,4,4,"");
        abandonedShip = new AbandonedShip(5,5,5,1,"");
        card1 = new Pirates(1,1,1,1,new ArrayList<>(),"");
        card2 = new Smugglers(null,1,1,1,1,"");
        card3 = new AbandonedStation(1,1,null,1,"");
        card4 = new Epidemic(1,"");
        card5 = new MeteorSwarm(1,new ArrayList<>(),"");
        card6 = new OpenSpace(1,"");
        card7 = new Planets(1,1,new ArrayList<>(),"");
        card8 = new Stardust(1,"");
    }

    @Test
    void visit() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card1));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }

    @Test
    void testVisit() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card2));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }

    @Test
    void testVisit1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card3));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }

    @Test
    void testVisit2() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card4));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }

    @Test
    void testVisit3() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card5));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }

    @Test
    void testVisit4() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card6));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }

    @Test
    void testVisit5() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card7));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }

    @Test
    void testVisit6() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitor.visit(card8));
        assertEquals("Unexpected card, the card was not one containing a crew loss event",exception.getMessage());
    }

    @Test
    void testVisit7() {
        assertEquals(5,visitor.visit(combatZone));
    }

    @Test
    void testVisit8() {
        assertEquals(4,visitor.visit(slavers));
    }

    @Test
    void testVisit9() {
        assertEquals(5,visitor.visit(abandonedShip));
    }
}