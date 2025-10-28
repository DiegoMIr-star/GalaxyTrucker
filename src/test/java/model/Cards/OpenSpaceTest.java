package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.Projectiles.Projectile;
import model.ShipDashboard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceTest {

    private static class TestVisit implements CardVisitor {
        private boolean applied = false;

        @Override
        public void visit(Stardust card) {}

        @Override
        public void visit(Planets card) {}

        @Override
        public void visit(OpenSpace card) {
            applied = true;
        }

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
            return 0;
        }

        @Override
        public int visit(Planets card) {
            return 0;
        }

        @Override
        public int visit(OpenSpace card) {
            return 1;
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
        OpenSpace openSpace = new OpenSpace(1, "");
        TestVisit testVisit = new TestVisit();
        openSpace.apply(testVisit);
        assertTrue(testVisit.applied);
    }

    @Test
    void testToString() {
        OpenSpace openSpace = new OpenSpace(1, "");
        assertEquals("Open space\n", openSpace.toString());
    }

    @Test
    void testClone() {
        OpenSpace openSpace = new OpenSpace(1, "");
        OpenSpace clone = openSpace.clone();
        assertEquals(openSpace.lvl, clone.lvl);
    }

    @Test
    void testReturn() {
        OpenSpace openSpace= new OpenSpace(1, "");
        TestReturner testReturner = new TestReturner();
        assertEquals(1, openSpace.returner(testReturner));
    }

    private static class TestShipDashboard extends ShipDashboard {
        public int daysToMove = -5;

        public TestShipDashboard() {
            super();
        }

        @Override
        public void setDaysToMove(int daysToMove) {
            this.daysToMove = daysToMove;
        }
    }

    @Test
    void testDaysToMove() {
        OpenSpace cardToTest = new OpenSpace(2, "");
        TestShipDashboard player1 = new TestShipDashboard();

        cardToTest.setDaysToMove(3, player1);

        assertEquals(3, player1.daysToMove);
    }

}