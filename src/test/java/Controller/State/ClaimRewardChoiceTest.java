package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClaimRewardChoiceTest {
    private ClaimRewardChoice object1;

    @BeforeEach
    void setUp() {
        object1 = new ClaimRewardChoice();
    }

    @Test
    void testToString() {
        assertEquals("ClaimRewardChoice state", object1.toString());
    }

    @Test
    void testClone() {
        ClaimRewardChoice clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }

}