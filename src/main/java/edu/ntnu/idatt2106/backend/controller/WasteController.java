package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.model.waste.WasteRequest;
import edu.ntnu.idatt2106.backend.service.WasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/waste")
public class WasteController {

    private final WasteService wasteService;

    @Autowired
    public WasteController(WasteService wasteService) {
        this.wasteService = wasteService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addWasteEntry(@AuthenticationPrincipal User user, @RequestBody WasteRequest wasteRequest) {
        return wasteService.addWasteEntry(user, wasteRequest);
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotalWasteByUser(@AuthenticationPrincipal User user) {
        return wasteService.getTotalWasteByUser(user);
    }

    @GetMapping("/total/date-range")
    public ResponseEntity<Double> getTotalWasteByUserBetweenDates(@AuthenticationPrincipal User user
            , @RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return wasteService.getTotalWasteByUserBetweenDates(user, start, end);
    }

    @GetMapping("/money")
    public ResponseEntity<Double> getMoneyLostByUser(@AuthenticationPrincipal User user) {
        Double moneyLost = wasteService.getTotalWasteByUser(user).getBody(); // 63kr per kg of food waste
        if (moneyLost == null || moneyLost == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(0.0);
        }
        return ResponseEntity.status(HttpStatus.OK).body(moneyLost*63.0);
    }

    @GetMapping("/emissions")
    public ResponseEntity<Double> getCo2EmissionsByUser(@AuthenticationPrincipal User user) {
        Double co2Emissions = wasteService.getTotalWasteByUser(user).getBody();
        if (co2Emissions == null || co2Emissions == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(0.0);
        }
        return ResponseEntity.status(HttpStatus.OK).body(co2Emissions*3.6); // 3.6 kg CO2 per kg of food waste
    }
}
