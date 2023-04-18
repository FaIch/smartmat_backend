package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Connectiong backend to database(?)
 */
public interface ItemRepository  extends JpaRepository<Item, Long> {
    /**
     * Method findByCategory
     * @param category string category of the items which we want
     * @return a list of items of the given category
     */
    List<Item> findByCategory(String category);

  /*  *//**
     * Method findAllByUser
     * @param user entity of user which we want to find all items
     * @return list of all items which specific user sells
     *//*
    List<ItemEntity> findAllByUser(User user);
*/
}
