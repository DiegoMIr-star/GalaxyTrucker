package Controller.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManageProjectileTest {
    private ManageProjectile object1;

    @BeforeEach
    void setUp() {
        object1 = new ManageProjectile();
    }

    @Test
    void testToString() {
        assertEquals("ManageProjectile state", object1.toString());
    }

    @Test
    void testClone() {
        ManageProjectile clonedObject = object1.clone();
        assertNotSame(object1, clonedObject);
    }

}