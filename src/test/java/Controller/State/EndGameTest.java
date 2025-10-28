package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndGameTest {
    private EndGame object1;

    @BeforeEach
    void setUp() {
        object1 = new EndGame();
    }

    @Test
    void testToString() {
        assertEquals("End Game state", object1.toString());
    }

    @Test
    void testClone() {
        EndGame clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }

}