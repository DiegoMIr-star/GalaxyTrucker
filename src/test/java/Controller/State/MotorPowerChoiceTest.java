package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MotorPowerChoiceTest {

    private MotorPowerChoice object1;

    @BeforeEach
    void setUp() {
        object1 = new MotorPowerChoice();
    }

    @Test
    void testToString() {
        assertEquals("MotorPowerChoice state", object1.toString());
    }

    @Test
    void testClone() {
        MotorPowerChoice clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }
}