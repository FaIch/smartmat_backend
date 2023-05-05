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

/**
 * WasteController is a REST controller responsible for handling waste-related
 * HTTP requests. This includes adding waste entries, and retrieving waste statistics
 * for different time periods.
 */
@RestController
@RequestMapping("/waste")
public class WasteController {

    private final WasteService wasteService;
    private final Double moneyLostPerKg = 63.0;
    private final Double co2EmissionsPerKg = 3.6;

    /**
     * Constructs a WasteController with the provided WasteService instance.
     *
     * @param wasteService an instance of WasteService
     */
    @Autowired
    public WasteController(WasteService wasteService) {
        this.wasteService = wasteService;
    }

    /**
     * Adds a waste entry for the authenticated user.
     *
     * @param user the authenticated user
     * @param wasteRequest the waste data to be added
     * @return ResponseEntity containing the result of the operation
     */
    @PostMapping("/add")
    public ResponseEntity<String> addWasteEntry(@AuthenticationPrincipal User user,@RequestBody WasteRequest wasteRequest) {
        return wasteService.addWasteEntry(user, wasteRequest);
    }

    /**
     * Retrieves the total waste, money lost, and CO2 emissions for the authenticated user
     * for all time.
     *
     * @param user the authenticated user
     * @return ResponseEntity containing a list of waste, money lost, and CO2 emissions values
     */
    @GetMapping("/total/all-time")
    public ResponseEntity<List<Double>> getTotalWasteByUser(@AuthenticationPrincipal User user) {
        Double totalWaste = wasteService.getTotalWasteByUser(user);
        Double totalMoneyLost = totalWaste * moneyLostPerKg;
        Double totalEmissions = totalWaste * co2EmissionsPerKg;
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>(Arrays.asList(totalWaste,
                totalMoneyLost, totalEmissions)));
    }

    /**
     * Retrieves the total waste, money lost, and CO2 emissions for the authenticated user
     * for the last week.
     *
     * @param user the authenticated user
     * @return ResponseEntity containing a list of waste, money lost, and CO2 emissions values
     */
    @GetMapping("/total/last-week")
    public ResponseEntity<List<Double>> getLastWeekWasteByUser(@AuthenticationPrincipal User user) {
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();
        Double lastWeekWaste = wasteService.getTotalWasteByUserBetweenDates(user, start, end).getBody();
        Double lastWeekMoneyLost = lastWeekWaste * moneyLostPerKg;
        Double lastWeekEmissions = lastWeekWaste * co2EmissionsPerKg;
        List<Double> statList = new ArrayList<>(Arrays.asList(lastWeekWaste,
                lastWeekMoneyLost, lastWeekEmissions));
        return ResponseEntity.status(HttpStatus.OK).body(statList);
    }

    /**
     * Retrieves the total waste, money lost, and CO2 emissions for the authenticated user
     * for the last month.
     *
     * @param user the authenticated user
     * @return ResponseEntity containing a list of waste, money lost, and CO2 emissions values
     */
    @GetMapping("/total/last-month")
    public ResponseEntity<List<Double>> getLastMonthWasteByUser(@AuthenticationPrincipal User user) {
        LocalDate start = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now();
        Double lastMonthWaste = wasteService.getTotalWasteByUserBetweenDates(user, start, end).getBody();
        Double lastMonthMoneyLost = lastMonthWaste * moneyLostPerKg;
        Double lastMonthEmissions = lastMonthWaste * co2EmissionsPerKg;
        List<Double> statList = new ArrayList<>(Arrays.asList(lastMonthWaste,
                lastMonthMoneyLost, lastMonthEmissions));
        return ResponseEntity.status(HttpStatus.OK).body(statList);
    }

    /**
     * Retrieves the total waste, money lost, and CO2 emissions for the authenticated user
     * for the last year.
     *
     * @param user the authenticated user
     * @return ResponseEntity containing a list of waste, money lost, and CO2 emissions values
     */
    @GetMapping("/total/last-year")
    public ResponseEntity<List<Double>> getLastYearWasteByUser(@AuthenticationPrincipal User user) {
        LocalDate start = LocalDate.now().minusYears(1);
        LocalDate end = LocalDate.now();
        Double lastYearWaste = wasteService.getTotalWasteByUserBetweenDates(user, start, end).getBody();
        Double lastYearMoneyLost = lastYearWaste * moneyLostPerKg;
        Double lastYearEmissions = lastYearWaste * co2EmissionsPerKg;
        List<Double> statList = new ArrayList<>(Arrays.asList(lastYearWaste,
                lastYearMoneyLost, lastYearEmissions));
        return ResponseEntity.status(HttpStatus.OK).body(statList);
    }
}
