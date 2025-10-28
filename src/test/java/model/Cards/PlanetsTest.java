package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.ShipDashboard;
import model.Stocks;
import model.exceptions.InvalidIndexException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlanetsTest {
    private static class TestVisit implements CardVisitor {
        private boolean applied = false;

        @Override
        public void visit(Stardust card) {}

        @Override
        public void visit(Planets card) {
            applied = true;
        }

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
            return 0;
        }

        @Override
        public int visit(Planets card) {
            return 1;
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
        Planets planets= new Planets(1, 2, new ArrayList<>(), "");
        TestVisit visitTest = new TestVisit();
        planets.apply(visitTest);
        assertTrue(visitTest.applied);
    }

    @Test
    void testToString() {
        ArrayList<Stocks> stocks = new ArrayList<>();
        Stocks stocks1= mock(Stocks.class);
        stocks.add(stocks1);
        Planets planets= new Planets(1, 2, stocks, "");
        assertEquals("Planets\n" +
                "    Days lost by landing: " + planets.daysLost + "\n" + "    Planet 1: " + stocks.getFirst().toString() + "\n", planets.toString());
    }

    @Test
    void testClone() {
        Planets planets= new Planets(1, 2, new ArrayList<>(), "");
        Planets clone = planets.clone();
        assertEquals(planets.lvl, clone.lvl);
        assertEquals(planets.daysLost, clone.daysLost);
        assertEquals(planets.planetStocks, clone.planetStocks);
    }

    @Test
    void testReturn() {
        Planets planets= new Planets(1, 2, new ArrayList<>(), "");
        TestReturner testReturner = new TestReturner();
        assertEquals(1, planets.returner(testReturner));
    }

    private static class TestShipDashboard extends ShipDashboard {
        public boolean daysLoss = false;
        public int daysToMove = 0;

        TestShipDashboard() {
            super();
        }

        @Override
        public void setDaysToMove(int daysToMove) {
            this.daysToMove = daysToMove;
            this.daysLoss = true;
        }
    }

    @Test
    void testConstructor() {
        ArrayList<Stocks> stocks = new ArrayList<>();
        stocks.add(new Stocks());
        stocks.add(new Stocks());
        stocks.add(new Stocks());

        Planets cardToTest = new Planets(2,2,stocks, "");

        assertEquals(2, cardToTest.lvl);
        assertEquals(stocks, cardToTest.planetStocks);
        assertEquals(2, cardToTest.daysLost);
    }

    @Test
    void testGetStocks() {
        Stocks stocks0 = new Stocks();
        Stocks stocks1 = new Stocks();
        Stocks stocks2 = new Stocks();
        ArrayList<Stocks> stocksTest = new ArrayList<>();
        stocksTest.add(stocks0);
        stocksTest.add(stocks1);
        stocksTest.add(stocks2);

        Planets cardPlanets = new Planets(2,2,stocksTest, "");

        assertEquals(stocks0, cardPlanets.getStocks(0));
        assertEquals(stocks1, cardPlanets.getStocks(1));
        assertEquals(stocks2, cardPlanets.getStocks(2));
    }

    @Test
    void testLandingPlanet() {
        ArrayList<Stocks> stocks1 = new ArrayList<>();
        stocks1.add(new Stocks());
        stocks1.add(new Stocks());
        stocks1.add(new Stocks());

        Planets cardToTest1 = new Planets(2,2,stocks1, "");
        TestShipDashboard player1 = new TestShipDashboard();

        cardToTest1.land(2, player1);

        assertTrue(player1.daysLoss);
        assertEquals(-2, player1.daysToMove);

        //the third planet (index 2) must result occupied and the others free
        ArrayList<Integer> freePlanets = cardToTest1.getFreePlanets();
        assertEquals(2, freePlanets.size());
        assertFalse(freePlanets.contains(2));
        assertTrue(freePlanets.contains(0));
        assertTrue(freePlanets.contains(1));
    }


    @Test
    void testLandingPlanetNotValidPlanet() {
        ArrayList<Stocks> stocks2 = new ArrayList<>();
        stocks2.add(new Stocks());
        stocks2.add(new Stocks());
        stocks2.add(new Stocks());
        stocks2.add(new Stocks());

        Planets cardToTest2 = new Planets(2,2,stocks2, "");
        TestShipDashboard player1 = new TestShipDashboard();

        //index too high
        assertThrows(InvalidIndexException.class, () ->cardToTest2.land(5, player1));
    }

    @Test
    void testLandingPlanetNotValidPlanetTooLow() {
        ArrayList<Stocks> stocks2 = new ArrayList<>();
        stocks2.add(new Stocks());
        stocks2.add(new Stocks());
        stocks2.add(new Stocks());
        stocks2.add(new Stocks());

        Planets cardToTest2 = new Planets(2,2,stocks2, "");
        TestShipDashboard player1 = new TestShipDashboard();

        //index too low
        assertThrows(InvalidIndexException.class, () ->cardToTest2.land(-3, player1));
    }

    @Test
    void testLandingPlanetOccupied() {
        ArrayList<Stocks> stocks3 = new ArrayList<>();
        stocks3.add(new Stocks());
        stocks3.add(new Stocks());
        stocks3.add(new Stocks());

        Planets cardToTest3 = new Planets(2,2,stocks3, "");
        TestShipDashboard player1 = new TestShipDashboard();
        TestShipDashboard player2 = new TestShipDashboard();

        //throws the exception in case of planet occupied
        cardToTest3.land(2, player1);
        assertThrows(InvalidIndexException.class, () ->cardToTest3.land(2, player2));

    }

}