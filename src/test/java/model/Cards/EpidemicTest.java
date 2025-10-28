package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.ShipDashboard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpidemicTest {

    private static class TestShipDashboard extends ShipDashboard {
        boolean malusCrew= false;

        public TestShipDashboard() {
            super();
        }

        @Override
        public void removeConnectedCrew(){
            malusCrew = true;
        }
    }

    private static class TestShipDashboardSaved extends ShipDashboard {
        boolean malusCrew= false;

        public TestShipDashboardSaved() {
            super();
        }

        @Override
        public void removeConnectedCrew(){
            malusCrew = false;
        }
    }

    private static class TestVisit implements CardVisitor {
        private boolean applied = false;

        @Override
        public void visit(Stardust card) {}

        @Override
        public void visit(Planets card) {}

        @Override
        public void visit(OpenSpace card) {}

        @Override
        public void visit(MeteorSwarm card) {}

        @Override
        public void visit(Epidemic card) {
            applied = true;
        }

        @Override
        public void visit(CombatZone card) {}

        @Override
        public void visit(AbandonedShip card) {}

        @Override
        public void visit(AbandonedStation card) {}

        @Override
        public void visit(Smugglers card) {}

        @Override
        public void visit(Slavers card) {}

        @Override
        public void visit(Pirates card) {}
    }

    private static class TestReturner implements CardVisitReturner {
        @Override
        public int visit(Stardust card) {
            return 0;
        }

        @Override
        public int visit(Planets card) {
            return 0;
        }

        @Override
        public int visit(OpenSpace card) {
            return 0;
        }

        @Override
        public int visit(MeteorSwarm card) {
            return 0;
        }

        @Override
        public int visit(Epidemic card) {
            return 1;
        }

        @Override
        public int visit(CombatZone card) {
            return 0;
        }

        @Override
        public int visit(AbandonedShip card) {
            return 0;
        }

        @Override
        public int visit(AbandonedStation card) {
            return 0;
        }

        @Override
        public int visit(Smugglers card) {
            return 0;
        }

        @Override
        public int visit(Slavers card) {
            return 0;
        }

        @Override
        public int visit(Pirates card) {
            return 0;
        }

    }


    //case: all the players have connected crew
    @Test
    void applyToAllPlayers() {
        TestShipDashboard testShipDashboard1 = new TestShipDashboard();
        TestShipDashboard testShipDashboard2 = new TestShipDashboard();
        TestShipDashboard testShipDashboard3 = new TestShipDashboard();
        TestShipDashboard testShipDashboard4 = new TestShipDashboard();

        ArrayList<ShipDashboard> testShip = new ArrayList<>();
        testShip.add(testShipDashboard1);
        testShip.add(testShipDashboard2);
        testShip.add(testShipDashboard3);
        testShip.add(testShipDashboard4);

        Epidemic cardToTest = new Epidemic(3, "");
        cardToTest.apply(testShip);

        assertTrue(testShipDashboard1.malusCrew);
        assertTrue(testShipDashboard2.malusCrew);
        assertTrue(testShipDashboard3.malusCrew);
        assertTrue(testShipDashboard4.malusCrew);
    }

    //case: some players don't have connected crew
    @Test
    void applyNotAllPlayersAffected() {
        TestShipDashboard testShipDashboard1 = new TestShipDashboard();
        TestShipDashboardSaved testShipDashboard2 = new TestShipDashboardSaved();
        TestShipDashboardSaved testShipDashboard3 = new TestShipDashboardSaved();
        TestShipDashboard testShipDashboard4 = new TestShipDashboard();

        ArrayList<ShipDashboard> testShip1 = new ArrayList<>();
        testShip1.add(testShipDashboard1);
        testShip1.add(testShipDashboard2);
        testShip1.add(testShipDashboard3);
        testShip1.add(testShipDashboard4);

        Epidemic cardToTest = new Epidemic(2, "");
        cardToTest.apply(testShip1);

        assertTrue(testShipDashboard1.malusCrew);
        assertFalse(testShipDashboard2.malusCrew);
        assertFalse(testShipDashboard3.malusCrew);
        assertTrue(testShipDashboard4.malusCrew);
    }

    @Test
    void testVisit() {
        Epidemic cardToTest = new Epidemic(3, "");
        TestVisit testVisit = new TestVisit();
        cardToTest.apply(testVisit);
        assertTrue(testVisit.applied);

    }

    @Test
    void testReturn() {
        Epidemic cardToTest = new Epidemic(3, "");
        TestReturner testReturner = new TestReturner();
        assertEquals(1, cardToTest.returner(testReturner));
    }

    @Test
    void testClone(){
        Epidemic cardToTest = new Epidemic(3, "");
        Epidemic cardClone = cardToTest.clone();
        assertTrue(cardClone.lvl == cardToTest.lvl);
    }

    @Test
    void testToString() {
        Epidemic cardToTest = new Epidemic(3, "");
        assertEquals("Epidemic\n", cardToTest.toString());
    }
}