package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.waste.Waste;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.WasteService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Waste addWasteEntry(@AuthenticationPrincipal User user, @RequestParam int weight,
                               @RequestParam String entryDate) {
        LocalDate date = LocalDate.parse(entryDate);
        return wasteService.addWasteEntry(user, weight, date);
    }

    @GetMapping("/total")
    public Integer getTotalWasteByUser(@AuthenticationPrincipal User user) {
        return wasteService.getTotalWasteByUser(user);
    }

    @GetMapping("/total/date-range")
    public Integer getTotalWasteByUserBetweenDates(@AuthenticationPrincipal User user, @RequestParam String startDate,
                                                   @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return wasteService.getTotalWasteByUserBetweenDates(user, start, end);
    }
}
