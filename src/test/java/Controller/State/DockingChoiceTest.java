package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DockingChoiceTest {
    private DockingChoice object1;

    @BeforeEach
    void setUp() {
        object1 = new DockingChoice();
    }

    @Test
    void testToString() {
        assertEquals("DockingChoice state", object1.toString());
    }

    @Test
    void testClone() {
        DockingChoice clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }
}