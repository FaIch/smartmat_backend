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

    /*
    * Get all shopping list items for a user
     */
    @GetMapping("/get")
    public ResponseEntity<List<ShoppingListItem>> getShoppingListItemsByUserId(@AuthenticationPrincipal User user) {
        return shoppingListService.getShoppingListItemsByUserId(user);
    }

    /*
    * Get number of shopping list items for a user
     */
    @GetMapping("/get/number")
    public ResponseEntity<Map<String, Integer>> getNumberOfShoppingListItems(@AuthenticationPrincipal User user) {
        return shoppingListService.getNumberOfShoppingListItemsByUserId(user);
    }

    /*
    * Get suggestions by user
     */
    @GetMapping("/get/suggestions")
    public ResponseEntity<List<Item>> getSuggestions(@AuthenticationPrincipal User user) {
        return shoppingListService.getSuggestedItems(user.getId());
    }

    /*
    * Add a list of shopping list items
     */
    @PostMapping("/add")
    public ResponseEntity<String> addListOfShoppingListItems(@RequestBody List<ShoppingListItemRequest> shoppingListItems,
                                                             @AuthenticationPrincipal User user) {
        return shoppingListService.addListOfShoppingListItems(user, shoppingListItems);
    }

    /*
    * Get all wished shopping list items for a user
     */
    @GetMapping("/get/wished")
    public ResponseEntity<List<ShoppingListItem>> getWishedShoppingListItems(@AuthenticationPrincipal User user) {
        return shoppingListService.getWishedItemsByUser(user);
    }

    /*
    * Add a list of wished shopping list items
     */
    @PostMapping("/add/wished")
    public ResponseEntity<String> addWishedShoppingListItem(@RequestBody List<ShoppingListItemRequest> shoppingListItems,
                                                             @AuthenticationPrincipal User user) {
        return shoppingListService.addWishedItem(shoppingListItems, user);
    }

    /*
    * Remove a list of wished shopping list items
     */
    @DeleteMapping("/remove/wished")
    public ResponseEntity<String> removeWishedShoppingListItem(@AuthenticationPrincipal User user,
                                                       @RequestBody List<Long> shoppingListItemIds) {
        return shoppingListService.removeWishedShoppingListItems(user, shoppingListItemIds);
    }

    /*
    * Remove a list of shopping list items
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeListOfShoppingListItems(@RequestParam List<Long> shoppingListItemIds,
                                                                @AuthenticationPrincipal User user) {
        return shoppingListService.removeListOfShoppingListItems(user, shoppingListItemIds);
    }

    /*
    * Update a list of shopping list items
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateShoppingListItemsQuantity(@RequestBody List<ShoppingListItemRequest> shoppingListItemRequests,
                                                                  @AuthenticationPrincipal User user) {
        return shoppingListService.updateShoppingListItems(user, shoppingListItemRequests);
    }
}
