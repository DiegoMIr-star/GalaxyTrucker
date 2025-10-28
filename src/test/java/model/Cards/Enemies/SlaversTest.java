package model.Cards.Enemies;

import model.Cards.*;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SlaversTest {
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
        public void visit(Smugglers card) {}

        @Override
        public void visit(Slavers card) {
            applied = true;
        }

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
            return 0;
        }

        @Override
        public int visit(Slavers card) {
            return 1;
        }

        @Override
        public int visit(Pirates card) {
            return 0;
        }

    }

    @Test
    void testVisit() {
        Slavers slavers = new Slavers(1,1,1,2,2, "");
        TestVisit testVisit = new TestVisit();
        slavers.apply(testVisit);
        assertTrue(testVisit.applied);
    }

    @Test
    void testToString() {
        Slavers slavers = new Slavers(1, 2, 5,1,1, "");
        assertEquals(
                "Slavers\n" +
                        "    Minimum fire power Required: " + slavers.firePower + "\n" +
                        "    Days lost: " + slavers.daysLoss + "\n" +
                        "    Reward: " + slavers.creditsGained + " credits\n" +
                        "    Crew lost: " + slavers.crewLoss + "\n", slavers.toString());
    }

    @Test
    void testClone() {
        Slavers slavers = new Slavers(1, 2, 5,1,1, "");
        Slavers clone= slavers.clone();

        assertEquals(slavers.lvl, clone.lvl);
        assertEquals(slavers.creditsGained, clone.creditsGained);
        assertEquals(slavers.crewLoss, clone.crewLoss);
        assertEquals(slavers.daysLoss, clone.daysLoss);
        assertEquals(slavers.firePower, clone.firePower);
    }

    @Test
    void testReturn() {
        Slavers slavers = new Slavers(1, 2, 5,1,1, "");
        TestReturner testReturner= new TestReturner();
        assertEquals(1, slavers.returner(testReturner));
    }

    @Test
    void testConstructor() {
        int lvl = 2;
        int firePower = 3;
        int daysLoss = 4;
        int creditsGained = 5;
        int crewLoss = 3;

        Slavers slaversTestCard = new Slavers(lvl, firePower, daysLoss, crewLoss, creditsGained, "");

        assertEquals(lvl, slaversTestCard.lvl);
        assertEquals(firePower, slaversTestCard.firePower);
        assertEquals(daysLoss, slaversTestCard.daysLoss);
        assertEquals(crewLoss, slaversTestCard.crewLoss);
        assertEquals(creditsGained, slaversTestCard.creditsGained);
    }
}