package edu.ntnu.idatt2106.backend.controller;


import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.backend.model.user.User;
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
        return shoppingListService.getShoppingListItemsByUserId(user.getId());
    }

    @PostMapping("/shopping-list/add")
    public ResponseEntity<String> addShoppingListItem(@RequestBody ShoppingListItem shoppingListItem, @AuthenticationPrincipal User user) {
        return shoppingListService.addShoppingListItem(user.getId(), shoppingListItem);
    }

    @DeleteMapping("/shopping-list-items/{shoppingListItemId}")
    public ResponseEntity<String> removeShoppingListItem(@PathVariable Long shoppingListItemId, @AuthenticationPrincipal User user) {
        return shoppingListService.removeShoppingListItem(user.getId(), shoppingListItemId);
    }

    @PutMapping("/shopping-list-items/editQuantity/{shoppingListItemId}")
    public ResponseEntity<String> updateShoppingListItemQuantity(@PathVariable Long shoppingListItemId
            , @RequestBody int updatedShoppingListItemQuantity) {
        return shoppingListService.updateShoppingListItemQuantity(shoppingListItemId, updatedShoppingListItemQuantity);
    }
}
