package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.backend.model.shoppinglist.WishedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishedItemRepository extends JpaRepository<WishedItem, Long> {
    Optional<WishedItem> findByShoppingListAndItem(ShoppingList shoppingList, Item item);
    List<WishedItem> findAllByShoppingList(ShoppingList shoppingList);
}
