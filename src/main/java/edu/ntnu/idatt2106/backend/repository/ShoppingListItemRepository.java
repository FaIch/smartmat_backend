package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long>{
}
