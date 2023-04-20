package edu.ntnu.idatt2106.backend.user;

import edu.ntnu.idatt2106.backend.model.fridge.Fridge;
import edu.ntnu.idatt2106.backend.model.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class UserJUnitTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", 1234567890L, "Test Street 123");
    }

    @Test
    void testAddSubUser() {
        SubUser subUser = new SubUser();
        user.addSubUser(subUser);

        assertEquals(1, user.getSubUsers().size());
        assertTrue(user.getSubUsers().contains(subUser));
        assertEquals(user, subUser.getMainUser());
    }

    @Test
    void testRemoveSubUser() {
        SubUser subUser = new SubUser();
        user.addSubUser(subUser);
        user.removeSubUser(subUser);

        assertEquals(0, user.getSubUsers().size());
        assertFalse(user.getSubUsers().contains(subUser));
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
