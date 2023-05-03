package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItemRequest;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/shopping-list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<ShoppingListItem>> getShoppingListItemsByUserId(@AuthenticationPrincipal User user) {
        return shoppingListService.getShoppingListItemsByUserId(user);
    }

    @GetMapping("/get/number")
    public ResponseEntity<Map<String, Integer>> getNumberOfShoppingListItems(@AuthenticationPrincipal User user) {
        return shoppingListService.getNumberOfShoppingListItemsByUserId(user);
    }
    @GetMapping("/get/suggestions")
    public ResponseEntity<List<Item>> getSuggestions(@AuthenticationPrincipal User user) {
        return shoppingListService.getSuggestedItems(user.getId());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addListOfShoppingListItems(@RequestBody List<ShoppingListItemRequest> shoppingListItems,
                                                             @AuthenticationPrincipal User user) {
        return shoppingListService.addListOfShoppingListItems(user, shoppingListItems);
    }
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeListOfShoppingListItems(@RequestParam List<Long> shoppingListItemIds,
                                                                @AuthenticationPrincipal User user) {
        return shoppingListService.removeListOfShoppingListItems(user, shoppingListItemIds);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateShoppingListItemsQuantity(@RequestBody List<ShoppingListItemRequest> shoppingListItemRequests,
                                                                  @AuthenticationPrincipal User user) {
        return shoppingListService.updateShoppingListItems(user, shoppingListItemRequests);
    }
}
