package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.shoppinglist.WishedItem;
import edu.ntnu.idatt2106.backend.model.shoppinglist.WishedItemRequest;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.WishedItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * WishedItemController is a REST controller responsible for managing wished item-related operations
 * such as retrieving, adding, updating, and removing wished items for a user.
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/wished")
public class WishedItemController {

    private final WishedItemService wishedItemService;

    /**
     * Constructs a WishedItemController with the provided WishedItemService instance.
     *
     * @param wishedItemService an instance of WishedItemService
     */
    @Autowired
    public WishedItemController(WishedItemService wishedItemService) {
        this.wishedItemService = wishedItemService;
    }

    /**
     * Retrieves the wished items for the specified user.
     *
     * @param user the authenticated user
     * @return ResponseEntity containing the list of wished items for the user
     */
    @GetMapping("/get")
    public ResponseEntity<List<WishedItem>> getWishedItems(@AuthenticationPrincipal User user) {
        return wishedItemService.getWishedItems(user);
    }

    /**
     * Adds wished items for the specified user.
     *
     * @param wishedItemRequest the list of wished items to be added
     * @param user the authenticated user
     * @return ResponseEntity containing the result of the operation
     */
    @PostMapping("/add")
    public ResponseEntity<String> addWishedItem(@RequestBody List<WishedItemRequest> wishedItemRequest,
                                                @AuthenticationPrincipal User user) {
        return wishedItemService.addWishedItem(user, wishedItemRequest);
    }

    /**
     * Updates wished items for the specified user.
     *
     * @param wishedItemRequest the list of wished items to be updated
     * @param user the authenticated user
     * @return ResponseEntity containing the result of the operation
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateWishedItem(@RequestBody List<WishedItemRequest> wishedItemRequest,
                                                   @AuthenticationPrincipal User user) {
        return wishedItemService.updateWishedItem(user, wishedItemRequest);
    }

    /**
     * Removes wished items for the specified user.
     *
     * @param wishedItemIds the list of wished item IDs to be removed
     * @param user the authenticated user
     * @return ResponseEntity containing the result of the operation
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeWishedItem(@RequestParam List<Long> wishedItemIds,
                                                   @AuthenticationPrincipal User user) {
        return wishedItemService.deleteWishedItem(user, wishedItemIds);
    }
}
