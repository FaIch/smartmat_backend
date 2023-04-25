package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.item.CustomItem;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomItemRepository extends JpaRepository<CustomItem, Long> {
    List<CustomItem> findAllByUser(User user);
}
