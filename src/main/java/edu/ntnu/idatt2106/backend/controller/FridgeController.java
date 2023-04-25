package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItemRequest;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.FridgeService;
import edu.ntnu.idatt2106.backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin("*")
public class FridgeController {

    private final FridgeService fridgeService;

    @Autowired
    public FridgeController(FridgeService fridgeService) {
        this.fridgeService = fridgeService;
    }

    @GetMapping("/user/fridge-items")
    public ResponseEntity<List<FridgeItem>> getFridgeItemsByUserId(@AuthenticationPrincipal User user) {
        return fridgeService.getFridgeItemsByUserId(user);
    }

    @PostMapping("/fridge/add")
    public ResponseEntity<String> addFridgeItem(@RequestBody FridgeItemRequest fridgeItemRequest, @AuthenticationPrincipal User user) {
        return fridgeService.addFridgeItem(user, fridgeItemRequest);
    }

    @PostMapping("/fridge/add-list")
    public ResponseEntity<String> addFridgeItems(@RequestBody List<FridgeItemRequest> fridgeItemRequests,
                                                 @AuthenticationPrincipal User user) {
        return fridgeService.addListOfFridgeItems(user, fridgeItemRequests);
    }

    @DeleteMapping("/fridge-items/{fridgeItemId}")
    public ResponseEntity<String> removeFridgeItem(@PathVariable Long fridgeItemId) {
        return fridgeService.removeFridgeItem(fridgeItemId);
    }

    @DeleteMapping("/fridge-items/remove-list")
    public ResponseEntity<String> removeFridgeItems(@RequestBody List<Long> fridgeItemIds) {
        return fridgeService.removeListOfFridgeItems(fridgeItemIds);
    }

    @PutMapping("/fridge-items/editQuantity/{fridgeItemId}")
    public ResponseEntity<String> updateFridgeItemQuantity(@PathVariable Long fridgeItemId
            , @RequestBody FridgeItemRequest updatedFridgeItem) {
        return fridgeService.updateFridgeItemQuantity(fridgeItemId, updatedFridgeItem);
    }

    @PutMapping("/fridge-items/editExpirationDate/{fridgeItemId}")
    public ResponseEntity<String> updateFridgeItemExpirationDate(@PathVariable Long fridgeItemId
            , @RequestBody FridgeItemRequest updatedFridgeItem) {
        return fridgeService.updateFridgeItemExpirationDate(fridgeItemId, updatedFridgeItem);
    }

    //Not tested!!!
    @GetMapping("fridge-items/list/date")
    public ResponseEntity<List<FridgeItem>> getItemsByDate(){
        return fridgeService.expirationDate();
    }
}
