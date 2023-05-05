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

/**
 * ShoppingListController is a REST controller responsible for handling HTTP requests
 * related to shopping list items. It uses ShoppingListService to process the business logic
 * and return appropriate responses.
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/shopping-list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    /**
     * Constructs a ShoppingListController with the provided ShoppingListService instance.
     *
     * @param shoppingListService an instance of ShoppingListService
     */
    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    /**
     * Retrieves shopping list items for the authenticated user.
     *
     * @param user the authenticated user
     * @return ResponseEntity containing a list of shopping list items for the user
     */
    @GetMapping("/get")
    public ResponseEntity<List<ShoppingListItem>> getShoppingListItemsByUserId(@AuthenticationPrincipal User user) {
        return shoppingListService.getShoppingListItemsByUserId(user);
    }

    /**
     * Retrieves the number of shopping list items for the authenticated user.
     *
     * @param user the authenticated user
     * @return ResponseEntity containing the number of shopping list items for the user
     */
    @GetMapping("/get/number")
    public ResponseEntity<Map<String, Integer>> getNumberOfShoppingListItems(@AuthenticationPrincipal User user) {
        return shoppingListService.getNumberOfShoppingListItemsByUserId(user);
    }

    /**
     * Retrieves item suggestions for the authenticated user.
     *
     * @param user the authenticated user
     * @return ResponseEntity containing a list of suggested items for the user
     */
    @GetMapping("/get/suggestions")
    public ResponseEntity<List<Item>> getSuggestions(@AuthenticationPrincipal User user) {
        return shoppingListService.getSuggestedItems(user.getId());
    }

    /**
     * Adds a list of shopping list items for the authenticated user.
     *
     * @param shoppingListItems the list of ShoppingListItemRequest objects to add
     * @param user              the authenticated user
     * @return ResponseEntity containing a message about the result of the operation
     */
    @PostMapping("/add")
    public ResponseEntity<String> addListOfShoppingListItems(@RequestBody List<ShoppingListItemRequest> shoppingListItems,
                                                             @AuthenticationPrincipal User user) {
        return shoppingListService.addListOfShoppingListItems(user, shoppingListItems);
    }

    /**
     * Removes a list of shopping list items for the authenticated user.
     *
     * @param shoppingListItemIds the list of shopping list item IDs to remove
     * @param user                the authenticated user
     * @return ResponseEntity containing a message about the result of the operation
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeListOfShoppingListItems(@RequestParam List<Long> shoppingListItemIds,
                                                                @AuthenticationPrincipal User user) {
        return shoppingListService.removeListOfShoppingListItems(user, shoppingListItemIds);
    }

    /**
     * Updates the quantities of shopping list items for the authenticated user.
     *
     * @param shoppingListItemRequests the list of ShoppingListItemRequest objects with updated quantities
     * @param user                      the authenticated user
     * @return ResponseEntity containing a message about the result of the operation
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateShoppingListItemsQuantity(@RequestBody List<ShoppingListItemRequest> shoppingListItemRequests,
                                                                  @AuthenticationPrincipal User user) {
        return shoppingListService.updateShoppingListItems(user, shoppingListItemRequests);
    }
}
