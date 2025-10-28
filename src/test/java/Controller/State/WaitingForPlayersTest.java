package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaitingForPlayersTest {
    private WaitingForPlayers object1;

    @BeforeEach
    void setUp() {
        object1 = new WaitingForPlayers();
    }

    @Test
    void testToString() {
        assertEquals("WaitingForPlayers state", object1.toString());
    }

    @Test
    void testClone() {
        WaitingForPlayers clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }
}