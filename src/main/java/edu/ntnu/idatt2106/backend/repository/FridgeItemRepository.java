package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeItemRepository extends JpaRepository<FridgeItem, Long> {
}
