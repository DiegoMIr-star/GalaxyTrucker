package model;

import model.DifferentShipComponents.*;
import model.Projectiles.Orientation;
import model.Projectiles.Projectile;
import model.Projectiles.ProjectileType;
import model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShipDashboardTest {
    private final ShipDashboard ship = new ShipDashboard();
    Stocks stocks = new Stocks();
    @BeforeEach
    void setUp() {
        String useless= "";
        ShipComponent cannon1 = new Cannon(Side.CannonSpace, Side.BlankSide, Side.SingleConnector, Side.BlankSide, false, 1,useless);
        ship.addComponent(cannon1, 2, 0, false);
        ShipComponent cannon2 = new Cannon(Side.CannonSpace, Side.BlankSide, Side.DoubleConnector, Side.BlankSide, false, 1,useless);
        ship.addComponent(cannon2, 4, 0, false);
        ShipComponent doubleCannon1 = new Cannon(Side.CannonSpace, Side.BlankSide, Side.BlankSide, Side.UniversalConnector, true, 60,useless);
        ship.addComponent(doubleCannon1, 5, 1, false);
        ShipComponent shield1 = new Shield(Side.ShieldProtection, Side.SingleConnector, Side.UniversalConnector, Side.ShieldProtection,useless);
        ship.addComponent(shield1, 1, 1, false);
        ShipComponent shield2 = new Shield(Side.ShieldProtection, Side.ShieldProtection, Side.SingleConnector, Side.UniversalConnector,useless);
        ship.addComponent(shield2, 6, 3, false);
        ShipComponent cargoHold1 = new CargoHold(Side.SingleConnector, Side.UniversalConnector, Side.SingleConnector, Side.SingleConnector, true,1,1,useless);
        ship.addComponent(cargoHold1, 2, 1, false);
        ShipComponent cargoHold2 = new CargoHold(Side.SingleConnector, Side.DoubleConnector, Side.SingleConnector, Side.UniversalConnector, false,2,1,useless);
        ship.addComponent(cargoHold2, 1, 2, false);
        ShipComponent cargoHold3 = new CargoHold(Side.BlankSide, Side.SingleConnector, Side.BlankSide, Side.BlankSide, false,3,1,useless);
        ship.addComponent(cargoHold3, 0, 4, false);
        ShipComponent cargoHold4 = new CargoHold(Side.DoubleConnector, Side.BlankSide, Side.BlankSide, Side.BlankSide, true,2,1,useless);
        ship.addComponent(cargoHold4, 4, 4, false);
        ShipComponent structural1 = new Structural(Side.UniversalConnector, Side.SingleConnector, Side.UniversalConnector, Side.SingleConnector,"");
        ship.addComponent(structural1, 4, 1, false);
        ShipComponent engine1 = new Engine(Side.UniversalConnector, Side.DoubleConnector, Side.MotorSpace, Side.SingleConnector, false,1,useless);
        ship.addComponent(engine1, 5, 3, false);
        ShipComponent engine2 = new Engine(Side.UniversalConnector, Side.BlankSide, Side.MotorSpace, Side.SingleConnector, false,1,useless);
        ship.addComponent(engine2, 1, 4, false);
        ShipComponent engine3 = new Engine(Side.SingleConnector, Side.BlankSide, Side.MotorSpace, Side.BlankSide, false,1,useless);
        ship.addComponent(engine3, 6, 4, false);
        ShipComponent doubleEngine1 = new Engine(Side.BlankSide, Side.UniversalConnector, Side.MotorSpace, Side.UniversalConnector, true,1,useless);
        ship.addComponent(doubleEngine1, 0, 2, false);
        ShipComponent doubleEngine2 = new Engine(Side.UniversalConnector, Side.BlankSide, Side.MotorSpace, Side.DoubleConnector, true,1,useless);
        ship.addComponent(doubleEngine2, 3, 3, false);
        //ShipComponent centralCabin = new Cabin(true, Side.UniversalConnector, Side.DoubleConnector, Side.MotorSpace, Side.SingleConnector,1);
        //ship.addComponent(centralCabin, 7, 7);
        ShipComponent cabin1 = new Cabin(false, Side.DoubleConnector, Side.DoubleConnector, Side.SingleConnector, Side.DoubleConnector, 1,useless);
        ship.addComponent(cabin1, 4, 2, false);
        ShipComponent cabin2 = new Cabin(false, Side.SingleConnector, Side.SingleConnector, Side.UniversalConnector, Side.BlankSide, 1,useless);
        ship.addComponent(cabin2, 4, 3, false);
        ShipComponent purpleLifeSupport = new LifeSupport(Side.SingleConnector, Side.DoubleConnector, Side.BlankSide, Side.DoubleConnector, true,1,useless);
        ship.addComponent(purpleLifeSupport, 2, 2, false);
        ShipComponent brownLifeSupport = new LifeSupport(Side.BlankSide, Side.BlankSide, Side.SingleConnector, Side.UniversalConnector, false,1,useless);
        ship.addComponent(brownLifeSupport, 5, 2, false);
        ShipComponent battery1 = new BatteryComponent(Side.UniversalConnector, Side.DoubleConnector, Side.DoubleConnector, Side.DoubleConnector, 2,1,useless);
        ship.addComponent(battery1, 1, 3, false);
        ShipComponent battery2 = new BatteryComponent(Side.BlankSide, Side.UniversalConnector, Side.BlankSide, Side.UniversalConnector, 2,1,useless);
        ship.addComponent(battery2, 2, 3, false);
    }


    @Test
    void getNumOfExposedConnectors() {
        ship.fillTheGaps();
        ship.finalizeShip();
        assertEquals(5, ship.getNumOfExposedConnectors());
    }

    // decrementing batteries sequentially, unit by unit
    @Test
    void useBatteries1() {
        ship.fillTheGaps();
        ship.finalizeShip();
        // use 1st battery
        ship.useBatteries(1);
        assertEquals(3, ship.getBatteries());
        for (int i = 0; i < ship.getShip().length; i++) {
            for (int j = 0; j < ship.getShip()[i].length; j++) {
                if (ship.getShip()[i][j].getType().equals(ComponentType.BATTERY_COMPONENT)) {
                    BatteryComponent battery = (BatteryComponent) ship.getShip()[i][j];
                    if (i == 3 && j == 1) {
                        assertEquals(1, battery.getCurrentBatteries());
                    }
                    else {
                        assertEquals(2, battery.getCurrentBatteries());
                    }
                }

            }
        }
        // use 2nd battery
        ship.useBatteries(1);
        assertEquals(2, ship.getBatteries());
        for (int i = 0; i < ship.getShip().length; i++) {
            for (int j = 0; j < ship.getShip()[i].length; j++) {
                if (ship.getShip()[i][j].getType().equals(ComponentType.BATTERY_COMPONENT)) {
                    BatteryComponent battery = (BatteryComponent) ship.getShip()[i][j];
                    if (i == 3 && j == 1) {
                        assertEquals(0, battery.getCurrentBatteries());
                    }
                    else {
                        assertEquals(2, battery.getCurrentBatteries());
                    }
                }

            }
        }
        // use 3rd battery
        ship.useBatteries(1);
        assertEquals(1, ship.getBatteries());
        for (int i = 0; i < ship.getShip().length; i++) {
            for (int j = 0; j < ship.getShip()[i].length; j++) {
                if (ship.getShip()[i][j].getType().equals(ComponentType.BATTERY_COMPONENT)) {
                    BatteryComponent battery = (BatteryComponent) ship.getShip()[i][j];
                    if (i == 3 && j == 1) {
                        assertEquals(0, battery.getCurrentBatteries());
                    }
                    else {
                        assertEquals(1, battery.getCurrentBatteries());
                    }
                }

            }
        }
        // use 4th and last battery
        ship.useBatteries(1);
        assertEquals(0, ship.getBatteries());
        for (int i = 0; i < ship.getShip().length; i++) {
            for (int j = 0; j < ship.getShip()[i].length; j++) {
                if (ship.getShip()[i][j].getType().equals(ComponentType.BATTERY_COMPONENT)) {
                    BatteryComponent battery = (BatteryComponent) ship.getShip()[i][j];
                    if (i == 3 && j == 1) {
                        assertEquals(0, battery.getCurrentBatteries());
                    }
                    else {
                        assertEquals(0, battery.getCurrentBatteries());
                    }
                }

            }
        }
        // use more batteries than needed
        assertThrows(NotEnoughBatteriesException.class, () -> ship.useBatteries(1));
        for (int i = 0; i < ship.getShip().length; i++) {
            for (int j = 0; j < ship.getShip()[i].length; j++) {
                if (ship.getShip()[i][j].getType().equals(ComponentType.BATTERY_COMPONENT)) {
                    BatteryComponent battery = (BatteryComponent) ship.getShip()[i][j];
                    if (i == 3 && j == 1) {
                        assertEquals(0, battery.getCurrentBatteries());
                    }
                    else {
                        assertEquals(0, battery.getCurrentBatteries());
                    }
                }

            }
        }
    }

    // decrementing batteries not sequentially
    @Test
    void useBatteries2() {
        ship.fillTheGaps();
        ship.finalizeShip();
        ship.useBatteries(3);
        assertEquals(1, ship.getBatteries());
        for (int i = 0; i < ship.getShip().length; i++) {
            for (int j = 0; j < ship.getShip()[i].length; j++) {
                if (ship.getShip()[i][j].getType().equals(ComponentType.BATTERY_COMPONENT)) {
                    BatteryComponent battery = (BatteryComponent) ship.getShip()[i][j];
                    if (i == 3 && j == 1) {
                        assertEquals(0, battery.getCurrentBatteries());
                    } else {
                        assertEquals(1, battery.getCurrentBatteries());
                    }
                }

            }
        }
    }

    @Test
    void removeComponent() {
        ship.fillTheGaps();
        ship.finalizeShip();
        assertEquals(0, ship.getGarbageHeap());
        assertNotEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[3][4].getType());
        ship.removeComponent(4, 3);
        assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[3][4].getType());
        assertEquals(1, ship.getGarbageHeap());
    }

    @Test
    void addStocks() {
        ship.fillTheGaps();
        ship.finalizeShip();
        stocks.add(10, 10, 10, 10);
        ship.addStocks(this.stocks);
        Stocks stocksToAdd = new Stocks(2, 0, 1, 2);
        ship.addStocks(stocksToAdd);
        assertEquals(10 + stocksToAdd.getSpecialRedStocks(), ship.getStocks().getSpecialRedStocks());
        assertEquals(10 + stocksToAdd.getYellowStocks(), ship.getStocks().getYellowStocks());
        assertEquals(10 + stocksToAdd.getGreenStocks(), ship.getStocks().getGreenStocks());
        assertEquals(10 + stocksToAdd.getBlueStocks(), ship.getStocks().getBlueStocks());
    }

    @Test
    void removeAllStocks() {
        ship.fillTheGaps();
        ship.finalizeShip();
        stocks.add(10, 10, 10, 10);
        ship.addStocks(this.stocks);
        Stocks stocksToRemove = new Stocks(10, 10, 10, 10);
        ship.removeStocks(this.stocks);
        assertEquals(10 - stocksToRemove.getSpecialRedStocks(), ship.getStocks().getSpecialRedStocks());
        assertEquals(10 - stocksToRemove.getYellowStocks(), ship.getStocks().getYellowStocks());
        assertEquals(10 - stocksToRemove.getGreenStocks(), ship.getStocks().getGreenStocks());
        assertEquals(10 - stocksToRemove.getBlueStocks(), ship.getStocks().getBlueStocks());
    }

    @Test
    void removeMoreStocksThanStored() {
        ship.fillTheGaps();
        ship.finalizeShip();
        stocks.add(10, 10, 10, 10);
        ship.addStocks(this.stocks);
        Stocks stocksToRemove = new Stocks(11, 10, 10, 10);
        ship.removeStocks(stocksToRemove);
    }

    //tests when there aren't enough red stocks to pay the penalty
    @Test
    void removeMostPreciousStocks1() {
        ship.fillTheGaps();
        ship.finalizeShip();
        stocks.add(10, 10, 10, 10);
        ship.addStocks(this.stocks);
        ship.removeMostPreciousStocks(11);
        assertEquals(0, ship.getStocks().getSpecialRedStocks());
        assertEquals(9, ship.getStocks().getYellowStocks());
        assertEquals(10, ship.getStocks().getGreenStocks());
        assertEquals(10, ship.getStocks().getBlueStocks());
    }

    //tests when there aren't enough red and yellow stocks to pay the penalty
    @Test
    void removeMostPreciousStocks2() {
        ship.fillTheGaps();
        ship.finalizeShip();
        stocks.add(10, 10, 10, 10);
        ship.addStocks(this.stocks);
        Stocks stocksToRemove = new Stocks(0, 10, 0, 0);
        ship.removeStocks(stocksToRemove);
        ship.removeMostPreciousStocks(11);
        assertEquals(0, ship.getStocks().getSpecialRedStocks());
        assertEquals(0, ship.getStocks().getYellowStocks());
        assertEquals(9, ship.getStocks().getGreenStocks());
        assertEquals(10, ship.getStocks().getBlueStocks());
    }

    //tests when there aren't enough red, yellow and green stocks to pay the penalty
    @Test
    void removeMostPreciousStocks3() {
        ship.fillTheGaps();
        ship.finalizeShip();
        stocks.add(10, 10, 10, 10);
        ship.addStocks(this.stocks);
        Stocks stocksToRemove = new Stocks(0, 10, 10, 0);
        ship.removeStocks(stocksToRemove);
        ship.removeMostPreciousStocks(11);
        assertEquals(0, ship.getStocks().getSpecialRedStocks());
        assertEquals(0, ship.getStocks().getYellowStocks());
        assertEquals(0, ship.getStocks().getGreenStocks());
        assertEquals(9, ship.getStocks().getBlueStocks());
    }

    //tests when there aren't enough red, yellow, green and blue stocks to pay the penalty
    @Test
    void removeMostPreciousStocks4() {
        ship.fillTheGaps();
        ship.finalizeShip();
        stocks.add(10, 10, 10, 10);
        ship.addStocks(this.stocks);
        Stocks stocksToRemove = new Stocks(0, 10, 10, 10);
        ship.removeStocks(stocksToRemove);
        ship.removeMostPreciousStocks(11);
        assertEquals(0, ship.getStocks().getSpecialRedStocks());
        assertEquals(0, ship.getStocks().getYellowStocks());
        assertEquals(0, ship.getStocks().getGreenStocks());
        assertEquals(0, ship.getStocks().getBlueStocks());
        assertEquals(3, ship.getBatteries());
    }

    //tests when there are exactly the number of red, yellow, green, blue stocks and batteries to pay the penalty
    @Test
    void removeMostPreciousStocks5() {
        ship.fillTheGaps();
        ship.finalizeShip();
        stocks.add(10, 10, 10, 10);
        ship.addStocks(this.stocks);
        Stocks stocksToRemove = new Stocks(0, 10, 10, 10);
        ship.removeStocks(stocksToRemove);
        ship.removeMostPreciousStocks(14);
        assertEquals(0, ship.getStocks().getSpecialRedStocks());
        assertEquals(0, ship.getStocks().getYellowStocks());
        assertEquals(0, ship.getStocks().getGreenStocks());
        assertEquals(0, ship.getStocks().getBlueStocks());
        assertEquals(0, ship.getBatteries());
    }

    //tests when there aren't enough red, yellow, green, blue stocks and batteries to pay the penalty
    @Test
    void removeMostPreciousStocks6() {
        ship.fillTheGaps();
        ship.finalizeShip();
        stocks.add(10, 10, 10, 10);
        ship.addStocks(this.stocks);
        Stocks stocksToRemove = new Stocks(0, 10, 10, 10);
        ship.removeStocks(stocksToRemove);
        assertThrows(OutOfStockException.class, () -> ship.removeMostPreciousStocks(15));
    }

    @Test
    void removeMostPreciousStocks7() {
        ship.fillTheGaps();
        ship.finalizeShip();
        stocks.add(10, 10, 10, 10);
        ship.addStocks(this.stocks);
        //penalty stocks are less than red stocks
        ship.removeMostPreciousStocks(1);
        assertEquals(9, ship.getStocks().getSpecialRedStocks());
    }

    //all cabins have 2 humans
    @Test
    void removeConnectedCrew1() {
        ship.fillTheGaps();
        ship.finalizeShip();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                switch(ship.getShip()[i][j]) {
                    case Cabin cabin:
                        cabin.setHumans(true, true);
                        break;
                    default:
                        break;
                }
            }
        }
        ship.removeConnectedCrew();
        ArrayList<Boolean> humansAfterRemoval = new ArrayList<>(2);
        humansAfterRemoval.add(false);
        humansAfterRemoval.add(true);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                switch(ship.getShip()[i][j]) {
                    case Cabin cabin:
                        assertEquals(humansAfterRemoval, cabin.getHumanEquip());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    //all cabins have 1 human
    @Test
    void removeConnectedCrew2() {
        ship.fillTheGaps();
        ship.finalizeShip();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                switch(ship.getShip()[i][j]) {
                    case Cabin cabin:
                        cabin.setHumans(false, true);
                        break;
                    default:
                        break;
                }
            }
        }
        ship.removeConnectedCrew();
        ArrayList<Boolean> humansAfterRemoval = new ArrayList<>(2);
        humansAfterRemoval.add(false);
        humansAfterRemoval.add(false);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                switch(ship.getShip()[i][j]) {
                    case Cabin cabin:
                        assertEquals(humansAfterRemoval, cabin.getHumanEquip());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    //tests the removeConnectedCrew with aliens
    @Test
    void removeConnectedCrew3() {
        ship.fillTheGaps();
        ship.finalizeShip();
        Cabin brownCabin = (Cabin) ship.getShip()[2][4];
        brownCabin.setBrownAlien(true);

        Cabin purpleCabin = (Cabin) ship.getShip()[2][3];
        purpleCabin.setPurpleAlien(true);

        Cabin cabin = (Cabin) ship.getShip()[3][4];
        cabin.setHumans(true, true);

        ship.removeConnectedCrew();
        ArrayList<Boolean> humansAfterRemoval = new ArrayList<>(2);
        humansAfterRemoval.add(false);
        humansAfterRemoval.add(true);
        assertEquals(humansAfterRemoval, cabin.getHumanEquip());
        assertFalse(purpleCabin.getPurpleAlienEquip());
        assertFalse(brownCabin.getBrownAlienEquip());
    }

    //tests edge cases when there isn't any human/alien to remove
    @Test
    void removeConnectedCrew4() {
        ship.fillTheGaps();
        ship.finalizeShip();
        Cabin brownCabin = (Cabin) ship.getShip()[2][4];
        brownCabin.setBrownAlien(false);

        Cabin purpleCabin = (Cabin) ship.getShip()[2][3];
        purpleCabin.setPurpleAlien(false);

        Cabin cabin = (Cabin) ship.getShip()[3][4];
        cabin.setHumans(false, false);

        ship.removeConnectedCrew();
        ArrayList<Boolean> humansAfterRemoval = new ArrayList<>(2);
        humansAfterRemoval.add(false);
        humansAfterRemoval.add(false);
        assertEquals(humansAfterRemoval, cabin.getHumanEquip());
        assertFalse(purpleCabin.getPurpleAlienEquip());
        assertFalse(brownCabin.getBrownAlienEquip());
    }

    // cabins with humans, purple alien and brown alien
    @Test
    void removeCrewFromCabin1() {
        ship.fillTheGaps();
        ship.finalizeShip();
        for (int i = 0; i < ship.getShip().length; i++) {
            for (int j = 0; j < ship.getShip()[i].length; j++) {
                if (ship.getShip()[i][j].getType().equals(ComponentType.CABIN)) {
                    Cabin cabin = (Cabin) ship.getShip()[i][j];
                    if (!cabin.hasBrownLifeSupport() && !cabin.hasPurpleLifeSupport()) {
                        cabin.setHumans(true, true);
                    }
                    else if (cabin.hasPurpleLifeSupport()) {
                        cabin.setPurpleAlien(true);
                    }
                    else if (cabin.hasBrownLifeSupport()) {
                        cabin.setBrownAlien(true);
                    }
                }
            }
        }
        Cabin cabinWithHumans = (Cabin) ship.getShip()[3][4];
        assertThrows(NotEnoughCrewmatesException.class, () -> ship.removeCrewFromCabin(4, 3, 0, true, false));
        assertThrows(NotEnoughCrewmatesException.class, () -> ship.removeCrewFromCabin(4, 3, 0, false, true));
        assertThrows(NotEnoughCrewmatesException.class, () -> ship.removeCrewFromCabin(4, 3, 0, true, true));
        assertEquals(2, cabinWithHumans.getHumans());
        assertTrue(cabinWithHumans.getHumanEquip().getFirst());
        assertTrue(cabinWithHumans.getHumanEquip().getLast());
        assertFalse(cabinWithHumans.getPurpleAlienEquip());
        assertFalse(cabinWithHumans.getBrownAlienEquip());
        ship.removeCrewFromCabin(4, 3, 2, false, false);
        assertFalse(cabinWithHumans.getHumanEquip().getFirst());
        assertFalse(cabinWithHumans.getHumanEquip().getLast());
        assertEquals(0, cabinWithHumans.getHumans());
        assertFalse(cabinWithHumans.getPurpleAlienEquip());
        assertFalse(cabinWithHumans.getBrownAlienEquip());

        Cabin cabinWithPurpleAlien = (Cabin) ship.getShip()[2][3];
        assertThrows(NotEnoughCrewmatesException.class, () -> ship.removeCrewFromCabin(3, 2, 1, false, false));
        assertThrows(NotEnoughCrewmatesException.class, () -> ship.removeCrewFromCabin(3, 2, 1, false, true));
        assertEquals(0, cabinWithPurpleAlien.getHumans());
        assertFalse(cabinWithPurpleAlien.getHumanEquip().getFirst());
        assertFalse(cabinWithPurpleAlien.getHumanEquip().getLast());
        assertTrue(cabinWithPurpleAlien.getPurpleAlienEquip());
        assertFalse(cabinWithPurpleAlien.getBrownAlienEquip());
        ship.removeCrewFromCabin(3, 2, 0, true, false);
        assertEquals(0, cabinWithPurpleAlien.getHumans());
        assertFalse(cabinWithPurpleAlien.getHumanEquip().getFirst());
        assertFalse(cabinWithPurpleAlien.getHumanEquip().getLast());
        assertFalse(cabinWithPurpleAlien.getPurpleAlienEquip());
        assertFalse(cabinWithPurpleAlien.getBrownAlienEquip());

        Cabin cabinWithBrownAlien = (Cabin) ship.getShip()[2][4];
        assertThrows(NotEnoughCrewmatesException.class, () -> ship.removeCrewFromCabin(3, 2, 1, false, false));
        assertThrows(NotEnoughCrewmatesException.class, () -> ship.removeCrewFromCabin(3, 2, 1, true, false));
        assertEquals(0, cabinWithBrownAlien.getHumans());
        assertFalse(cabinWithBrownAlien.getHumanEquip().getFirst());
        assertFalse(cabinWithBrownAlien.getHumanEquip().getLast());
        assertFalse(cabinWithBrownAlien.getPurpleAlienEquip());
        assertTrue(cabinWithBrownAlien.getBrownAlienEquip());
        ship.removeCrewFromCabin(4, 2, 0, false, true);
        assertFalse(cabinWithBrownAlien.getHumanEquip().getFirst());
        assertFalse(cabinWithBrownAlien.getHumanEquip().getLast());
        assertEquals(0, cabinWithBrownAlien.getHumans());
        assertFalse(cabinWithBrownAlien.getBrownAlienEquip());
        assertFalse(cabinWithBrownAlien.getPurpleAlienEquip());
    }

    // removes humans sequentially, unit by unit; tests the IncompatibleTargetComponentException
    @Test
    void removeCrewFromCabin2() {
        ship.fillTheGaps();
        ship.finalizeShip();
        for (int i = 0; i < ship.getShip().length; i++) {
            for (int j = 0; j < ship.getShip()[i].length; j++) {
                if (ship.getShip()[i][j].getType().equals(ComponentType.CABIN)) {
                    Cabin cabin = (Cabin) ship.getShip()[i][j];
                    if (!cabin.hasBrownLifeSupport() && !cabin.hasPurpleLifeSupport()) {
                        cabin.setHumans(true, true);
                    }
                    else if (cabin.hasPurpleLifeSupport()) {
                        cabin.setPurpleAlien(true);
                    }
                    else if (cabin.hasBrownLifeSupport()) {
                        cabin.setBrownAlien(true);
                    }
                }
            }
        }
        Cabin cabinWithHumans = (Cabin) ship.getShip()[3][4];
        ship.removeCrewFromCabin(4, 3, 1, false, false);
        assertTrue(cabinWithHumans.getHumanEquip().getFirst());
        assertFalse(cabinWithHumans.getHumanEquip().getLast());
        assertEquals(1, cabinWithHumans.getHumans());
        assertFalse(cabinWithHumans.getPurpleAlienEquip());
        assertFalse(cabinWithHumans.getBrownAlienEquip());
        ship.removeCrewFromCabin(4, 3, 1, false, false);
        assertFalse(cabinWithHumans.getHumanEquip().getFirst());
        assertFalse(cabinWithHumans.getHumanEquip().getLast());
        assertEquals(0, cabinWithHumans.getHumans());
        assertFalse(cabinWithHumans.getPurpleAlienEquip());
        assertFalse(cabinWithHumans.getBrownAlienEquip());

        assertThrows(IncompatibleTargetComponent.class, () -> ship.removeCrewFromCabin(0, 0, 1, false, false));
    }

    @Test
    void is_ADJACENCY_legal() {
        ship.fillTheGaps();
        ship.finalizeShip();
        ShipComponent structural = new Structural(Side.BlankSide, Side.UniversalConnector, Side.DoubleConnector, Side.UniversalConnector, "");
        ShipComponent doubleCannon = new Cannon(Side.BlankSide, Side.CannonSpace, Side.BlankSide, Side.DoubleConnector,true, "" );
        ShipComponent shield = new Shield(Side.ShieldProtection, Side.ShieldAndSingleConnector, Side.DoubleConnector, Side.DoubleConnector, "");
        assertFalse(ship.is_ADJACENCY_legal(5, 0, structural));
        assertTrue(ship.is_ADJACENCY_legal(3, 1, structural));
        assertFalse(ship.is_ADJACENCY_legal(3, 1, doubleCannon));
        assertTrue(ship.is_ADJACENCY_legal(3, 1, shield));
    }

    //the ship is well built
    @Test
    void checkShip1() {
        ship.fillTheGaps();
        ship.finalizeShip();
        boolean[][] checkShipResults = ship.checkShip();
        for (int i = 0; i < checkShipResults.length; i++) {
            for (int j = 0; j < checkShipResults[i].length; j++) {
                if (checkShipResults[i][j]) {
                    System.out.println("i: " + i + ", j: " + j + ", checkShipResults[i][j]: " + checkShipResults[i][j]);
                }
                assertFalse(checkShipResults[i][j]);
            }
        }
    }

    // the ship has to be fixed; the following errors are tested:
    // - incompatible connectors nearby
    // - component behind engine
    // - component not connected to any other one
    @Test
    void checkShip2() {
        ShipComponent structural = new Structural(Side.BlankSide, Side.DoubleConnector, Side.BlankSide, Side.BlankSide, "");
        ship.addComponent(structural, 3, 1, false);
        ship.addComponent(structural, 0,3, false);
        ship.addComponent(structural, 6, 2, false);
        ship.fillTheGaps();
        ship.finalizeShip();
        boolean[][] checkShipResults = ship.checkShip();
        for (int i = 0; i < checkShipResults.length; i++) {
            for (int j = 0; j < checkShipResults[i].length; j++) {
                if (i == 1 && j == 2) {
                    assertTrue(checkShipResults[i][j]);
                }
                else if (i == 1 && j == 3) {
                    assertTrue(checkShipResults[i][j]);
                }
                else if (i == 1 && j == 4) {
                    assertTrue(checkShipResults[i][j]);
                }
                else if (i == 2 && j == 0) {
                    assertTrue(checkShipResults[i][j]);
                }
                else if (i == 2 && j == 3) {
                    assertTrue(checkShipResults[i][j]);
                }
                else if (i == 3 && j == 0) {
                    assertTrue(checkShipResults[i][j]);
                }
                else if (i == 2 && j == 6) {
                    assertTrue(checkShipResults[i][j]);
                }
                else assertFalse(checkShipResults[i][j]);
            }
        }
    }

    // the ship has to be fixed; the following errors are tested:
    // - component in front of a cannon
    @Test
    void checkShip3() {
        ShipComponent cannon1 = new Cannon(Side.CannonSpace, Side.DoubleConnector, Side.BlankSide, Side.BlankSide, false, "");
        ship.addComponent(cannon1, 0,3, false);
        ShipComponent cannon2 = new Cannon(Side.BlankSide, Side.SingleConnector, Side.CannonSpace, Side.UniversalConnector, false, "");
        ship.addComponent(cannon2, 3,1, false);
        ship.fillTheGaps();
        ship.finalizeShip();
        boolean[][] checkShipResults = ship.checkShip();
        for (int i = 0; i < checkShipResults.length; i++) {
            for (int j = 0; j < checkShipResults[i].length; j++) {
                if (i == 1 && j == 3) {
                    assertTrue(checkShipResults[i][j]);
                }
                else if (i == 2 && j == 3) {
                    assertTrue(checkShipResults[i][j]);
                }
                else if (i == 2 && j == 0) {
                    assertTrue(checkShipResults[i][j]);
                }
                else if (i == 3 && j == 0) {
                    assertTrue(checkShipResults[i][j]);
                }
                else assertFalse(checkShipResults[i][j]);
            }
        }
    }

    @Test
    void getEligibleCannons() {
        ship.fillTheGaps();
        ship.finalizeShip();
        Projectile meteor = new Projectile(Orientation.SOUTH, ProjectileType.BIG_ASTEROID);
        Projectile meteor1 = new Projectile(Orientation.NORTH, ProjectileType.BIG_ASTEROID);
        Projectile meteor2 = new Projectile(Orientation.WEST, ProjectileType.BIG_ASTEROID);
        Projectile meteor3 = new Projectile(Orientation.EAST, ProjectileType.BIG_ASTEROID);

        ArrayList<Cannon> cannons = ship.getEligibleCannons(meteor, 5);
        ArrayList<Cannon> cannons1 = ship.getEligibleCannons(meteor1, 5);
        ArrayList<Cannon> cannons2 = ship.getEligibleCannons(meteor2, 4);
        ArrayList<Cannon> cannons3 = ship.getEligibleCannons(meteor3, 4);
        ShipComponent doubleCannon = ship.getShip()[1][5];
        //DEBUG
        //System.out.println(doubleCannon.getID());
        //System.out.println(doubleCannon.getNorthSide());
        /*for(ShipComponent c : cannons) {
            System.out.println(c.getID());
        }*/
        //System.out.println(cannons.size());
        assertTrue(cannons.contains(doubleCannon));
        assertFalse(cannons1.contains(doubleCannon));
        assertFalse(cannons2.contains(doubleCannon));
        assertFalse(cannons3.contains(doubleCannon));
    }

    @Test
    void getDoubleCannons() {
        ship.fillTheGaps();
        ship.finalizeShip();
        assertEquals(1, ship.getDoubleCannons());
    }

    @Test
    void getDoubleMotors() {
        ship.fillTheGaps();
        ship.finalizeShip();
        assertEquals(2, ship.getDoubleMotors());
    }

    //method used to test findComponentsConnectedTo() method
    ArrayList<ShipComponent[][]> breakComponent (int x, int y) {
        ArrayList<ShipComponent[][]> brokenShipPieces = new ArrayList<>();
        boolean componentAlreadyScanned;
        int real_x = x-4;
        int real_y = y-5;
        if(real_y<0||real_y>=ship.getShip().length||real_x<0||real_x>=ship.getShip()[real_y].length)
            throw new IllegalPositionException("The provided coordinates are outside of the index range");

        if(ship.isUnavailableSlot(real_x,real_y))
            return brokenShipPieces;

        ship.getShip()[real_y][real_x] = new UnavailableSlot();

        //scan every component left
        for(int cy = 0; cy < ship.getShip().length; cy++) {
            for (int cx = 0; cx < ship.getShip()[cy].length; cx++) {
                //check if it's already part of one of the pieces of the broken ship
                componentAlreadyScanned = false;
                for (ShipComponent[][] brokenPiece : brokenShipPieces) {
                    //purposely check for instance correspondence
                    if (brokenPiece[cy][cx] == ship.getShip()[cy][cx]) {
                        componentAlreadyScanned = true;
                        break;
                    }
                }
                if (!componentAlreadyScanned && !ship.isUnavailableSlot(cx, cy)) {
                    //initialized to all nulls by default
                    ShipComponent[][] newBrokenPiece = new ShipComponent[ship.getShip().length][ship.getShip()[0].length];
                    ship.findComponentsConnectedTo(ship.getShip(), cx, cy, newBrokenPiece);
                    brokenShipPieces.add(newBrokenPiece);
                }
                //else go to next component
            }
        }

        return brokenShipPieces;
    }

    @Test
    void findComponentsConnectedTo() {
        ship.fillTheGaps();
        ship.finalizeShip();
        ArrayList<ShipComponent[][]> brokenShipPieces = new ArrayList<>();
        brokenShipPieces = breakComponent(7, 8);
        assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[3][3].getType());
        brokenShipPieces = breakComponent(7, 7);
        brokenShipPieces = ship.getBrokenPieces();
        assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[2][3].getType());

        ShipComponent[][] part1 = new ShipComponent[5][7];
        ShipComponent[][] part2 = new ShipComponent[5][7];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                part1[i][j] = ship.getShip()[i][j] instanceof UnavailableSlot ? null : ship.getShip()[i][j];
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 4; j < 7; j++) {
                part2[i][j] = ship.getShip()[i][j] instanceof UnavailableSlot ? null : ship.getShip()[i][j];
            }
        }

    //    ShipStringBuilder SSB = new ShipStringBuilder(7,5,20,2.7,4,5);
    //    System.out.println(SSB.buildShipString(ship.getShip()));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                if (brokenShipPieces.get(0)[i][j] == null) {
                    System.out.println(i + " " + j + " " + part1[i][j] + "non presente");
                }
                if (brokenShipPieces.get(1) == null) {
                    System.out.println("non presente");
                }
                if (brokenShipPieces.get(1)[i][j+4] == null) {
                    System.out.println(i + " " + j + " " + part2[i][j] + " non presente");
                }
                if(part1[i][j] == null){
                    System.out.println("x="+j+"  y="+i);
                    assertNull(brokenShipPieces.get(1)[i][j]);
                }
                else {
                    assertEquals(part1[i][j].getType(), brokenShipPieces.getFirst()[i][j].getType());
                    assertEquals(part1[i][j].getNorthSide(), brokenShipPieces.getFirst()[i][j].getNorthSide());
                    assertEquals(part1[i][j].getSouthSide(), brokenShipPieces.getFirst()[i][j].getSouthSide());
                    assertEquals(part1[i][j].getWestSide(), brokenShipPieces.getFirst()[i][j].getWestSide());
                    assertEquals(part1[i][j].getEastSide(), brokenShipPieces.getFirst()[i][j].getEastSide());
                }

                System.out.println(brokenShipPieces.getFirst().length);
                System.out.println(brokenShipPieces.getFirst()[0].length);

                System.out.println(brokenShipPieces.getFirst()[0][4] == null ? null : brokenShipPieces.getFirst()[0][4].getType());

                if(part2[i][j] == null){
                    System.out.println("x="+j+"  y="+i);
                    assertNull(brokenShipPieces.get(1)[i][j]);
                }
                else {
                    assertEquals(part2[i][j].getType(), brokenShipPieces.get(1)[i][j].getType());
                    assertEquals(part2[i][j].getNorthSide(), brokenShipPieces.get(1)[i][j].getNorthSide());
                    assertEquals(part2[i][j].getSouthSide(), brokenShipPieces.get(1)[i][j].getSouthSide());
                    assertEquals(part2[i][j].getWestSide(), brokenShipPieces.get(1)[i][j].getWestSide());
                    assertEquals(part2[i][j].getEastSide(), brokenShipPieces.get(1)[i][j].getEastSide());
                }
            }
        }

        // check sides and type for each component
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++){
                assertEquals(part1[i][j], brokenShipPieces.get(0)[i][j]);
                assertEquals(part2[i][j], brokenShipPieces.get(1)[i][j]);
            }
        }

    }

    // tested the method which returns the coordinates of the hit component, from all 4 possible directions
    @Test
    void getHitComponent() {
        ship.fillTheGaps();
        ship.finalizeShip();
        int[] hitCoordinates = new int[2];
        Projectile projectile1 = new Projectile(Orientation.SOUTH, ProjectileType.BIG_ASTEROID);
        Projectile projectile2 = new Projectile(Orientation.NORTH, ProjectileType.BIG_ASTEROID);
        Projectile projectile3 = new Projectile(Orientation.WEST, ProjectileType.BIG_ASTEROID);
        Projectile projectile4 = new Projectile(Orientation.EAST, ProjectileType.BIG_ASTEROID);

        hitCoordinates = ship.getHitComponent(projectile1, 3, hitCoordinates);
        assertEquals(3, hitCoordinates[0]);
        assertEquals(2, hitCoordinates[1]);

        hitCoordinates = ship.getHitComponent(projectile2, 4, hitCoordinates);
        assertEquals(4, hitCoordinates[0]);
        assertEquals(4, hitCoordinates[1]);

        hitCoordinates = ship.getHitComponent(projectile3, 2, hitCoordinates);
        assertEquals(5, hitCoordinates[0]);
        assertEquals(2, hitCoordinates[1]);

        hitCoordinates = ship.getHitComponent(projectile4, 2, hitCoordinates);
        assertEquals(0, hitCoordinates[0]);
        assertEquals(2, hitCoordinates[1]);

        hitCoordinates = ship.getHitComponent(projectile1, 7, hitCoordinates);
        assertEquals(-1, hitCoordinates[0]);
        assertEquals(-1, hitCoordinates[1]);
    }

    @Test
    void finalizeShip() {
        ship.fillTheGaps();
        Stocks expectedStocks = new Stocks(0,0,0,0);
        //this isn't a proper way to test this method, it's used to check only the last method InitializeShipAttributesFromComponent
        ship.connectLifeSupports();
        for (int i = 0; i < ship.getShip().length; i++) {
            for (int j = 0; j < ship.getShip()[i].length; j++) {
                if (ship.getShip()[i][j].getType().equals(ComponentType.CABIN)) {
                    Cabin cabin = (Cabin) ship.getShip()[i][j];
                    if (!cabin.hasBrownLifeSupport() && !cabin.hasPurpleLifeSupport()) {
                        cabin.setHumans(true, true);
                    }
                    else if (cabin.hasPurpleLifeSupport()) {
                        cabin.setPurpleAlien(true);
                    }
                    else if (cabin.hasBrownLifeSupport()) {
                        cabin.setBrownAlien(true);
                    }
                }
            }
        }

        ship.initializeShipAttributesFromComponents();
        //testing fillTheGaps()
        for (int i = 0; i < ship.getShip().length; i++) {
            for (int j = 0; j < ship.getShip()[i].length; j++) {
                if (i == 0 && j == 0)  {
                    assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
                else if (i == 0 && j == 1)  {
                    assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
                else if (i == 0 && j == 3)  {
                    assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
                else if (i == 0 && j == 5)  {
                    assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
                else if (i == 0 && j == 6)  {
                    assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
                else if (i == 1 && j == 0)  {
                    assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
                else if (i == 1 && j == 3)  {
                    assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
                else if (i == 1 && j == 6)  {
                    assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
                else if (i == 2 && j == 6)  {
                    assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
                else if (i == 3 && j == 0) {
                    assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
                else if (i == 4 && j == 2) {
                    assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
                else if (i == 4 && j == 3)  {
                    assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
                else if (i == 4 && j == 5) {
                    assertEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
                else {
                    assertNotEquals(ComponentType.UNAVAILABLE_SLOT, ship.getShip()[i][j].getType());
                }
            }
        }

        //testing connectLifeSupport()
        Cabin cabin1 = (Cabin) ship.getShip()[2][3];
        assertTrue(cabin1.hasPurpleLifeSupport());
        assertFalse(cabin1.hasBrownLifeSupport());
        Cabin cabin2 = (Cabin) ship.getShip()[2][4];
        assertFalse(cabin2.hasPurpleLifeSupport());
        assertTrue(cabin2.hasBrownLifeSupport());
        Cabin cabin3 = (Cabin) ship.getShip()[3][4];
        assertFalse(cabin3.hasPurpleLifeSupport());
        assertFalse(cabin3.hasBrownLifeSupport());

        //testing initializeShipAttributesFromComponents()
        assertEquals(expectedStocks.getSpecialRedStocks(), ship.getStocks().getSpecialRedStocks());
        assertEquals(expectedStocks.getBlueStocks(), ship.getStocks().getBlueStocks());
        assertEquals(expectedStocks.getGreenStocks(), ship.getStocks().getGreenStocks());
        assertEquals(expectedStocks.getYellowStocks(), ship.getStocks().getYellowStocks());
        assertEquals(4, ship.getBatteries());
        assertEquals(4, ship.getStaticFirePower());
        assertEquals(5, ship.getStaticMotorPower());
        assertEquals(2, ship.getHumans());
        // there should be an alien, but it isn't recognised in the ship
        assertTrue(ship.isBrownAlienPresent());
        assertTrue(ship.isPurpleAlienPresent());
    }

    @Test
    void bookingComponentsMethods() {
        ShipComponent structural1 = new Structural(Side.BlankSide, Side.SingleConnector, Side.UniversalConnector, Side.DoubleConnector, "");
        ShipComponent structural2 = new Structural(Side.BlankSide, Side.SingleConnector, Side.UniversalConnector, Side.DoubleConnector, "");

        assertTrue(ship.getBookedComponents().isEmpty());
        assertEquals(0, ship.getGarbageHeap());

        ship.bookComponent(structural1);
        assertEquals(1, ship.getBookedComponents().size());
        assertEquals(structural1, ship.getBookedComponent(0));
        assertThrows(IndexOutOfBoundsException.class, () -> ship.getBookedComponent(1));
        ship.bookComponent(structural2);
        assertEquals(2, ship.getBookedComponents().size());
        assertEquals(structural2, ship.getBookedComponent(1));
        assertThrows(BookingSlotsFullException.class, () -> ship.bookComponent(structural1));

        ship.removeBookedComponent(0);
        assertEquals(1, ship.getBookedComponents().size());
        assertEquals(structural2, ship.getBookedComponent(0));
        ship.removeBookedComponent(0);
        assertEquals(0, ship.getBookedComponents().size());
        assertThrows(IndexOutOfBoundsException.class, () -> ship.removeBookedComponent(0));

        ship.bookComponent(structural1);
        ship.bookComponent(structural2);
        ship.addBookedComponentsToGarbage();
        assertEquals(0, ship.getBookedComponents().size());
        assertEquals(2, ship.getGarbageHeap());
    }

    @Test
    void testClone(){
        ship.finalizeShip();
        ShipDashboard cloned;
        cloned =ship.clone();

        assertEquals(ship.getShip(), cloned.getShip());
    }

    @Test
    void testRemoveAllCrew(){
        ship.finalizeShip();
        Cabin brownCabin = (Cabin) ship.getShip()[2][4];
        brownCabin.setBrownAlien(true);

        Cabin purpleCabin = (Cabin) ship.getShip()[2][3];
        purpleCabin.setPurpleAlien(true);
        Cabin cabin = (Cabin) ship.getShip()[3][4];
        cabin.setHumans(true, true);
        finalizeShip();

        //before removal are present->
        assertFalse(ship.getHumans()==0);
        assertTrue(ship.isBrownAlienPresent());
        assertTrue(ship.isPurpleAlienPresent());

        ship.removeAllCrewmates();

        assertTrue(ship.getHumans()==0);
        assertFalse(ship.isBrownAlienPresent());
        assertFalse(ship.isPurpleAlienPresent());
    }

}