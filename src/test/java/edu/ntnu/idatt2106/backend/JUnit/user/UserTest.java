package edu.ntnu.idatt2106.backend.JUnit.user;

import edu.ntnu.idatt2106.backend.model.fridge.Fridge;
import edu.ntnu.idatt2106.backend.model.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", 1234567890L, "Test Street 123");
    }


    @Test
    void testEqualsAndHashCode() {
        User user2 = new User("test@example.com", 9876543210L, "Another Street 456");

        assertEquals(user, user2);
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void testNotEquals() {
        User user2 = new User("different@example.com", 9876543210L, "Another Street 456");

        assertNotEquals(user, user2);
    }

    @Test
    void testFridgeAssociation() {
        Fridge fridge = new Fridge();
        user.setFridge(fridge);
        fridge.setUser(user);

        assertEquals(fridge, user.getFridge());
        assertEquals(user, fridge.getUser());
    }
}
