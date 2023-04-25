package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.fridge.Fridge;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FridgeRepository extends JpaRepository<Fridge, Long> {
    Optional<Fridge> findFridgeByUser(User user);

    @Query("SELECT sli FROM FridgeItem sli JOIN sli.fridge sl WHERE sl.id = :fridgeId")
    List<FridgeItem> findFridgeItemsByFridgeId(@Param("fridgeId") Long fridgeId);
}
