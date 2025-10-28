package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InitializationStateTest {
    private InitializationState object1;

    @BeforeEach
    void setUp() {
        object1 = new InitializationState();
    }

    @Test
    void testToString() {
        assertEquals("InitializationState", object1.toString());
    }

    @Test
    void testClone() {
        InitializationState clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }

}