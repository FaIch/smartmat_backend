package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    @Query("SELECT fi.item.id FROM ShoppingListItem fi WHERE fi.shoppingList.user.id = :userId")
    List<Long> findItemIdsByUserId(Long userId);

    Optional<ShoppingListItem> findByShoppingListAndItem(ShoppingList shoppingList, Item item);

}
