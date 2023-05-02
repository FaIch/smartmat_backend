package edu.ntnu.idatt2106.backend.JUnit.fridge;

import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.item.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FridgeItemTest {

    private FridgeItem fridgeItem;

    @BeforeEach
    void setUp() {
        Item item = new Item("TestItem", "Short description", Category.FISH, 100.0, 5.0, "TestImage", Unit.ITEM, 10);
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
    void testSettersAndGetters() {
        fridgeItem.setId(1L);
        fridgeItem.setQuantity(2);
        fridgeItem.setExpirationDate(LocalDate.now().plusDays(14));

        assertEquals(1L, fridgeItem.getId());
        assertEquals(2, fridgeItem.getQuantity());
        assertEquals(LocalDate.now().plusDays(14), fridgeItem.getExpirationDate());
    }

    @Test
    void testEqualsAndHashCode() {
        Item item1 = new Item("TestItem", "Short description", Category.FISH, 100.0, 5.0, "TestImage", Unit.ITEM, 10);

        FridgeItem fridgeItem1 = new FridgeItem(1, LocalDate.now().plusDays(7), item1);

        Item item2 = new Item("TestItem", "Short description", Category.FISH, 100.0, 5.0, "TestImage", Unit.ITEM, 10);
        FridgeItem fridgeItem2 = new FridgeItem(2, LocalDate.now().plusDays(14), item2);

        FridgeItem fridgeItem3 = new FridgeItem(1, LocalDate.now().plusDays(7), item1);

        assertNotEquals(fridgeItem1, fridgeItem2);
        assertNotEquals(fridgeItem1.hashCode(), fridgeItem2.hashCode());

        assertEquals(fridgeItem1, fridgeItem3);
        assertEquals(fridgeItem1.hashCode(), fridgeItem3.hashCode());
    }

    @Test
    void testAllArgsConstructor() {
        Item item = new Item("TestItem", "Short description", Category.FISH, 100.0, 5.0, "TestImage", Unit.ITEM, 10);
        FridgeItem fridgeItem = new FridgeItem(1, LocalDate.now().plusDays(7), item);

        assertEquals(1, fridgeItem.getQuantity());
        assertEquals(LocalDate.now().plusDays(7), fridgeItem.getExpirationDate());
        assertEquals(item, fridgeItem.getItem());
    }
}
