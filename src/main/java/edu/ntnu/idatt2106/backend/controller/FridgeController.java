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

    /**
     * Get all fridge items for a user
     * @param user the user to get fridge items for
     * @return a list of fridge items
     */
    @GetMapping("/get")
    public ResponseEntity<List<FridgeItem>> getFridgeItemsByUserId(@AuthenticationPrincipal User user) {
        return fridgeService.getFridgeItemsByUserId(user);
    }

    /**
     * Get the number of fridge items for a user
     * @param user the user to get the number of fridge items for
     * @return a map with the number of fridge items split into expired and not expired
     */
    @GetMapping("/get/number")
    public ResponseEntity<Map<String, Integer>> getNumberOfFridgeItemsByUserID(@AuthenticationPrincipal User user) {
        return fridgeService.getNumberOfFridgeItemsByUserID(user);
    }

    /**
     * Get all expired fridge items for a user
     * @param user the user to get expired fridge items for
     * @return a list of expired fridge items
     */
    @GetMapping("/get/expired")
    public ResponseEntity<List<FridgeItem>> getExpiredFridgeItemsByUserId(@AuthenticationPrincipal User user) {
        return fridgeService.getExpiredFridgeItemsByUserId(user);
    }

    /**
     * Get items that are expired or almost expired to notify the user
     * @param user the user to get the items for
     * @return a list of expired and almost expired fridge items
     */
    @GetMapping("/get/notification")
    public ResponseEntity<List<FridgeItem>> getNotificationItems(@AuthenticationPrincipal User user) {
        return fridgeService.getExpiredAndAlmostExpiredFridgeItemsByUser(user);
    }

    /**
     * Add a list of fridge items to the fridge
     * @param fridgeItemRequests the fridge items to add
     * @param user the user to add the fridge item for
     * @return a response entity with a status message
     */
    @PostMapping("/add")
    public ResponseEntity<String> addFridgeItems(@RequestBody List<FridgeItemRequest> fridgeItemRequests,
                                                 @AuthenticationPrincipal User user) {
        return fridgeService.addListOfFridgeItems(user, fridgeItemRequests);
    }

    /**
     * Remove a list of fridge items from the fridge
     * @param fridgeItemIds the fridge items to remove
     * @param user the user to remove the fridge items for
     * @return a response entity with a status message
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFridgeItems(@RequestBody List<Long> fridgeItemIds,
                                                    @AuthenticationPrincipal User user) {
        return fridgeService.removeListOfFridgeItems(fridgeItemIds, user);
    }

    /**
     * Edit a fridge item
     * @param fridgeItemId the id of the fridge item to edit
     * @param updatedFridgeItem the updated fridge item
     * @param user the user to edit the fridge item for
     * @return a response entity with the updated fridge item
     */
    @PutMapping("/edit/{fridgeItemId}")
    public ResponseEntity<FridgeItem> editFridgeItem(@PathVariable Long fridgeItemId
            , @RequestBody FridgeItemRequest updatedFridgeItem
            , @AuthenticationPrincipal User user) {
        return fridgeService.editFridgeItem(fridgeItemId, updatedFridgeItem, user);
    }

    /**
     * Remove fridge items that are used in a recipe
     * @param items the fridge items to remove
     * @param user the user to remove the fridge items for
     * @return a response entity with a status message
     */
    @PostMapping("remove/byRecipe")
    public ResponseEntity<String> removeFridgeItemsByRecipe(@RequestBody List<FridgeItemRequest> items,
                                                            @AuthenticationPrincipal User user) {
        return fridgeService.removeFridgeItemsByRecipe(items, user);
    }

    @GetMapping("fridge-items/list/date")
    public ResponseEntity<List<FridgeItem>> getItemsByDate(){
        return fridgeService.expirationDate();
    }
}
