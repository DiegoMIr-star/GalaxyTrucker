package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TUICardVisitorTest {

    private TUICardVisitor cardVisitor;
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
        cardVisitor = new TUICardVisitor();
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
        assertEquals(1,cardVisitor.visit(card1));
    }

    @Test
    void testVisit() {
        assertEquals(1,cardVisitor.visit(slavers));
    }

    @Test
    void testVisit1() {
        assertEquals(1,cardVisitor.visit(card2));
    }

    @Test
    void testVisit2() {
        assertEquals(0,cardVisitor.visit(card3));
    }

    @Test
    void testVisit3() {
        assertEquals(0,cardVisitor.visit(card4));
    }

    @Test
    void testVisit4() {
        assertEquals(0,cardVisitor.visit(card5));
    }

    @Test
    void testVisit5() {
        assertEquals(0,cardVisitor.visit(card6));
    }

    @Test
    void testVisit6() {
        assertEquals(0,cardVisitor.visit(card7));
    }

    @Test
    void testVisit7() {
        assertEquals(0,cardVisitor.visit(card8));
    }

    @Test
    void testVisit8() {
        assertEquals(0,cardVisitor.visit(combatZone));
    }

    @Test
    void testVisit9() {
        assertEquals(0,cardVisitor.visit(abandonedShip));
    }
}