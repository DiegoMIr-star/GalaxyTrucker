package model.Cards.Enemies;

import model.Cards.*;
import model.Stocks;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SmugglersTest {
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
        public void visit(Epidemic card) {}

        @Override
        public void visit(CombatZone card) {}

        @Override
        public void visit(AbandonedShip card) {}

        @Override
        public void visit(AbandonedStation card) {}

        @Override
        public void visit(Smugglers card) {
            applied = true;
        }

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
            return 1;
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
        Smugglers smugglers = new Smugglers(new Stocks(), 1, 3,2,1, "");
        TestVisit testVisit = new TestVisit();
        smugglers.apply(testVisit);
        assertTrue(testVisit.applied);
    }

    @Test
    void testToString() {
        Smugglers smugglers = new Smugglers(new Stocks(), 1, 3,2,1, "");
        assertEquals(
                "Smugglers\n" +
                        "    Minimum fire power Required: " + smugglers.firePower + "\n" +
                        "    Days lost: " + smugglers.daysLoss + "\n" +
                        "    Reward: " + smugglers.stocks.toString() + "\n" +
                        "    Stocks lost: " + smugglers.penaltyStocks + " most precious containers.\n", smugglers.toString());
    }

    @Test
    void testClone() {
        Smugglers smugglers = new Smugglers(new Stocks(), 1, 3,2,1, "");
        Smugglers clone= smugglers.clone();

        assertEquals(smugglers.lvl, clone.lvl);
        assertEquals(smugglers.penaltyStocks, clone.penaltyStocks);
        assertEquals(smugglers.daysLoss, clone.daysLoss);
        assertEquals(smugglers.firePower, clone.firePower);
        assertEquals(smugglers.stocks, clone.stocks);
    }

    @Test
    void testReturn() {
        Smugglers smugglers = new Smugglers(new Stocks(), 1, 3,2,1, "");
        TestReturner testReturner= new TestReturner();
        assertEquals(1, smugglers.returner(testReturner));
    }

    @Test
    void testConstructor() {
        Stocks stocks = new Stocks();
        int lvl = 2;
        int firePower = 3;
        int daysLoss = 4;
        int malus = 1;

        Smugglers smugglersTestCard = new Smugglers(stocks, lvl, firePower, daysLoss, malus, "");

        assertEquals(lvl, smugglersTestCard.lvl);
        assertEquals(firePower, smugglersTestCard.firePower);
        assertEquals(daysLoss, smugglersTestCard.daysLoss);
        assertEquals(stocks, smugglersTestCard.stocks);
        assertEquals(malus, smugglersTestCard.penaltyStocks);
    }
}