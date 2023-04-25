package edu.ntnu.idatt2106.backend.controller;


import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItemRequest;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.ItemService;
import edu.ntnu.idatt2106.backend.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }
    @GetMapping("/user/shopping-list-items")
    public ResponseEntity<List<ShoppingListItem>> getShoppingListItemsByUserId(@AuthenticationPrincipal User user) {
        return shoppingListService.getShoppingListItemsByUserId(user);
    }

    @PostMapping("/shopping-list/add")
    public ResponseEntity<String> addShoppingListItem(@RequestBody ShoppingListItemRequest request, @AuthenticationPrincipal User user) {
        return shoppingListService.addShoppingListItem(user, request);
    }

    @PostMapping("/shopping-list/add-list")
    public ResponseEntity<String> addListOfShoppingListItems(@RequestBody List<ShoppingListItemRequest> shoppingListItems,
                                                             @AuthenticationPrincipal User user) {

        return shoppingListService.addListOfShoppingListItems(user, shoppingListItems);
    }

    @DeleteMapping("/shopping-list-items/")
    public ResponseEntity<String> removeShoppingListItem(@RequestParam Long shoppingListItemId, @AuthenticationPrincipal User user) {
        return shoppingListService.removeShoppingListItem(user, shoppingListItemId);
    }

    @DeleteMapping("/shopping-list-items/delete-list")
    public ResponseEntity<String> removeListOfShoppingListItems(@RequestParam List<Long> shoppingListItemIds,
                                                                @AuthenticationPrincipal User user) {
        return shoppingListService.removeListOfShoppingListItems(user, shoppingListItemIds);
    }

    @PutMapping("/shopping-list-items/editQuantity/")
    public ResponseEntity<String> updateShoppingListItemQuantity(@RequestParam Long shoppingListItemId
            , @RequestParam int updatedShoppingListItemQuantity) {
        return shoppingListService.updateShoppingListItemQuantity(shoppingListItemId, updatedShoppingListItemQuantity);
    }
}
