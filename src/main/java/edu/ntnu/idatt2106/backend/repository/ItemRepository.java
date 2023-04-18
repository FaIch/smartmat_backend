package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Connectiong backend to database(?)
 */
public interface ItemRepository  extends JpaRepository<ItemEntity, Long> {
}
