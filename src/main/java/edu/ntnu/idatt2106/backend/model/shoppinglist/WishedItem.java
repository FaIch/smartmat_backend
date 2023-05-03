package edu.ntnu.idatt2106.backend.model.shoppinglist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ntnu.idatt2106.backend.model.item.Item;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WishedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_list_id")
    private ShoppingList shoppingList;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public WishedItem(int quantity, Item item) {
        this.quantity = quantity;
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishedItem that = (WishedItem) o;
        return quantity == that.quantity && Objects.equals(id, that.id) &&
                Objects.equals(shoppingList, that.shoppingList) && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shoppingList, quantity, item);
    }
}
