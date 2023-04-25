package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.waste.Waste;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.model.waste.WasteRequest;
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

    @Autowired
    public WasteService(WasteRepository wasteRepository) {
        this.wasteRepository = wasteRepository;
    }

    public ResponseEntity<Waste> addWasteEntry(User user, WasteRequest wasteRequest) {
        Waste wasteEntry = new Waste(user, wasteRequest.getWeight(), LocalDate.parse(wasteRequest.getEntryDate()));
        return ResponseEntity.status(HttpStatus.OK).body(wasteRepository.save(wasteEntry));
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
