package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.Projectiles.Orientation;
import model.Projectiles.Projectile;
import model.Projectiles.ProjectileType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MeteorSwarmTest {
    private static class TestVisit implements CardVisitor {
        private boolean applied = false;

        @Override
        public void visit(Stardust card) {}

        @Override
        public void visit(Planets card) {}

        @Override
        public void visit(OpenSpace card) {}

        @Override
        public void visit(MeteorSwarm card) {
            applied = true;
        }

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
            return 0;
        }

        @Override
        public int visit(MeteorSwarm card) {
            return 1;
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
        MeteorSwarm meteorSwarm= new MeteorSwarm(1, new ArrayList<Projectile>(), "" );
        TestVisit visit = new TestVisit();
        meteorSwarm.apply(visit);
        assertTrue(visit.applied);
    }

    @Test
    void testReturn() {
        TestReturner testReturner = new TestReturner();
        MeteorSwarm meteorSwarm= new MeteorSwarm(2, new ArrayList<Projectile>(), "");
        assertEquals(1, meteorSwarm.returner(testReturner));
    }

    @Test
    void testGetNextMeteorToHandle(){
        //single projectile
        Projectile testerProjSingle= mock(Projectile.class);
        ArrayList<Projectile> projectiles = new ArrayList<>();
        projectiles.add(testerProjSingle);
        //adding the single projectile in the set of projectiles to handle-> the single is the next one
        MeteorSwarm cardToTest = new MeteorSwarm(2, projectiles, "" );
        assertEquals(testerProjSingle ,cardToTest.getNextMeteorToHandle());
    }

    @Test
    void testClone() {
        MeteorSwarm meteorSwarm= new MeteorSwarm(1, new ArrayList<>(), "");
        MeteorSwarm clone = meteorSwarm.clone();

        assertEquals(meteorSwarm.lvl, clone.lvl);
        assertEquals(meteorSwarm.meteorites, clone.meteorites);
        assertEquals(meteorSwarm.name, clone.name);
        assertEquals(meteorSwarm.nextMeteorToHandle, clone.nextMeteorToHandle);
    }

    @Test
    void testToString(){
        Projectile testerProjSingle= mock(Projectile.class);
        ArrayList<Projectile> projectiles = new ArrayList<>();
        projectiles.add(testerProjSingle);
        MeteorSwarm meteorSwarm= new MeteorSwarm(1, projectiles, "" );
        assertEquals("Meteor swarm / Stray big meteors\n" +
                "    Meteors: " + projectiles.getFirst().toString() + "\n" ,meteorSwarm.toString());
    }

    @Test
    void testGetNextMeteoToHandle(){
        Projectile testerProjSingle= mock(Projectile.class);
        ArrayList<Projectile> projectiles = new ArrayList<>();
        projectiles.add(testerProjSingle);
        MeteorSwarm cardToTest = new MeteorSwarm(2, projectiles, "" );
        assertEquals(testerProjSingle ,cardToTest.getNextMeteorToHandle());

    }

    @Test
    void testMeteorSwarm(){

        ArrayList<Projectile> projectilesMeteors = new ArrayList<>();
        projectilesMeteors.add(new Projectile(Orientation.NORTH, ProjectileType.HEAVY_SHOT));
        projectilesMeteors.add(new Projectile(Orientation.EAST, ProjectileType.LIGHT_SHOT));
        projectilesMeteors.add(new Projectile(Orientation.SOUTH, ProjectileType.SMALL_ASTEROID));
        projectilesMeteors.add(new Projectile(Orientation.WEST, ProjectileType.BIG_ASTEROID));

        MeteorSwarm cardToTest = new MeteorSwarm(2, projectilesMeteors, "");

        //example of a sequence
        assertEquals(0, cardToTest.getIndexOfNextMeteorToHandle());
        assertFalse(cardToTest.allMeteorsHandled());
        cardToTest.markPlayerAsDoneManagingCurrentMeteor();
        assertEquals(1, cardToTest.getPlayersDoneManagingCurrentMeteor());
        cardToTest.markPlayerAsDoneManagingCurrentMeteor();
        cardToTest.markMeteorAsHandled();
        assertEquals(1, cardToTest.getIndexOfNextMeteorToHandle());
        assertEquals(0, cardToTest.getPlayersDoneManagingCurrentMeteor());
        cardToTest.markPlayerAsDoneManagingCurrentMeteor();
        assertEquals(1, cardToTest.getPlayersDoneManagingCurrentMeteor());
        cardToTest.markMeteorAsHandled();
        cardToTest.markPlayerAsDoneManagingCurrentMeteor();
        assertEquals(2, cardToTest.getIndexOfNextMeteorToHandle());
        cardToTest.markPlayerAsDoneManagingCurrentMeteor();
        assertEquals(2, cardToTest.getPlayersDoneManagingCurrentMeteor());
        cardToTest.markMeteorAsHandled();
        assertEquals(3, cardToTest.getIndexOfNextMeteorToHandle());
    }
}