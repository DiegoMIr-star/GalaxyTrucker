package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.ShipDashboard;
import model.Stocks;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class StardustTest {
    private static class TestVisit implements CardVisitor {
        private boolean applied = false;

        @Override
        public void visit(Stardust card) {
            applied = true;
        }

        @Override
        public void visit(Planets card) {}

        @Override
        public void visit(OpenSpace card) {}

        @Override
        public void visit(MeteorSwarm card) {}

        @Override
        public void visit(Epidemic card) {}

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
            return 1;
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
            return 0;
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

    @Test
    void testVisit() {
        Stardust stardust= new Stardust(2, "");
        TestVisit testVisit= new TestVisit();
        stardust.apply(testVisit);
        assertTrue(testVisit.applied);
    }

    @Test
    void testToString() {
        Stardust stardust= new Stardust(2, "");
        assertEquals("Stardust\n", stardust.toString());
    }

    @Test
    void testClone() {
        Stardust stardust= new Stardust(2, "");
        Stardust clone= stardust.clone();
        assertEquals(stardust.lvl, clone.lvl);
    }

    @Test
    void testReturn() {
        Stardust stardust= new Stardust(2, "");
        TestReturner testReturner= new TestReturner();
        assertEquals(1, stardust.returner(testReturner));
    }

    private static class TestShipDashboard extends ShipDashboard {
        private int daysLoss = 0;
        private int exposedConnectors = 0;
        public TestShipDashboard(int exposedConnectors) {
            super();
            this.exposedConnectors = exposedConnectors;
        }

        @Override
        public int getNumOfExposedConnectors(){
            return exposedConnectors;
        }

        @Override
        public void setDaysToMove(int daysToMove) {
            daysLoss = daysToMove;
        }

        public int getDaysToMove() {
            return daysLoss;
        }
    }

    @Test
    void testApply(){
        TestShipDashboard testShipDashboard1 = new TestShipDashboard(1);
        TestShipDashboard testShipDashboard2 = new TestShipDashboard(2);
        TestShipDashboard testShipDashboard3 = new TestShipDashboard(3);
        TestShipDashboard testShipDashboard4 = new TestShipDashboard(4);

        ArrayList<ShipDashboard> shipDashboards = new ArrayList<>();
        shipDashboards.add(testShipDashboard1);
        shipDashboards.add(testShipDashboard2);
        shipDashboards.add(testShipDashboard3);
        shipDashboards.add(testShipDashboard4);

        Stardust cardToTest = new Stardust(2,"");

        cardToTest.apply(shipDashboards);

        assertEquals(-1, testShipDashboard1.getDaysToMove());
        assertEquals(-2, testShipDashboard2.getDaysToMove());
        assertEquals(-3, testShipDashboard3.getDaysToMove());
        assertEquals(-4, testShipDashboard4.getDaysToMove());
    }
}