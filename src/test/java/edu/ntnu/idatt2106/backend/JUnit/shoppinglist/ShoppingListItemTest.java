package edu.ntnu.idatt2106.backend.JUnit.shoppinglist;

import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShoppingListItemTest {

    private ShoppingListItem shoppingListItem;
    private Item item;
    private ShoppingList shoppingList;

    @BeforeEach
    public void setUp() {
        item = new Item();
        shoppingList = new ShoppingList();
        shoppingListItem = new ShoppingListItem(2, item);
        shoppingListItem.setShoppingList(shoppingList);
    }

    @Test
    public void testAllArgsConstructor() {
        ShoppingListItem shoppingListItem2 = new ShoppingListItem(3, item);
        Assertions.assertEquals(3, shoppingListItem2.getQuantity());
        Assertions.assertEquals(item, shoppingListItem2.getItem());
    }

    @Test
    public void testEqualsAndHashCode() {
        ShoppingListItem shoppingListItem2 = new ShoppingListItem(2, item);
        shoppingListItem2.setShoppingList(shoppingList);

        Assertions.assertEquals(shoppingListItem, shoppingListItem2);
        Assertions.assertEquals(shoppingListItem.hashCode(), shoppingListItem2.hashCode());

        shoppingListItem2.setItem(new Item("test"));

        Assertions.assertNotEquals(shoppingListItem, shoppingListItem2);
        Assertions.assertNotEquals(shoppingListItem.hashCode(), shoppingListItem2.hashCode());
    }

    @Test
    public void testGettersAndSetters() {
        Item item2 = new Item();
        ShoppingList shoppingList2 = new ShoppingList();

        shoppingListItem.setQuantity(3);
        Assertions.assertEquals(3, shoppingListItem.getQuantity());

        shoppingListItem.setItem(item2);
        Assertions.assertEquals(item2, shoppingListItem.getItem());

        shoppingListItem.setShoppingList(shoppingList2);
        Assertions.assertEquals(shoppingList2, shoppingListItem.getShoppingList());
    }
}
