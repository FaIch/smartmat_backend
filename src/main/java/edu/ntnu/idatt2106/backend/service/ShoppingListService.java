package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItemRequest;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.FridgeItemRepository;
import edu.ntnu.idatt2106.backend.repository.ItemRepository;
import edu.ntnu.idatt2106.backend.repository.ShoppingListItemRepository;
import edu.ntnu.idatt2106.backend.repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final ItemService itemService;
    private final FridgeItemRepository fridgeItemRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository,
                               ShoppingListItemRepository shoppingListItemRepository,
                               ItemService itemService,
                               FridgeItemRepository fridgeItemRepository,
                               ItemRepository itemRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.itemService = itemService;
        this.fridgeItemRepository = fridgeItemRepository;
        this.itemRepository = itemRepository;
    }

    public ResponseEntity<List<ShoppingListItem>> getShoppingListItemsByUserId(User user) {
        List<ShoppingListItem> shoppingListItems = getShoppingListItems(user);

        if (shoppingListItems == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(shoppingListItems);
    }

    public ResponseEntity<Map<String, Integer>> getNumberOfShoppingListItemsByUserId(User user) {
        List<ShoppingListItem> shoppingListItems = getShoppingListItems(user);

        if (shoppingListItems == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        int shoppingListSize = shoppingListItems.size();
        List<Long> suggestedItemIds = getSuggestedItemIds(user.getId());
        int suggestedItemsSize = (suggestedItemIds != null) ? suggestedItemIds.size() : 0;

        Map<String, Integer> itemCounts = new HashMap<>();
        itemCounts.put("shoppingListItemsNumber", shoppingListSize);
        itemCounts.put("suggestedItemsNumber", suggestedItemsSize);
        return ResponseEntity.status(HttpStatus.OK).body(itemCounts);
    }

    public ResponseEntity<List<Item>> getSuggestedItems(Long userId) {
        List<Long> idsToSuggest = getSuggestedItemIds(userId);

        if (idsToSuggest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(itemRepository.findAllById(idsToSuggest));
    }

    private List<Long> getSuggestedItemIds(Long userId) {
        List<Long> itemIds = new ArrayList<>(Arrays.asList(2L, 7L, 11L, 12L, 13L, 18L, 23L, 34L, 36L, 37L, 38L, 40L));
        List<Long> itemIdsFromFridge = fridgeItemRepository.findItemIdsByUserId(userId);
        List<Long> itemIdsFromShoppingList = shoppingListItemRepository.findItemIdsByUserId(userId);

        if (itemIdsFromFridge == null || itemIdsFromShoppingList == null) {
            return null;
        }

        return itemIds.stream().filter(id -> !itemIdsFromFridge.contains(id) &&
                !itemIdsFromShoppingList.contains(id)).toList();
    }

    private List<ShoppingListItem> getShoppingListItems(User user) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);

        if (shoppingListOptional.isEmpty()) {
            return null;
        }

        ShoppingList shoppingList = shoppingListOptional.get();
        return shoppingListRepository.findShoppingListItemsByShoppingListId(shoppingList.getId());
    }

    public ResponseEntity<String> addListOfShoppingListItems(User user, List<ShoppingListItemRequest> shoppingListItems) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
        }
        ShoppingList shoppingList = shoppingListOptional.get();
        for (ShoppingListItemRequest shoppingListItemRequest : shoppingListItems) {
            ShoppingListItem shoppingListItem = new ShoppingListItem(shoppingListItemRequest.getQuantity(),
                    itemService.getItemById(shoppingListItemRequest.getItemId()));
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

    public ResponseEntity<String> updateShoppingListItems(User user, List<ShoppingListItemRequest> shoppingListItemRequests) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
        }
        for (ShoppingListItemRequest shoppingListItemRequest : shoppingListItemRequests) {
            Optional<ShoppingListItem> shoppingListItemOptional = shoppingListItemRepository
                    .findById(shoppingListItemRequest.getItemId());
            if (shoppingListItemOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            ShoppingListItem shoppingListItem = shoppingListItemOptional.get();
            if (!shoppingListItem.getShoppingList().getId().equals(shoppingListOptional.get().getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("The shopping list item is not in the user's shopping list");
            }

            shoppingListItem.setQuantity(shoppingListItemRequest.getQuantity());
            shoppingListItemRepository.save(shoppingListItem);
        }

        return ResponseEntity.status(HttpStatus.OK).body("Shopping list items quantity updated");
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
