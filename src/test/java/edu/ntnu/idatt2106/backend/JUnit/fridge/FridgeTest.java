package edu.ntnu.idatt2106.backend.JUnit.fridge;

import edu.ntnu.idatt2106.backend.model.fridge.Fridge;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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

        Item item = new Item(1L, "TestItem", "Short description", Category.FISH, 100.0, 5.0, "TestImage");
        fridgeItem = new FridgeItem(1, LocalDate.now().plusDays(7), item);
        fridge.addFridgeItem(fridgeItem);
    }

    @Test
    void testNoArgsConstructor() {
        Fridge emptyFridge = new Fridge();
        assertNull(emptyFridge.getUser());
        assertTrue(emptyFridge.getFridgeItems().isEmpty());
    }

    @Test
    void testAddFridgeItem() {
        assertEquals(1, fridge.getFridgeItems().size());
        assertEquals(fridgeItem, fridge.getFridgeItems().get(0));
        assertEquals(fridge, fridgeItem.getFridge());
    }

    @Test
    void testRemoveFridgeItem() {
        fridge.removeFridgeItem(fridgeItem);
        assertTrue(fridge.getFridgeItems().isEmpty());
        assertNull(fridgeItem.getFridge());
    }

    @Test
    void testEquals() {
        Fridge fridge2 = new Fridge();
        fridge2.setUser(user);
        fridge2.addFridgeItem(fridgeItem);
        assertEquals(fridge, fridge2);

        fridge2.setUser(new User("different@example.com", 9876543210L, "Different Address"));

        assertNotEquals(fridge, fridge2);
    }
}