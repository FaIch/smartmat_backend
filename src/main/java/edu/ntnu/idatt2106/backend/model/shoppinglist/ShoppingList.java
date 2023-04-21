package edu.ntnu.idatt2106.backend.model.shoppinglist;


import edu.ntnu.idatt2106.backend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingListItem> shoppingListItems = new ArrayList<>();

    public void addShoppingListItem(ShoppingListItem shoppingListItem) {
        shoppingListItems.add(shoppingListItem);
        shoppingListItem.setShoppingList(this);
    }

    public void removeShoppingListItem(ShoppingListItem shoppingListItem) {
        shoppingListItems.remove(shoppingListItem);
        shoppingListItem.setShoppingList(null);
    }
}
