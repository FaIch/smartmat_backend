package edu.ntnu.idatt2106.backend.JUnit.item;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.item.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemTest {
    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item("TestItem", "Short description", Category.FISH, 100.0, 5.0, "TestImage", Unit.ITEM, 10);
    }

    @Test
    void testNoArgsConstructor() {
        Item emptyItem = new Item();
        assertNull(emptyItem.getName());
        assertNull(emptyItem.getShortDesc());
        assertNull(emptyItem.getCategory());
        assertEquals(0.0, emptyItem.getPrice());
        assertEquals(0.0, emptyItem.getWeightPerUnit());
        assertNull(emptyItem.getImage());
    }

    @Test
    void testAllArgsConstructor() {
        assertEquals("TestItem", item.getName());
        assertEquals("Short description", item.getShortDesc());
        assertEquals(Category.FISH, item.getCategory());
        assertEquals(100.0, item.getPrice());
        assertEquals(5.0, item.getWeightPerUnit());
        assertEquals("TestImage", item.getImage());
    }

    @Test
    void testSettersAndGetters() {
        item.setId(2L);
        item.setName("UpdatedItem");
        item.setShortDesc("Updated description");
        item.setCategory(Category.EGG);
        item.setPrice(50.0);
        item.setWeightPerUnit(2.0);
        item.setImage("UpdatedImage");

        assertEquals(2L, item.getId());
        assertEquals("UpdatedItem", item.getName());
        assertEquals("Updated description", item.getShortDesc());
        assertEquals(Category.EGG, item.getCategory());
        assertEquals(50.0, item.getPrice());
        assertEquals(2.0, item.getWeightPerUnit());
        assertEquals("UpdatedImage", item.getImage());
    }

    @Test
    public void testIdGeneration() {
        Item item1 = new Item(1L, "Ost", "Gulost", Category.CHEESE, 80.0, 1.0, "itemImg", Unit.ITEM, 10);
        Item item2 = new Item(2L,"Ost", "Gulost", Category.CHEESE, 50.0, 0.5, "itemImg", Unit.ITEM, 10);
        Assertions.assertNotEquals(item1.getId(), item2.getId());
    }
}