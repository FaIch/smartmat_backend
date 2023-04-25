package edu.ntnu.idatt2106.backend.JUnit.shoppinglist;

import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItemRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShoppingListItemRequestTest {

    private ShoppingListItemRequest shoppingListItemRequest;

    @BeforeEach
    public void setUp() {
        shoppingListItemRequest = new ShoppingListItemRequest(1L, 2);
    }

    @Test
    public void testAllArgsConstructor() {
        ShoppingListItemRequest shoppingListItemRequest2 = new ShoppingListItemRequest(1L, 3);
        Assertions.assertEquals(1L, shoppingListItemRequest2.getItemId());
        Assertions.assertEquals(3, shoppingListItemRequest2.getQuantity());
    }

    @Test
    public void testEqualsAndHashCode() {
        ShoppingListItemRequest shoppingListItemRequest2 = new ShoppingListItemRequest(1L, 2);

        Assertions.assertEquals(shoppingListItemRequest, shoppingListItemRequest2);
        Assertions.assertEquals(shoppingListItemRequest.hashCode(), shoppingListItemRequest2.hashCode());

        shoppingListItemRequest2.setItemId(2L);

        Assertions.assertNotEquals(shoppingListItemRequest, shoppingListItemRequest2);
        Assertions.assertNotEquals(shoppingListItemRequest.hashCode(), shoppingListItemRequest2.hashCode());
    }

    @Test
    public void testGettersAndSetters() {
        shoppingListItemRequest.setItemId(2L);
        Assertions.assertEquals(2L, shoppingListItemRequest.getItemId());

        shoppingListItemRequest.setQuantity(3);
        Assertions.assertEquals(3, shoppingListItemRequest.getQuantity());
    }
}
