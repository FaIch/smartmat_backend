package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.FridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

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
        return fridgeService.getFridgeItemsByUserId(user.getId());
    }

    @PostMapping("/fridge/add")
    public ResponseEntity<String> addFridgeItem(@RequestBody FridgeItem fridgeItem, @AuthenticationPrincipal User user) {
        return fridgeService.addFridgeItem(user.getId(), fridgeItem);
    }

    @DeleteMapping("/fridge-items/{fridgeItemId}")
    public ResponseEntity<String> removeFridgeItem(@PathVariable Long fridgeItemId) {
        return fridgeService.removeFridgeItem(fridgeItemId);
    }

    @PutMapping("/fridge-items/editQuantity/{fridgeItemId}")
    public ResponseEntity<String> updateFridgeItemQuantity(@PathVariable Long fridgeItemId
            , @RequestBody FridgeItem updatedFridgeItem) {
        return fridgeService.updateFridgeItemQuantity(fridgeItemId, updatedFridgeItem);
    }

    @PutMapping("/fridge-items/editExpirationDate/{fridgeItemId}")
    public ResponseEntity<String> updateFridgeItemExpirationDate(@PathVariable Long fridgeItemId
            , @RequestBody FridgeItem updatedFridgeItem) {
        return fridgeService.updateFridgeItemExpirationDate(fridgeItemId, updatedFridgeItem);
    }
}
