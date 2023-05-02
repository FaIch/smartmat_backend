package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenu;
import edu.ntnu.idatt2106.backend.model.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface WeekMenuRepository extends JpaRepository<WeekMenu, Long> {
    Optional<WeekMenu> findByUser(User user);
}
