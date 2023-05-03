package edu.ntnu.idatt2106.backend.model.shoppinglist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WishedItemRequest {
    private Long itemId;
    private int quantity;
}
