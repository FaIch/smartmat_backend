package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

    Optional<ShoppingList> findShoppingListByUser(User user);
    @Query("SELECT sli FROM ShoppingListItem sli JOIN sli.shoppingList sl WHERE sl.id = :shoppingListId")
    List<ShoppingListItem> findShoppingListItemsByShoppingListId(@Param("shoppingListId") Long shoppingListId);
}
