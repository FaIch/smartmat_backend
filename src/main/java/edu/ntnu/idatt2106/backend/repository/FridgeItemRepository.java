package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FridgeItemRepository extends JpaRepository<FridgeItem, Long> {
    List<FridgeItem> findAll(Sort sort);

    FridgeItem findByItemId(Long itemId);

    @Query("SELECT fi.item.id FROM FridgeItem fi WHERE fi.fridge.user.id = :userId")
    List<Long> findItemIdsByUserId(Long userId);

    @Query("SELECT fi.item.id FROM FridgeItem fi WHERE fi.fridge.user.id = :userId AND " +
            "fi.expirationDate BETWEEN :today AND :threeDaysFromNow")
    List<Long> findExpiringItemIdsByUserId(Long userId, LocalDate today, LocalDate threeDaysFromNow);

    @Query("SELECT fi FROM FridgeItem fi JOIN fi.fridge fr WHERE fr.user.id = :userId AND fi.item.id = :itemId")
    List<FridgeItem> findByUserIdAndItemId(Long userId, Long itemId);

    @Query("SELECT fi FROM FridgeItem fi WHERE fi.fridge.user.id = :userId")
    List<FridgeItem> findByUserId(Long userId);
}
