package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardDrawingTest {
    private CardDrawing object1;

    @BeforeEach
    void setUp() {
        object1 = new CardDrawing();
    }

    @Test
    void testToString() {
        assertEquals("Card Drawing state", object1.toString());
    }

    @Test
    void testClone() {
        CardDrawing clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }
}