package edu.ntnu.idatt2106.backend.model.fridge;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class FridgeItemRequest {
    private Long itemId;
    private int quantity;
    private LocalDate expirationDate;
}
