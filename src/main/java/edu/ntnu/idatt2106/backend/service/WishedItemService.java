package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.shoppinglist.*;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.ShoppingListRepository;
import edu.ntnu.idatt2106.backend.repository.WishedItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WishedItemService {

    private final WishedItemRepository wishedItemRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final ItemService itemService;

    public WishedItemService(WishedItemRepository wishedItemRepository, ShoppingListRepository shoppingListRepository,
                             ItemService itemService) {
        this.wishedItemRepository = wishedItemRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.itemService = itemService;
    }

    public ResponseEntity<String> addWishedItem(User user, List<WishedItemRequest> wishedItemRequests) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
        }
        ShoppingList shoppingList = shoppingListOptional.get();
        for (WishedItemRequest wishedItemRequest : wishedItemRequests) {
            Item item = itemService.getItemById(wishedItemRequest.getItemId());
            Optional<WishedItem> optionalWishedItem = wishedItemRepository.findByShoppingListAndItem(shoppingList, item);

            if (optionalWishedItem.isPresent()) {
                WishedItem existingWishedItem = optionalWishedItem.get();
                existingWishedItem.setQuantity(existingWishedItem.getQuantity() + wishedItemRequest.getQuantity());
                wishedItemRepository.save(existingWishedItem);
            }
            else {
                WishedItem wishedItem = new WishedItem(wishedItemRequest.getQuantity(), item);
                wishedItem.setShoppingList(shoppingList);
                wishedItemRepository.save(wishedItem);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Shopping list items added");
    }

    public ResponseEntity<String> deleteWishedItem(User user, List<Long> ids) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
        }
        ShoppingList shoppingList = shoppingListOptional.get();
        for (Long itemId : ids) {
            Optional<WishedItem> optionalWishedItem = wishedItemRepository.findById(itemId);
            if (optionalWishedItem.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wished item not found");
            }
            WishedItem wishedItem = optionalWishedItem.get();
            if (!Objects.equals(wishedItem.getShoppingList().getId(), shoppingList.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wished item not found in shopping list");
            }
            wishedItemRepository.delete(wishedItem);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Wished items deleted");
    }

    public ResponseEntity<String> updateWishedItem(User user, List<WishedItemRequest> wishedItemRequests) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
        }

        for (WishedItemRequest wishedItemRequest : wishedItemRequests) {
            Optional<WishedItem> wishedItemOptional =
                    wishedItemRepository.findById(wishedItemRequest.getItemId());

            if (wishedItemOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wished item not found");
            }

            WishedItem wishedItem = wishedItemOptional.get();
            if (!wishedItem.getShoppingList().getId().equals(shoppingListOptional.get().getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("The shopping list item is not in the user's shopping list");
            }

            wishedItem.setQuantity(wishedItemRequest.getQuantity());
            wishedItemRepository.save(wishedItem);
        }

        return ResponseEntity.status(HttpStatus.OK).body("Shopping list items quantity updated");
    }

    public ResponseEntity<List<WishedItem>> getWishedItems(User user) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        ShoppingList shoppingList = shoppingListOptional.get();
        List<WishedItem> wishedItems = wishedItemRepository.findAllByShoppingList(shoppingList);
        return ResponseEntity.status(HttpStatus.OK).body(wishedItems);
    }
}
