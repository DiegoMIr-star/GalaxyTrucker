package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToBeFixedAndFixingShipsTest {
    private ToBeFixedAndFixingShips object1;

    @BeforeEach
    void setUp() {
        object1 = new ToBeFixedAndFixingShips();
    }

    @Test
    void testToString() {
        assertEquals("ToBeFixedAndFixingShips state", object1.toString());
    }

    @Test
    void testClone() {
        ToBeFixedAndFixingShips clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }
}