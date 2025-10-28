package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.ShipDashboard;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipTest {
    //creating a test player to try the card
    private static class TestShipDashboard extends ShipDashboard {
        public boolean bonusMalusApplied = false;
        public int bonusCount = 0;
        public int malusDaysCount = 0;

        @Override
        public void bonusMalus(int malusDays, int bonus) {
            bonusMalusApplied = true;
            bonusCount = bonus;
            malusDaysCount = malusDays;
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
        public void visit(Epidemic card) {}

        @Override
        public void visit(CombatZone card) {}

        @Override
        public void visit(AbandonedShip card) {
            applied = true;
        }

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
            return 0;
        }

        @Override
        public int visit(CombatZone card) {
            return 0;
        }

        @Override
        public int visit(AbandonedShip card) {
            return 1;
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
    void testConstructor() {
    AbandonedShip cardToTest = new AbandonedShip(3,1, 3,2, "");

    //testing if the constructor works correctly
    assertEquals(3, cardToTest.crewLoss);
    assertEquals(1, cardToTest.daysLoss);
    assertEquals(3, cardToTest.creditsGained);
    assertEquals(2, cardToTest.lvl);
    }

    @Test
    void bonusMalusApplied() {
        //creating yhe card and the ship dashboard to test
        AbandonedShip cardToTest1 = new AbandonedShip(3,2, 3,2, "");
        TestShipDashboard testShipDashboard1 = new TestShipDashboard();

        cardToTest1.bonusMalus(testShipDashboard1);

        //test if everything is applied correctly when I call the method
        assertTrue(testShipDashboard1.bonusMalusApplied);
        assertEquals(3, testShipDashboard1.bonusCount);
        assertEquals(2, testShipDashboard1.malusDaysCount);
    }

    @Test
    void cloneTest(){
        AbandonedShip cardToTest = new AbandonedShip(3,1, 3,2, "");
        AbandonedShip cloneCard = cardToTest.clone();
        assertEquals(3, cloneCard.crewLoss);
        assertEquals(1, cloneCard.daysLoss);
        assertEquals(3, cloneCard.creditsGained);
        assertEquals(2, cloneCard.lvl);
    }

    @Test
    void testToString() {
        AbandonedShip cardToTest = new AbandonedShip(3,1, 3,2, "");
        assertEquals("Abandoned ship\n" +
                "    Reward: " + cardToTest.creditsGained + " credits\n" +
                "    Days lost: " + cardToTest.daysLoss + "\n" +
                "    Humans lost: " + cardToTest.crewLoss + "\n", cardToTest.toString());
    }

    @Test
    void testVisit() {
        AbandonedShip card = new AbandonedShip(3,1, 3,2, "");
        TestVisit testVisit = new TestVisit();
        card.apply(testVisit);

        assertTrue(testVisit.applied);
    }

    @Test
    void returnVisit() {
        AbandonedShip card = new AbandonedShip(3,1, 3,2, "");
        TestReturner cardToVisit = new TestReturner();

        assertEquals(1,card.returner(cardToVisit));
    }

}