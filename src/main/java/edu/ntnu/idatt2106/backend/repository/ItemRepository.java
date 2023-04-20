package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.CustomItem;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.springframework.data.domain.Sort;
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
    List<Item> findByCategory(Category category);

}
