package edu.ntnu.idatt2106.backend.JUnit.fridge;

import edu.ntnu.idatt2106.backend.model.fridge.Fridge;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.item.Unit;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FridgeTest {

    private Fridge fridge;
    private User user;
    private FridgeItem fridgeItem;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", 1234567890L, "Test Address");
        fridge = new Fridge();
        fridge.setUser(user);

        Item item = new Item("TestItem", "Short description", Category.FISH, 100.0, 5.0, "TestImage", Unit.ITEM, 10);
        fridgeItem = new FridgeItem(1, LocalDate.now().plusDays(7), item);
        fridge.addFridgeItem(fridgeItem);
    }

    @Test
    void allArgsConstructor() {
        Fridge fridge = new Fridge(5L, user);
        assertEquals(5L, fridge.getId());
        assertEquals(user, fridge.getUser());
    }

    @Test
    void testNoArgsConstructor() {
        Fridge emptyFridge = new Fridge();
        assertNull(emptyFridge.getUser());
    }

    @Test
    void testAddFridgeItem() {
        assertEquals(fridge, fridgeItem.getFridge());
    }

    @Test
    void testRemoveFridgeItem() {
        fridge.removeFridgeItem(fridgeItem);
        assertNull(fridgeItem.getFridge());
    }

    @Test
    void testEqualsAndHashCode() {
        Fridge fridge2 = new Fridge();
        fridge2.setUser(user);
        fridge2.addFridgeItem(fridgeItem);

        // Both fridges have the same user and fridgeItems, but different IDs (null)
        assertEquals(fridge, fridge2);
        assertEquals(fridge.hashCode(), fridge2.hashCode());

        // Setting the same ID for both fridges
        fridge.setId(1L);
        fridge2.setId(1L);

        assertEquals(fridge, fridge2);
        assertEquals(fridge.hashCode(), fridge2.hashCode());

        // Changing the user for one of the fridges
        fridge2.setUser(new User("different@example.com", 9876543210L, "Different Address"));

        assertNotEquals(fridge, fridge2);
        assertNotEquals(fridge.hashCode(), fridge2.hashCode());
    }
}