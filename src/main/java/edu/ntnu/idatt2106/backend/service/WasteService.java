package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.waste.Waste;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.WasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class WasteService {

    private final WasteRepository wasteRepository;

    @Autowired
    public WasteService(WasteRepository wasteRepository) {
        this.wasteRepository = wasteRepository;
    }

    public Waste addWasteEntry(User user, int weight, LocalDate entryDate) {
        Waste wasteEntry = new Waste(user, weight, entryDate);
        return wasteRepository.save(wasteEntry);
    }

    public Integer getTotalWasteByUser(User user) {
        Integer totalWaste = wasteRepository.getTotalWasteByUser(user);
        return totalWaste != null ? totalWaste : 0;
    }

    public Integer getTotalWasteByUserBetweenDates(User user, LocalDate startDate, LocalDate endDate) {
        Integer totalWaste = wasteRepository.getTotalWasteByUserBetweenDates(user, startDate, endDate);
        return totalWaste != null ? totalWaste : 0;
    }
}
