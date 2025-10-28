package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FirePowerChoiceTest {
    private FirePowerChoice object1;

    @BeforeEach
    void setUp() {
        object1 = new FirePowerChoice();
    }

    @Test
    void testToString() {
        assertEquals("FirePowerChoice state", object1.toString());
    }

    @Test
    void testClone() {
        FirePowerChoice clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }

}