package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.waste.Waste;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.model.waste.WasteRequest;
import edu.ntnu.idatt2106.backend.repository.WasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

/**
 * WasteService is a service class responsible for managing waste-related operations
 * such as adding waste entries and retrieving waste statistics for a user.
 */
@Service
public class WasteService {

    private final WasteRepository wasteRepository;

    /**
     * Constructs a WasteService with the provided WasteRepository instance.
     *
     * @param wasteRepository an instance of WasteRepository
     */
    @Autowired
    public WasteService(WasteRepository wasteRepository) {
        this.wasteRepository = wasteRepository;
    }

    /**
     * Adds a waste entry for the specified user.
     *
     * @param user the user for whom the waste entry is to be added
     * @param wasteRequest the waste data to be added
     * @return ResponseEntity containing the result of the operation
     */
    public ResponseEntity<String> addWasteEntry(User user, WasteRequest wasteRequest) {
        Waste wasteEntry = new Waste(user, wasteRequest.getWeight(), LocalDate.parse(wasteRequest.getEntryDate()));
        wasteRepository.save(wasteEntry);
        return ResponseEntity.status(HttpStatus.OK).body("Waste was added successfully");
    }

    /**
     * Retrieves the total waste for the specified user.
     *
     * @param user the user for whom the total waste is to be retrieved
     * @return the total waste for the user
     */
    public Double getTotalWasteByUser(User user) {
        return wasteRepository.getTotalWasteByUser(user);
    }

    /**
     * Retrieves the total waste for the specified user between the given dates.
     *
     * @param user the user for whom the total waste is to be retrieved
     * @param startDate the start date for the period to be considered
     * @param endDate the end date for the period to be considered
     * @return ResponseEntity containing the total waste for the user between the given dates
     */
    public ResponseEntity<Double> getTotalWasteByUserBetweenDates(User user, LocalDate startDate, LocalDate endDate) {
        Double totalWaste = wasteRepository.getTotalWasteByUserBetweenDates(user, startDate, endDate);
        if (totalWaste == null || totalWaste == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(0.0);
        }
        return ResponseEntity.status(HttpStatus.OK).body(totalWaste);
    }
}
