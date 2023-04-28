package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.FridgeItemRepository;
import edu.ntnu.idatt2106.backend.repository.ItemRepository;
import edu.ntnu.idatt2106.backend.repository.ShoppingListItemRepository;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuggestionService {
    private final ItemRepository itemRepository;
    private final FridgeItemRepository fridgeItemRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;

    @Autowired
    public SuggestionService(ItemRepository itemRepository, FridgeItemRepository fridgeItemRepository,
                             ShoppingListItemRepository shoppingListItemRepository) {
        this.itemRepository = itemRepository;
        this.fridgeItemRepository = fridgeItemRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    public ResponseEntity<List<Item>> getSuggestedItems(Long userId) {
        List<Long> itemIds = new ArrayList<>(Arrays.asList(2L, 7L, 11L, 12L, 13L, 18L, 23L, 34L, 36L, 37L, 38L, 40L));
        List<Long> itemIdsFromFridge = fridgeItemRepository.findItemIdsByUserId(userId);
        List<Long> itemIdsFromShoppingList = shoppingListItemRepository.findItemIdsByUserId(userId);
        List<Long> idsToSuggest = itemIds.stream().filter(id -> !itemIdsFromFridge.contains(id) &&
                !itemIdsFromShoppingList.contains(id)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(itemRepository.findAllById(idsToSuggest));
    }
}