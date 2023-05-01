package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenu;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WeekMenuRepository extends JpaRepository<WeekMenu, Long> {
    @Query("SELECT wm FROM WeekMenu wm WHERE wm.user = :user")
    Optional<WeekMenu> findByUser(@Param("user") User user);
}
