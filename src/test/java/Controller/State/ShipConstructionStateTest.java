package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipConstructionStateTest {

    private ShipConstructionState object1;

    @BeforeEach
    void setUp() {
        object1 = new ShipConstructionState();
    }

    @Test
    void testToString() {
        assertEquals("ShipsConstructionState", object1.toString());
    }

    @Test
    void testClone() {
        ShipConstructionState clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }
}