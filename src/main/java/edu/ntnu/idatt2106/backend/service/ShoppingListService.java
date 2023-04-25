package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItemRequest;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.ShoppingListItemRepository;
import edu.ntnu.idatt2106.backend.repository.ShoppingListRepository;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ShoppingListService {

    private final UserRepository userRepository;

    private final ShoppingListRepository shoppingListRepository;

    private final ShoppingListItemRepository shoppingListItemRepository;

    @Autowired
    public ShoppingListService(UserRepository userRepository, ShoppingListRepository shoppingListRepository, ShoppingListItemRepository shoppingListItemRepository) {
        this.userRepository = userRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    public ResponseEntity<List<ShoppingListItem>> getShoppingListItemsByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User user = userOptional.get();
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        ShoppingList shoppingList = shoppingListOptional.get();
        List<ShoppingListItem> shoppingListItems = shoppingListRepository.findShoppingListItemsByShoppingListId(shoppingList.getId());
        return ResponseEntity.status(HttpStatus.OK).body(shoppingListItems);
    }

    public ResponseEntity<String> addShoppingListItem(Long userId, ShoppingListItem shoppingListItem) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        User user = userOptional.get();
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        ShoppingList shoppingList = shoppingListOptional.get();
        shoppingListItem.setShoppingList(shoppingList);
        shoppingListItemRepository.save(shoppingListItem);
        return ResponseEntity.status(HttpStatus.OK).body("Shopping list item added");
    }

    public ResponseEntity<String> addListOfShoppingListItems(User user, List<ShoppingListItem> shoppingListItems) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
        }
        ShoppingList shoppingList = shoppingListOptional.get();
        for (ShoppingListItem shoppingListItem : shoppingListItems) {
            shoppingListItem.setShoppingList(shoppingList);
            shoppingListItemRepository.save(shoppingListItem);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Shopping list items added");
    }

    public ResponseEntity<String> removeShoppingListItem(Long userId, Long shoppingListItemId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        User user = userOptional.get();
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findShoppingListByUser(user);
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        ShoppingList shoppingList = shoppingListOptional.get();
        Optional<ShoppingListItem> shoppingListItemOptional = shoppingListItemRepository.findById(shoppingListItemId);
        if (shoppingListItemOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        ShoppingListItem shoppingListItem = shoppingListItemOptional.get();
        if (shoppingListItem.getShoppingList().getId() != shoppingList.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        shoppingListItemRepository.delete(shoppingListItem);
        return ResponseEntity.status(HttpStatus.OK).body("Shopping list item deleted");
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

    public ResponseEntity<String> updateShoppingListItemQuantity(Long shoppingListItemId, int quantity) {
        Optional<ShoppingListItem> shoppingListItemOptional = shoppingListItemRepository.findById(shoppingListItemId);
        if (shoppingListItemOptional.isPresent()) {
            ShoppingListItem shoppingListItem = shoppingListItemOptional.get();
            shoppingListItem.setQuantity(quantity);
            shoppingListItemRepository.save(shoppingListItem);
            return ResponseEntity.status(HttpStatus.OK).body("Shopping list item quantity updated");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
