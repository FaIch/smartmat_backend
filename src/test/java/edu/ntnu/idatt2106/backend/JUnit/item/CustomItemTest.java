package edu.ntnu.idatt2106.backend.JUnit.item;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.CustomItem;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CustomItemTest {
    private CustomItem customItem;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", 1234567890L, "Test Address");
        customItem = new CustomItem("TestCustomItem", 2.0, Category.MEAT, user);
    }

    @Test
    void testNoArgsConstructor() {
        CustomItem emptyCustomItem = new CustomItem();
        assertNull(emptyCustomItem.getName());
        assertEquals(0.0, emptyCustomItem.getWeight());
        assertNull(emptyCustomItem.getCategory());
        assertNull(emptyCustomItem.getUser());
    }

    @Test
    void testAllArgsConstructor() {
        assertEquals("TestCustomItem", customItem.getName());
        assertEquals(2.0, customItem.getWeight());
        assertEquals(Category.MEAT, customItem.getCategory());
        assertEquals(user, customItem.getUser());
    }

    @Test
    void testSettersAndGetters() {
        customItem.setId(1L);
        customItem.setName("UpdatedCustomItem");
        customItem.setWeight(3.0);
        customItem.setCategory(Category.VEGETABLES);

        assertEquals(1L, customItem.getId());
        assertEquals("UpdatedCustomItem", customItem.getName());
        assertEquals(3.0, customItem.getWeight());
        assertEquals(Category.VEGETABLES, customItem.getCategory());
    }

    @Test
    void testUserRelationship() {
        User newUser = new User("newtest@example.com", 9876543210L, "New Test Address");
        customItem.setUser(newUser);
        assertEquals(newUser, customItem.getUser());
    }
}
