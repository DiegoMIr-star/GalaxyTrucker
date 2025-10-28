package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddAndRearrangeStocksTest {
    private AddAndRearrangeStocks object1;
    @BeforeEach
    void setUp() {
        object1 = new AddAndRearrangeStocks();
    }

    @Test
    void testToString() {
        assertEquals("AddAndRearrangeStocks state", object1.toString());
    }

    @Test
    void testClone() {
        AddAndRearrangeStocks clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }
}