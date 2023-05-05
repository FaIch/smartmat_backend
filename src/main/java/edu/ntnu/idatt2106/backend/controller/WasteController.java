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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/waste")
public class WasteController {

    private final WasteService wasteService;

    @Autowired
    public WasteController(WasteService wasteService) {
        this.wasteService = wasteService;
    }

    /*
    Add a new waste entry
     */
    @PostMapping("/add")
    public ResponseEntity<String> addWasteEntry(@AuthenticationPrincipal User user,@RequestBody WasteRequest wasteRequest) {
        return wasteService.addWasteEntry(user, wasteRequest);
    }

    /*
    Get total waste by user
     */
    @GetMapping("/total/all-time")
    public ResponseEntity<List<Double>> getTotalWasteByUser(@AuthenticationPrincipal User user) {
        Double totalWaste = wasteService.getTotalWasteByUser(user);
        Double totalMoneyLost = totalWaste * 63.0;
        Double totalEmissions = totalWaste * 3.6;
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>(Arrays.asList(totalWaste,
                totalMoneyLost, totalEmissions)));
    }

    /*
    Get total waste by user last week
     */
    @GetMapping("/total/last-week")
    public ResponseEntity<List<Double>> getLastWeekWasteByUser(@AuthenticationPrincipal User user) {
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();
        Double lastWeekWaste = wasteService.getTotalWasteByUserBetweenDates(user, start, end).getBody();
        Double lastWeekMoneyLost = lastWeekWaste * 63.0;
        Double lastWeekEmissions = lastWeekWaste * 3.6;
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>(Arrays.asList(lastWeekWaste,
                lastWeekMoneyLost, lastWeekEmissions)));
    }

    /*
    Get total waste by user last month
     */
    @GetMapping("/total/last-month")
    public ResponseEntity<List<Double>> getLastMonthWasteByUser(@AuthenticationPrincipal User user) {
        LocalDate start = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now();
        Double lastMonthWaste = wasteService.getTotalWasteByUserBetweenDates(user, start, end).getBody();
        Double lastMonthMoneyLost = lastMonthWaste * 63.0;
        Double lastMonthEmissions = lastMonthWaste * 3.6;
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>(Arrays.asList(lastMonthWaste,
                lastMonthMoneyLost, lastMonthEmissions)));
    }

    /*
    Get total waste by user last year
     */
    @GetMapping("/total/last-year")
    public ResponseEntity<List<Double>> getLastYearWasteByUser(@AuthenticationPrincipal User user) {
        LocalDate start = LocalDate.now().minusYears(1);
        LocalDate end = LocalDate.now();
        Double lastYearWaste = wasteService.getTotalWasteByUserBetweenDates(user, start, end).getBody();
        Double lastYearMoneyLost = lastYearWaste * 63.0;
        Double lastYearEmissions = lastYearWaste * 3.6;
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>(Arrays.asList(lastYearWaste,
                lastYearMoneyLost, lastYearEmissions)));
    }
}
