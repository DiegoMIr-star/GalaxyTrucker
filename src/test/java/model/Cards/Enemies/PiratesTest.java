package model.Cards.Enemies;

import model.Cards.*;
import model.Projectiles.Orientation;
import model.Projectiles.Projectile;
import model.Projectiles.ProjectileType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PiratesTest {
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
        public void visit(Slavers card) {}

        @Override
        public void visit(Pirates card) {
            applied = true;
        }
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
            return 0;
        }

        @Override
        public int visit(Pirates card) {
            return 1;
        }

    }

    @Test
    void testVisit() {
        Pirates pirates= new Pirates(2, 5, 1, 89, new ArrayList<>(), "");
        TestVisit testVisit = new TestVisit();
        pirates.apply(testVisit);
        assertTrue(testVisit.applied);
    }

    @Test
    void testToString() {
        ArrayList<Projectile> projectiles = new ArrayList<>();
        Projectile projectile= mock(Projectile.class);
        projectiles.add(projectile);
        Pirates pirates= new Pirates(2, 5, 1, 89, projectiles, "");
        assertEquals(
                "Pirates\n" +
                        "    Minimum fire power Required: " + pirates.firePower + "\n" +
                        "    Days lost: " + pirates.daysLoss + "\n" +
                        "    Reward: " + pirates.creditsGained + " credits\n" +
                        "    Shots: " + projectiles.getFirst().toString() + "\n", pirates.toString());
    }

    @Test
    void testClone() {
        Pirates pirates= new Pirates(2, 5, 1, 89, new ArrayList<>(), "");
        Pirates clone= pirates.clone();

        assertEquals(pirates.lvl, clone.lvl);
        assertEquals(pirates.creditsGained, clone.creditsGained);
        assertEquals(pirates.shots, clone.shots);
        assertEquals(pirates.daysLoss, clone.daysLoss);
    }

    @Test
    void testReturn() {
        Pirates pirates= new Pirates(2, 5, 1, 89, new ArrayList<>(), "");
        TestReturner testReturner= new TestReturner();
        assertEquals(1, pirates.returner(testReturner));
    }

    @Test
    void testConstructor(){
        ArrayList<Projectile> meteorsProjectiles = new ArrayList<>();
        meteorsProjectiles.add(new Projectile(Orientation.NORTH, ProjectileType.LIGHT_SHOT));
        meteorsProjectiles.add(new Projectile(Orientation.SOUTH, ProjectileType.HEAVY_SHOT));
        meteorsProjectiles.add(new Projectile(Orientation.WEST, ProjectileType.LIGHT_SHOT));

        Pirates cardToTest = new Pirates(2, 3, 3, 2, meteorsProjectiles, "");

        assertEquals(2, cardToTest.lvl);
        assertEquals(3, cardToTest.firePower);
        assertEquals(3, cardToTest.daysLoss);
        assertEquals(2, cardToTest.creditsGained);
        assertEquals(meteorsProjectiles, cardToTest.shots);
        assertEquals(-1, cardToTest.getTrajectory(0));
        assertEquals(-1, cardToTest.getTrajectory(1));
        assertEquals(-1, cardToTest.getTrajectory(2));
    }

    @Test
    void RandomizeTrajectoryTestValueInterval() {
        //it's just an example to try if it generates numbers correctly... it's not sure
        ArrayList<Projectile> meteorsProjectiles = new ArrayList<>();
        meteorsProjectiles.add(new Projectile(Orientation.NORTH, ProjectileType.LIGHT_SHOT));
        meteorsProjectiles.add(new Projectile(Orientation.SOUTH, ProjectileType.HEAVY_SHOT));
        meteorsProjectiles.add(new Projectile(Orientation.WEST, ProjectileType.LIGHT_SHOT));
        meteorsProjectiles.add(new Projectile(Orientation.EAST, ProjectileType.LIGHT_SHOT));

        Pirates pirates = new Pirates(2, 3, 3, 2, meteorsProjectiles, "");
        pirates.randomizeTrajectory(0);
        pirates.randomizeTrajectory(1);
        pirates.randomizeTrajectory(2);
        pirates.randomizeTrajectory(3);

        assertTrue(pirates.getTrajectory(0) >= 2 && pirates.getTrajectory(0) <=12);
        assertTrue(pirates.getTrajectory(1) >= 2 && pirates.getTrajectory(1) <=12);
        assertTrue(pirates.getTrajectory(2) >= 2 && pirates.getTrajectory(2) <=12);
        assertTrue(pirates.getTrajectory(3) >= 2 && pirates.getTrajectory(3) <=12);
    }

    @Test
    void nextShotToHandle() {
        ArrayList<Projectile> meteorsProjectiles = new ArrayList<>();
        meteorsProjectiles.add(new Projectile(Orientation.NORTH, ProjectileType.LIGHT_SHOT));
        meteorsProjectiles.add(new Projectile(Orientation.SOUTH, ProjectileType.HEAVY_SHOT));

        Pirates pirates = new Pirates(2, 3, 3, 2, meteorsProjectiles, "");

        pirates.markShotAsHandled();
        assertEquals(1, pirates.getIndexOfNextShotToHandle());
        pirates.markShotAsHandled();
        assertEquals(2, pirates.getIndexOfNextShotToHandle());
        pirates.markShotAsHandled();
        //the array contains only 2 projectiles, is it right that the method continues to accept new positions?
        assertEquals(3, pirates.getIndexOfNextShotToHandle());
        pirates.prepareShotsForNewTurn();
        assertEquals(0, pirates.getIndexOfNextShotToHandle());
    }
}