package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItemRequest;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.ShoppingListItemRepository;
import edu.ntnu.idatt2106.backend.repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final ItemService itemService;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository,
                               ShoppingListItemRepository shoppingListItemRepository, ItemService itemService) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.itemService = itemService;
    }

    public ResponseEntity<List<ShoppingListItem>> getShoppingListItemsByUserId(User user) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        ShoppingList shoppingList = shoppingListOptional.get();
        List<ShoppingListItem> allShoppingListItems = shoppingListRepository
                .findShoppingListItemsByShoppingListId(shoppingList.getId());
        List<ShoppingListItem> shoppingListItems = allShoppingListItems.stream().filter(shoppingListItem ->
                !shoppingListItem.isWishedItem()).toList();
        return ResponseEntity.status(HttpStatus.OK).body(shoppingListItems);
    }

    public ResponseEntity<String> addListOfShoppingListItems(User user, List<ShoppingListItemRequest> shoppingListItems) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
        }
        ShoppingList shoppingList = shoppingListOptional.get();
        for (ShoppingListItemRequest shoppingListItemRequest : shoppingListItems) {
            ShoppingListItem shoppingListItem = new ShoppingListItem(shoppingListItemRequest.getQuantity(),
                    itemService.getItemById(shoppingListItemRequest.getItemId()), false);
            shoppingListItem.setShoppingList(shoppingList);
            shoppingListItemRepository.save(shoppingListItem);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Shopping list items added");
    }

    public ResponseEntity<String> removeListOfShoppingListItems(User user, List<Long> itemIds) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
        }
        ShoppingList shoppingList = shoppingListOptional.get();
        for (Long itemId : itemIds) {
            Optional<ShoppingListItem> shoppingListItemOptional = shoppingListItemRepository.findById(itemId);
            if (shoppingListItemOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list item not found");
            }
            ShoppingListItem shoppingListItem = shoppingListItemOptional.get();
            if (!Objects.equals(shoppingListItem.getShoppingList().getId(), shoppingList.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list item not found in shopping list");
            }
            shoppingListItemRepository.delete(shoppingListItem);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Shopping list items deleted");
    }

    public ResponseEntity<String> updateShoppingListItem(User user, ShoppingListItemRequest shoppingListItemRequest) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
        }
        Optional<ShoppingListItem> shoppingListItemOptional = shoppingListItemRepository
                                                            .findById(shoppingListItemRequest.getItemId());
        if (shoppingListItemOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        ShoppingListItem shoppingListItem = shoppingListItemOptional.get();
        if (!shoppingListItem.getShoppingList().getId().equals(shoppingListOptional.get().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The shopping list item is not in the user's shopping list");
        }

        shoppingListItem.setQuantity(shoppingListItemRequest.getQuantity());
        shoppingListItemRepository.save(shoppingListItem);
        return ResponseEntity.status(HttpStatus.OK).body("Shopping list item quantity updated");
    }

    public ResponseEntity<String> addWishedItem(List<ShoppingListItemRequest> shoppingListItemRequests,
                                                @AuthenticationPrincipal User user) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
        }
        ShoppingList shoppingList = shoppingListOptional.get();
        for (ShoppingListItemRequest shoppingListItemRequest : shoppingListItemRequests) {
            ShoppingListItem shoppingListItem = new ShoppingListItem(shoppingListItemRequest.getQuantity(),
                    itemService.getItemById(shoppingListItemRequest.getItemId()), true);
            shoppingListItem.setShoppingList(shoppingList);
            shoppingListItemRepository.save(shoppingListItem);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Wished items added");
    }

    public ResponseEntity<List<ShoppingListItem>> getWishedItemsByUser(User user) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        ShoppingList shoppingList = shoppingListOptional.get();
        List<ShoppingListItem> allShoppingListItems = shoppingListRepository
                .findShoppingListItemsByShoppingListId(shoppingList.getId());
        List<ShoppingListItem> shoppingListItems = allShoppingListItems.stream().filter(ShoppingListItem::isWishedItem)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(shoppingListItems);
    }
}
