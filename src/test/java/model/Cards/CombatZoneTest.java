package model.Cards;

import model.Cards.Enemies.Pirates;
import model.Cards.Enemies.Slavers;
import model.Cards.Enemies.Smugglers;
import model.Projectiles.Projectile;
import model.ShipDashboard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CombatZoneTest {

    private static class TestShipDashboard extends ShipDashboard {
        private final int crew;

        public TestShipDashboard(int crew) {
            this.crew = crew;
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
        public void visit(CombatZone card) {
            applied = true;
        }

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
            return 0;
        }

        @Override
        public int visit(CombatZone card) {
            return 1;
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
    void line1Test(){
        ShipDashboard shipDashboard0 = new TestShipDashboard(0);
        ShipDashboard shipDashboard1 = new TestShipDashboard(1);
        ShipDashboard shipDashboard2 = new TestShipDashboard(2);
        ShipDashboard shipDashboard3 = new TestShipDashboard(3);

        ArrayList<ShipDashboard> shipDashboards = new ArrayList<>();
        shipDashboards.add(shipDashboard0);
        shipDashboards.add(shipDashboard1);
        shipDashboards.add(shipDashboard2);
        shipDashboards.add(shipDashboard3);

        CombatZone cardToTest = new CombatZone(2, 3, 1, 2, new Projectile[0], "");

        cardToTest.Line1(shipDashboards);

        assertEquals(0, cardToTest.getAffectedPlayerIndex());
        assertEquals(-3, shipDashboard0.getDaysToMove());
        assertEquals(0, shipDashboard1.getDaysToMove());
        assertEquals(0, shipDashboard2.getDaysToMove());
        assertEquals(0, shipDashboard3.getDaysToMove());
    }

    @Test
    void powerValues(){
        CombatZone cardToTest = new CombatZone(2, 3, 1, 2, new Projectile[0],"");

        cardToTest.setLowestMotorPower(1);
        cardToTest.setLowestFirePower(2);

        assertEquals(1, cardToTest.getLowestMotorPower());
        assertEquals(2, cardToTest.getLowestFirePower());
    }

    @Test
    void handleTheRightProjectile(){
        CombatZone cardToTest = new CombatZone(2, 3, 1, 2, new Projectile[3], "");

        //the fourth projectile must be handled
        assertFalse(cardToTest.allShotsHandled());
        cardToTest.markShotAsHandled();
        assertFalse(cardToTest.allShotsHandled());
        cardToTest.markShotAsHandled();
        assertFalse(cardToTest.allShotsHandled());
        cardToTest.markShotAsHandled();
        assertTrue(cardToTest.allShotsHandled());
    }

    @Test
    void testVisit() {
        CombatZone cardToTest = new CombatZone(2, 3, 1, 2, new Projectile[0], "");
        TestVisit testVisit = new TestVisit();
        cardToTest.apply(testVisit);

        assertTrue(testVisit.applied);
    }

    @Test
    void returnVisit() {
        CombatZone cardToTest = new CombatZone(2, 3, 1, 2, new Projectile[0], "");
        TestReturner cardToVisit = new TestReturner();

        assertEquals(1,cardToTest.returner(cardToVisit));
    }

    @Test
    void testClone(){
        CombatZone cardToTest = new CombatZone(2, 3, 1, 2, new Projectile[0], "");
        CombatZone cardToTestClone = cardToTest.clone();
        assertEquals(2, cardToTestClone.lvl);
        assertEquals(3, cardToTestClone.daysLossLine1);
        assertEquals(1, cardToTestClone.crewLossLine2);
        assertEquals(2, cardToTestClone.stocksPenaltyLine2);
        assertEquals(cardToTest.hitsLine3, cardToTestClone.hitsLine3);
    }

    @Test
    void toStringTest(){
        Projectile[] testerProj= new Projectile[]{mock(Projectile.class)};
        when(Arrays.toString(testerProj)).thenReturn("Proj");
        CombatZone cardToTest = new CombatZone(2, 3, 1, 2, testerProj, "");
        assertEquals("Combat zone\n" +
                "    Player with the smallest crew - days lost: " + cardToTest.daysLossLine1 + "\n" +
                "    Player with the weakest engines - " +
                (cardToTest.crewLossLine2 != 0 ? "crew lost: " + cardToTest.crewLossLine2 : "stocks lost: " + cardToTest.stocksPenaltyLine2) + "\n" +
                "    Player with the weakest cannons - shots: " + cardToTest.hitsLine3[0].toString() + "\n", cardToTest.toString());
    }

    @Test
    void testSetAffectedPlayerIndex(){
        CombatZone cardToTest = new CombatZone(2, 3, 1, 2, new Projectile[0], "");
        cardToTest.setAffectedPlayerIndex(12);
        assertEquals(12, cardToTest.getAffectedPlayerIndex());
    }

    @Test
    void testGetNextHitToHandle(){
        //single projectile
        Projectile testerProjSingle= mock(Projectile.class);
        //adding the single projectile in the set of projectiles to handle-> the single is the next one
        CombatZone cardToTest = new CombatZone(2, 3, 1, 2, new Projectile[]{testerProjSingle}, "");
        assertEquals(testerProjSingle ,cardToTest.getNextHitToHandle());
    }
}