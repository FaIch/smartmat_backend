package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.waste.Waste;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

public interface WasteRepository extends JpaRepository<Waste, Long> {

    @Query("SELECT SUM(w.weight) FROM Waste w WHERE w.user = :user")
    Integer getTotalWasteByUser(@Param("user") User user);

    @Query("SELECT SUM(w.weight) FROM Waste w WHERE w.user = :user AND w.entryDate BETWEEN :startDate AND :endDate")
    Integer getTotalWasteByUserBetweenDates(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
