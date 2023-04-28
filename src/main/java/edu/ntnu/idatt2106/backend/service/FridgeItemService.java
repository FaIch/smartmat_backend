package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.repository.FridgeItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FridgeItemService {

    @Autowired
    private FridgeItemRepository fridgeItemRepository;

    public List<Long> getItemIdsByUserId(Long userId) {
        return fridgeItemRepository.findItemIdsByUserId(userId);
    }

    public List<Long> getExpiringItemIdsByUserId(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate threeDaysFromNow = today.plusDays(3);
        return fridgeItemRepository.findExpiringItemIdsByUserId(userId, today, threeDaysFromNow);
    }
}