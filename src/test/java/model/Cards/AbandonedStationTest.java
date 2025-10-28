package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.ShipDashboard;
import model.Stocks;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class AbandonedStationTest {
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
        public void visit(AbandonedStation card) {
            applied = true;
        }

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
            return 1;
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


    private static class TestShipDashboard extends ShipDashboard {
        public int daysLoss = 0;
        public boolean daysLossApplied = false;

        public TestShipDashboard() {
            super();
        }

        @Override
        public void setDaysToMove(int daysLoss) {
            this.daysLoss = daysLoss;
            daysLossApplied = true;
        }
    }
        @Test
        void testConstructor() {
            Stocks stocks = new Stocks();
            AbandonedStation cardToTest = new AbandonedStation(3,2, stocks, 2, "");

            assertEquals(3, cardToTest.daysLoss);
            assertEquals(2, cardToTest.requiredCrew);
            assertEquals(stocks, cardToTest.stocks);
            assertEquals(2, cardToTest.lvl);
        }

        @Test
        void daysLossApplied() {
            Stocks stocks = new Stocks();
            TestShipDashboard dashboard1 = new TestShipDashboard();

            AbandonedStation cardToTest = new AbandonedStation(3,4, stocks, 2, "");

            cardToTest.loseDays(dashboard1);

            assertTrue(dashboard1.daysLossApplied);
            assertEquals(-3,dashboard1.daysLoss);

        }

        @Test
        void applyVisit() {
        AbandonedStation toTest= new AbandonedStation(1,2, null ,2, "");
        TestVisit cardToVisit = new TestVisit();
        toTest.apply(cardToVisit);

        assertTrue(cardToVisit.applied);
        }

        @Test
        void returnVisit() {
        AbandonedStation toTest= new AbandonedStation(1,2, null ,2, "");
        TestReturner cardToVisit = new TestReturner();

        assertEquals(1,toTest.returner(cardToVisit));
        }

        @Test
        void toStringTest() {
            AbandonedStation toTest= new AbandonedStation(1,2, new Stocks() ,2, "");
            assertEquals("Abandoned station\n" +
                    "    Reward: " + toTest.stocks + "\n" +
                    "    Days lost: " + 1 + "\n" +
                    "    Required crew: " + 2 + "\n", toTest.toString());

        }

        @Test
        void cloneTest() {
        AbandonedStation toTest= new AbandonedStation(1,2, new Stocks() ,2, "");
        AbandonedStation clone= (AbandonedStation) toTest.clone();

        //it's cloned-> all attributes are the same
        assertTrue(toTest.daysLoss==clone.daysLoss);
        assertTrue(toTest.requiredCrew==clone.requiredCrew);
        assertTrue(toTest.stocks==clone.stocks);
        assertTrue(toTest.lvl==clone.lvl);
        }

    }