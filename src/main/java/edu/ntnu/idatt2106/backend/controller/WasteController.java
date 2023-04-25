package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.waste.Waste;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.model.waste.WasteRequest;
import edu.ntnu.idatt2106.backend.service.WasteService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Integer> getTotalWasteByUser(@AuthenticationPrincipal User user) {
        return wasteService.getTotalWasteByUser(user);
    }

    @GetMapping("/total/date-range")
    public ResponseEntity<Integer> getTotalWasteByUserBetweenDates(@AuthenticationPrincipal User user
            , @RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return wasteService.getTotalWasteByUserBetweenDates(user, start, end);
    }
}
