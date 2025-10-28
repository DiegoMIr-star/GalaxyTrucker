package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PLANETS_LandingChoiceTest {

    private PLANETS_LandingChoice object1;

    @BeforeEach
    void setUp() {
        object1 = new PLANETS_LandingChoice();
    }

    @Test
    void testToString() {
        assertEquals("PLANETS_LandingChoice state", object1.toString());
    }

    @Test
    void testClone() {
        PLANETS_LandingChoice clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }
}