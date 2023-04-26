package edu.ntnu.idatt2106.backend.JUnit.shoppinglist;

import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingListTest {

    private ShoppingList shoppingList;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        shoppingList = new ShoppingList();
        shoppingList.setUser(user);
    }

    @Test
    public void testAddShoppingListItem() {
        ShoppingListItem shoppingListItem = new ShoppingListItem();
        shoppingList.addShoppingListItem(shoppingListItem);

        assertEquals(1, shoppingList.getShoppingListItems().size());
    }

    @Test
    public void testRemoveShoppingListItem() {
        ShoppingListItem shoppingListItem = new ShoppingListItem();
        shoppingList.addShoppingListItem(shoppingListItem);

        shoppingList.removeShoppingListItem(shoppingListItem);

        assertEquals(0, shoppingList.getShoppingListItems().size());
    }

    @Test
    public void testEqualsAndHashCode() {
        ShoppingList shoppingList2 = new ShoppingList();
        shoppingList2.setUser(user);

        assertEquals(shoppingList, shoppingList2);
        assertEquals(shoppingList.hashCode(), shoppingList2.hashCode());

        shoppingList2.setUser(new User("test"));

        Assertions.assertNotEquals(shoppingList, shoppingList2);
        Assertions.assertNotEquals(shoppingList.hashCode(), shoppingList2.hashCode());
    }
}
