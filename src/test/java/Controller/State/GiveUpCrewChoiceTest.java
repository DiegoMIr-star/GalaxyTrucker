package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GiveUpCrewChoiceTest {
    private GiveUpCrewChoice object1;

    @BeforeEach
    void setUp() {
        object1 = new GiveUpCrewChoice();
    }

    @Test
    void testToString() {
        assertEquals("GiveUpCrewChoice state", object1.toString());
    }

    @Test
    void testClone() {
        GiveUpCrewChoice clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }
}