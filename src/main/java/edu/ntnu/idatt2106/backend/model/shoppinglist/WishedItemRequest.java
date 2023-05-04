package edu.ntnu.idatt2106.backend.model.shoppinglist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class WishedItemRequest {
    private Long itemId;
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishedItemRequest that = (WishedItemRequest) o;
        return quantity == that.quantity && Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, quantity);
    }
}
