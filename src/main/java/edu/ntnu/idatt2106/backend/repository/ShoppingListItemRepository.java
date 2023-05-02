package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long>{
    @Query("SELECT fi.item.id FROM ShoppingListItem fi WHERE fi.shoppingList.user.id = :userId")
    List<Long> findItemIdsByUserId(Long userId);
}
