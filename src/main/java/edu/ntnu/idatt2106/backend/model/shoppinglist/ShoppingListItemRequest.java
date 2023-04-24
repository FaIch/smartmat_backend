package edu.ntnu.idatt2106.backend.model.shoppinglist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ShoppingListItemRequest {
    private Long itemId;
    private int quantity;
}
