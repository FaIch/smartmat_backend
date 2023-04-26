package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long>{}
