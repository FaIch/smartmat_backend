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

@RestController
@CrossOrigin("*")
@RequestMapping("/wished")
public class WishedItemController {

    private final WishedItemService wishedItemService;

    @Autowired
    public WishedItemController(WishedItemService wishedItemService) {
        this.wishedItemService = wishedItemService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<WishedItem>> getWishedItems(@AuthenticationPrincipal User user) {
        return wishedItemService.getWishedItems(user);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addWishedItem(@RequestBody List<WishedItemRequest> wishedItemRequest,
                                                @AuthenticationPrincipal User user) {
        return wishedItemService.addWishedItem(user, wishedItemRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateWishedItem(@RequestBody WishedItemRequest wishedItemRequest,
                                                   @AuthenticationPrincipal User user) {
        return wishedItemService.updateWishedItem(user, wishedItemRequest);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeWishedItem(@RequestParam List<Long> wishedItemIds,
                                                   @AuthenticationPrincipal User user) {
        return wishedItemService.deleteWishedItem(user, wishedItemIds);
    }
}
