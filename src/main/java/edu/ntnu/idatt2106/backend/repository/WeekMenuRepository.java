package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.weekMenu.WeekMenu;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeekMenuRepository extends JpaRepository<WeekMenu, Long> {
    Optional<WeekMenu> findByUser(User user);
}
