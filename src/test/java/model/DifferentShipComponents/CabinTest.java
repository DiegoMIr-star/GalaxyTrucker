package model.DifferentShipComponents;

import model.exceptions.NotAllowedAlienException;
import model.exceptions.NotAllowedHumanException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CabinTest {
    private Cabin cabin;
    @BeforeEach
    void setUp() {
        cabin=new Cabin(false,Side.BlankSide,Side.ShieldProtection,Side.UniversalConnector,Side.SingleConnector,"");
    }

    @Test
    void counterClockwiseRotation() {
        cabin.counterClockwiseRotation(1);
        assertEquals(Side.ShieldProtection,cabin.getSides().getFirst());
        assertEquals(Side.UniversalConnector,cabin.getSides().get(1));
        assertEquals(Side.SingleConnector,cabin.getSides().get(2));
        assertEquals(Side.BlankSide,cabin.getSides().get(3));
    }

    @Test
    void counterClockwiseRotation2() {
        cabin.counterClockwiseRotation(2);
        assertEquals(Side.UniversalConnector,cabin.getSides().getFirst());
        assertEquals(Side.SingleConnector,cabin.getSides().get(1));
        assertEquals(Side.BlankSide,cabin.getSides().get(2));
        assertEquals(Side.ShieldProtection,cabin.getSides().get(3));
    }

    @Test
    void clockwiseRotation() {
        cabin.clockwiseRotation(1);
        assertEquals(Side.SingleConnector,cabin.getSides().getFirst());
        assertEquals(Side.BlankSide,cabin.getSides().get(1));
        assertEquals(Side.ShieldProtection,cabin.getSides().get(2));
        assertEquals(Side.UniversalConnector,cabin.getSides().get(3));
    }

    //old code
    /*
    @Test
    void clockwiseRotationOld() {
        cabin.clockwiseRotation();
        assertEquals(Side.SingleConnector,cabin.getSides().getFirst());
        assertEquals(Side.BlankSide,cabin.getSides().get(1));
        assertEquals(Side.ShieldProtection,cabin.getSides().get(2));
        assertEquals(Side.UniversalConnector,cabin.getSides().get(3));
    }
    */
    @Test
    void clockwiseRotation2() {
        cabin.clockwiseRotation(2);
        assertEquals(Side.UniversalConnector,cabin.getSides().getFirst());
        assertEquals(Side.SingleConnector,cabin.getSides().get(1));
        assertEquals(Side.BlankSide,cabin.getSides().get(2));
        assertEquals(Side.ShieldProtection,cabin.getSides().get(3));
    }

    @Test
    void getSides() {
        //Test passed!
        assertEquals(Side.BlankSide,cabin.getSides().getFirst());
        assertEquals(Side.ShieldProtection,cabin.getSides().get(1));
        assertEquals(Side.UniversalConnector,cabin.getSides().get(2));
        assertEquals(Side.SingleConnector,cabin.getSides().get(3));
    }

    @Test
    void getHumanEquip() {
        //Test passed!
        assertEquals(false,cabin.getHumanEquip().getFirst());
        assertEquals(false,cabin.getHumanEquip().getFirst());
    }

    @Test
    void getBrownAlienEquip() {
        //Test passed!
        assertFalse(cabin.getBrownAlienEquip());
    }

    @Test
    void getPurpleAlienEquip() {
        //Test passed!
        assertFalse(cabin.getPurpleAlienEquip());
    }

    @Test
    void getBrownOddBit() {
        //Test passed!
        assertFalse(cabin.hasBrownLifeSupport());
    }

    @Test
    void getPurpleOddBit() {
        //Test passed!
        assertFalse(cabin.hasPurpleLifeSupport());
    }

    @Test
    void setHumans() {
        //Test passed!
        //Check if the method can set correctly the humans
        cabin.setHumans(true,true);
        assertTrue(cabin.getHumanEquip().getFirst());
        assertTrue(cabin.getHumanEquip().get(1));
        //Check if humans are correctly deleted
        cabin.removeHuman();
        assertTrue(cabin.getHumanEquip().getFirst());
        assertFalse(cabin.getHumanEquip().get(1));
        cabin.removeHuman();
        assertFalse(cabin.getHumanEquip().getFirst());
        assertFalse(cabin.getHumanEquip().get(1));
        //Check if the aliens are correctly inserted
        cabin.setBrownLifeSupport(true);
        cabin.setBrownAlien(true);
        assertTrue(cabin.getBrownAlienEquip());
        //Check if the exception is correctly thrown
        Exception exception=assertThrows(NotAllowedHumanException.class,()-> cabin.setHumans(true,true));
        assertEquals("Trying to set humans where there's an alien.",exception.getMessage());
    }

    @Test
    void setBrownAlien() {
        //Test passed!
        //Check the exception when tried to insert a crown alien without support
        Exception exception=assertThrows(NotAllowedAlienException.class,()-> cabin.setBrownAlien(true));
        assertEquals("Trying to set alien without life support.",exception.getMessage());
        //Check if the odd bit is correctly inserted and also humans
        cabin.setBrownLifeSupport(true);
        assertTrue(cabin.hasBrownLifeSupport());
        cabin.setHumans(true,true);
        assertTrue(cabin.getHumanEquip().getFirst());
        assertTrue(cabin.getHumanEquip().get(1));
        //Check if the exception is correctly thrown
        Exception exception1=assertThrows(NotAllowedAlienException.class,()-> cabin.setBrownAlien(true));
        assertEquals("Trying to set alien where is not allowed.",exception1.getMessage());
        //Check if humans are correctly deleted
        cabin.removeHuman();
        assertTrue(cabin.getHumanEquip().getFirst());
        assertFalse(cabin.getHumanEquip().get(1));
        cabin.removeHuman();
        assertFalse(cabin.getHumanEquip().getFirst());
        assertFalse(cabin.getHumanEquip().get(1));
        //Check the alien
        cabin.setBrownAlien(true);
        assertTrue(cabin.getBrownAlienEquip());
    }

    @Test
    void setPurpleAlien() {
        //Test passed!
        //Check the exception when tried to insert a crown alien without support
        Exception exception=assertThrows(NotAllowedAlienException.class,()-> cabin.setPurpleAlien(true));
        assertEquals("Trying to set alien without life support.",exception.getMessage());
        //Check if the odd bit is correctly inserted and also humans
        cabin.setPurpleLifeSupport(true);
        assertTrue(cabin.hasPurpleLifeSupport());
        cabin.setHumans(true,true);
        assertTrue(cabin.getHumanEquip().getFirst());
        assertTrue(cabin.getHumanEquip().get(1));
        //Check if the exception is correctly thrown
        Exception exception1=assertThrows(NotAllowedAlienException.class,()-> cabin.setPurpleAlien(true));
        assertEquals("Trying to set alien where is not allowed.",exception1.getMessage());
        //Check if humans are correctly deleted
        cabin.removeHuman();
        assertTrue(cabin.getHumanEquip().getFirst());
        assertFalse(cabin.getHumanEquip().get(1));
        cabin.removeHuman();
        assertFalse(cabin.getHumanEquip().getFirst());
        assertFalse(cabin.getHumanEquip().get(1));
        //Check the alien
        cabin.setPurpleAlien(true);
        assertTrue(cabin.getPurpleAlienEquip());
    }

    @Test
    void setBrownLifeSupport() {
        //Test passed!
        assertFalse(cabin.hasBrownLifeSupport());
        cabin.setBrownLifeSupport(true);
        assertTrue(cabin.hasBrownLifeSupport());
    }

    @Test
    void setPurpleLifeSupport() {
        //Test passed!
        assertFalse(cabin.hasPurpleLifeSupport());
        cabin.setPurpleLifeSupport(true);
        assertTrue(cabin.hasPurpleLifeSupport());
    }

    @Test
    void removeHuman() {
        //Test passed!
        //Set humans and check they are really there
        cabin.setHumans(true,true);
        assertTrue(cabin.getHumanEquip().getFirst());
        assertTrue(cabin.getHumanEquip().get(1));
        //Check one human removed
        cabin.removeHuman();
        assertTrue(cabin.getHumanEquip().getFirst());
        assertFalse(cabin.getHumanEquip().get(1));
        //Check there's no human
        cabin.removeHuman();
        assertFalse(cabin.getHumanEquip().getFirst());
        assertFalse(cabin.getHumanEquip().get(1));
    }

    @Test
    void removeAlien() {
        //Test passed!
        //Check there are no aliens
        assertFalse(cabin.getBrownAlienEquip());
        assertFalse(cabin.getPurpleAlienEquip());
        //Check if the alien is good initialised
        cabin.setBrownLifeSupport(true);
        cabin.setBrownAlien(true);
        assertTrue(cabin.getBrownAlienEquip());
        assertFalse(cabin.getPurpleAlienEquip());
        //Remove and set the other alien
        cabin.removeAlien();
        cabin.setPurpleLifeSupport(true);
        cabin.setPurpleAlien(true);
        assertFalse(cabin.getBrownAlienEquip());
        assertTrue(cabin.getPurpleAlienEquip());
        //Remove and check
        cabin.removeAlien();
        assertFalse(cabin.getPurpleAlienEquip());
        assertFalse(cabin.getBrownAlienEquip());
    }
}