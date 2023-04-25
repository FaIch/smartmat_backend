package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.waste.Waste;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
import edu.ntnu.idatt2106.backend.repository.WasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class WasteService {

    private final WasteRepository wasteRepository;
    private final UserRepository userRepository;

    @Autowired
    public WasteService(WasteRepository wasteRepository, UserRepository userRepository) {
        this.wasteRepository = wasteRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> addWasteEntry(User user, int weight) {
        LocalDate entryDate = LocalDate.now();
        Waste wasteEntry = new Waste(user, weight, entryDate);
        wasteRepository.save(wasteEntry);
        return ResponseEntity.status(HttpStatus.OK).body("Waste was added successfully");
    }

    public ResponseEntity<Integer> getTotalWasteByUser(User user) {
        Integer totalWaste = wasteRepository.getTotalWasteByUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(totalWaste != null ? totalWaste : 0);
    }

    public ResponseEntity<Integer> getTotalWasteByUserBetweenDates(User user, LocalDate startDate, LocalDate endDate) {
        Integer totalWaste = wasteRepository.getTotalWasteByUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(totalWaste != null ? totalWaste : 0);
    }
}
