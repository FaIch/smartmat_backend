package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.item.CustomItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomItemRepository extends JpaRepository<CustomItem, Long> {
}
