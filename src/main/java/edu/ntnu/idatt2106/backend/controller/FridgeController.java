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
    private final ItemService itemService;

    @Autowired
    public FridgeController(FridgeService fridgeService, ItemService itemService) {
        this.fridgeService = fridgeService;
        this.itemService = itemService;
    }

    @GetMapping("/user/fridge-items")
    public ResponseEntity<List<FridgeItem>> getFridgeItemsByUserId(@AuthenticationPrincipal User user) {
        return fridgeService.getFridgeItemsByUserId(user.getId());
    }

    @PostMapping("/fridge/add")
    public ResponseEntity<String> addFridgeItem(@RequestParam("quantity") int quantity, @RequestParam("expirationDate")String expirationDate,
                                                @RequestParam long itemId, @AuthenticationPrincipal User user) {
        return fridgeService.addFridgeItem(user.getId(), new FridgeItem(quantity,LocalDate.parse(expirationDate),itemService.getItemById(itemId)));
    }

    @PostMapping("/fridge/add-list")
    public ResponseEntity<String> addFridgeItems(@RequestBody List<FridgeItemRequest> fridgeItemRequests,
                                                 @AuthenticationPrincipal User user) {
        return fridgeService.addListOfFridgeItems(user,
                fridgeService.convertListOfFridgeItemsRequestsToFridgeItems(fridgeItemRequests));
    }

    @DeleteMapping("/fridge-items/remove-list")
    public ResponseEntity<String> removeFridgeItems(@RequestBody List<Long> fridgeItemIds,
                                                    @AuthenticationPrincipal User user) {
        return fridgeService.removeListOfFridgeItems(fridgeItemIds, user);
    }

    @PutMapping("/fridge-items/edit/{fridgeItemId}")
    public ResponseEntity<FridgeItem> editFridgeItem(@PathVariable Long fridgeItemId
            , @RequestBody FridgeItem updatedFridgeItem) {
        return fridgeService.editFridgeItem(fridgeItemId, updatedFridgeItem);
    }

    //Not tested!!!
    @GetMapping("fridge-items/list/date")
    public ResponseEntity<List<FridgeItem>> getItemsByDate(){
        return fridgeService.expirationDate();
    }
}
