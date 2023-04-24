package edu.ntnu.idatt2106.backend.JUnit.item;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemTest {
    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item(1L, "TestItem", "Short description", Category.FISH, 100.0, 5.0, "TestImage");
    }

    @Test
    void testNoArgsConstructor() {
        Item emptyItem = new Item();
        assertNull(emptyItem.getName());
        assertNull(emptyItem.getShortDesc());
        assertNull(emptyItem.getCategory());
        assertEquals(0.0, emptyItem.getPrice());
        assertEquals(0.0, emptyItem.getWeight());
        assertNull(emptyItem.getImage());
    }

    @Test
    void testAllArgsConstructor() {
        assertEquals(1L, item.getId());
        assertEquals("TestItem", item.getName());
        assertEquals("Short description", item.getShortDesc());
        assertEquals(Category.FISH, item.getCategory());
        assertEquals(100.0, item.getPrice());
        assertEquals(5.0, item.getWeight());
        assertEquals("TestImage", item.getImage());
    }

    @Test
    void testSettersAndGetters() {
        item.setId(2L);
        item.setName("UpdatedItem");
        item.setShortDesc("Updated description");
        item.setCategory(Category.EGG);
        item.setPrice(50.0);
        item.setWeight(2.0);
        item.setImage("UpdatedImage");

        assertEquals(2L, item.getId());
        assertEquals("UpdatedItem", item.getName());
        assertEquals("Updated description", item.getShortDesc());
        assertEquals(Category.EGG, item.getCategory());
        assertEquals(50.0, item.getPrice());
        assertEquals(2.0, item.getWeight());
        assertEquals("UpdatedImage", item.getImage());
    }
}