package edu.ntnu.idatt2106.backend.model.fridge;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FridgeItemRequest {
    private Long itemId;
    private int quantity;

    private LocalDate expirationDate;

    public FridgeItemRequest(Long itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }
}
