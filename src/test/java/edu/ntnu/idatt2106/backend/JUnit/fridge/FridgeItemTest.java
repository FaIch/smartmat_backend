package edu.ntnu.idatt2106.backend.JUnit.fridge;

import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FridgeItemTest {

    private FridgeItem fridgeItem;
    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item(1L, "TestItem", "Short description", Category.FISH, 100.0, 5.0, "TestImage");
        fridgeItem = new FridgeItem(1, LocalDate.now().plusDays(7), item);
    }

    @Test
    void testNoArgsConstructor() {
        FridgeItem emptyFridgeItem = new FridgeItem();
        assertNull(emptyFridgeItem.getFridge());
        assertEquals(0, emptyFridgeItem.getQuantity());
        assertNull(emptyFridgeItem.getExpirationDate());
        assertNull(emptyFridgeItem.getItem());
    }

    @Test
    void testAllArgsConstructor() {
        assertEquals(1, fridgeItem.getQuantity());
        assertEquals(LocalDate.now().plusDays(7), fridgeItem.getExpirationDate());
        assertEquals(item, fridgeItem.getItem());
    }

    @Test
    void testSettersAndGetters() {
        fridgeItem.setId(1L);
        fridgeItem.setQuantity(2);
        fridgeItem.setExpirationDate(LocalDate.now().plusDays(14));

        assertEquals(1L, fridgeItem.getId());
        assertEquals(2, fridgeItem.getQuantity());
        assertEquals(LocalDate.now().plusDays(14), fridgeItem.getExpirationDate());
    }
}
