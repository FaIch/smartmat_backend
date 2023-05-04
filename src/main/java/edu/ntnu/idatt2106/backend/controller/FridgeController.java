package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItemRequest;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.FridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/fridge")
public class FridgeController {

    private final FridgeService fridgeService;

    @Autowired
    public FridgeController(FridgeService fridgeService) {
        this.fridgeService = fridgeService;
    }

    /*
    * Get all fridge items for a user
    *
    * @return List<FridgeItem>
     */
    @GetMapping("/get")
    public ResponseEntity<List<FridgeItem>> getFridgeItemsByUserId(@AuthenticationPrincipal User user) {
        return fridgeService.getFridgeItemsByUserId(user);
    }

    /*
    * Get number of fridge items for a user
     */
    @GetMapping("/get/number")
    public ResponseEntity<Map<String, Integer>> getNumberOfFridgeItemsByUserID(@AuthenticationPrincipal User user) {
        return fridgeService.getNumberOfFridgeItemsByUserID(user);
    }

    /*
    * Get all expired fridge items for a user
     */
    @GetMapping("/get/expired")
    public ResponseEntity<List<FridgeItem>> getExpiredFridgeItemsByUserId(@AuthenticationPrincipal User user) {
        return fridgeService.getExpiredFridgeItemsByUserId(user);
    }

    /*
    * Get all almost expired fridge items for a user
     */
    @GetMapping("/get/notification")
    public ResponseEntity<List<FridgeItem>> getNotificationItems(@AuthenticationPrincipal User user) {
        return fridgeService.getExpiredAndAlmostExpiredFridgeItemsByUser(user);
    }

    /*
    * Add a fridge item for a user
     */
    @PostMapping("/add")
    public ResponseEntity<String> addFridgeItems(@RequestBody List<FridgeItemRequest> fridgeItemRequests,
                                                 @AuthenticationPrincipal User user) {
        return fridgeService.addListOfFridgeItems(user, fridgeItemRequests);
    }

    /*
    * Remove a fridge item for a user
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFridgeItems(@RequestBody List<Long> fridgeItemIds,
                                                    @AuthenticationPrincipal User user) {
        return fridgeService.removeListOfFridgeItems(fridgeItemIds, user);
    }

    /*
    * Edit a fridge item for a user
     */
    @PutMapping("/edit/{fridgeItemId}")
    public ResponseEntity<FridgeItem> editFridgeItem(@PathVariable Long fridgeItemId
            , @RequestBody FridgeItemRequest updatedFridgeItem
            , @AuthenticationPrincipal User user) {
        return fridgeService.editFridgeItem(fridgeItemId, updatedFridgeItem, user);
    }

    /*
    * Remove all fridge items for a user
     */
    @PostMapping("remove/byRecipe")
    public ResponseEntity<String> removeFridgeItemsByRecipe(@RequestBody List<FridgeItemRequest> items,
                                                            @AuthenticationPrincipal User user) {
        return fridgeService.removeFridgeItemsByRecipe(items, user);
    }

    //Not tested!!!
    @GetMapping("fridge-items/list/date")
    public ResponseEntity<List<FridgeItem>> getItemsByDate(){
        return fridgeService.expirationDate();
    }
}
