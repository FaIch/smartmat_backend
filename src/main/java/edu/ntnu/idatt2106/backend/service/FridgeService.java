package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.fridge.Fridge;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItemRequest;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.FridgeItemRepository;
import edu.ntnu.idatt2106.backend.repository.FridgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class FridgeService {

    private final FridgeRepository fridgeRepository;
    private final FridgeItemRepository fridgeItemRepository;
    private final ItemService itemService;

    /**
     * Constructor for the FridgeService class.
     *
     * @param fridgeRepository     the repository for the fridge
     * @param fridgeItemRepository the repository for the fridge items
     * @param itemService          the service for the items
     */
    @Autowired
    public FridgeService(FridgeRepository fridgeRepository
            , FridgeItemRepository fridgeItemRepository, ItemService itemService) {
        this.fridgeRepository = fridgeRepository;
        this.fridgeItemRepository = fridgeItemRepository;
        this.itemService = itemService;
    }

    /**
     * Gets a list of fridge items belonging to a specific user.
     *
     * @param user the user whose fridge items are to be retrieved
     * @return a ResponseEntity containing the list of fridge items if the user is found, or a NOT_FOUND status
     * code if the user is not found
     */
    public ResponseEntity<List<FridgeItem>> getFridgeItemsByUserId(User user) {
        Optional<Fridge> fridgeOptional = fridgeRepository.findFridgeByUser(user);
        if (fridgeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Fridge fridge = fridgeOptional.get();
        List<FridgeItem> allFridgeItems = fridgeRepository.findFridgeItemsByFridgeId(fridge.getId());
        List<FridgeItem> fridgeItems = new ArrayList<>();
        for (FridgeItem fridgeItem : allFridgeItems) {
            if (fridgeItem.getExpirationDate().isAfter(LocalDate.now().minusDays(1))) {
                fridgeItems.add(fridgeItem);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(fridgeItems);
    }

    /**
     * Gets the number of fridge items, split into expired and unexpired items
     * @param user the user whose fridge items are to be counted
     * @return a ResponseEntity containing a map with the number of expired and unexpired items if the user is found
     */
    public ResponseEntity<Map<String, Integer>> getNumberOfFridgeItemsByUserID(User user) {
        Optional<Fridge> fridgeOptional = fridgeRepository.findFridgeByUser(user);
        if (fridgeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Fridge fridge = fridgeOptional.get();
        List<FridgeItem> allFridgeItems = fridgeRepository.findFridgeItemsByFridgeId(fridge.getId());
        int numberOfUnexpiredItems = 0;
        int numberOfExpiredItems = 0;
        for (FridgeItem fridgeItem : allFridgeItems) {
            if (fridgeItem.getExpirationDate().isAfter(LocalDate.now().minusDays(1))) {
                numberOfUnexpiredItems++;
            } else {
                numberOfExpiredItems++;
            }
        }
        Map<String, Integer> itemCounts = new HashMap<>();
        itemCounts.put("expired", numberOfExpiredItems);
        itemCounts.put("unexpired", numberOfUnexpiredItems);
        return ResponseEntity.status(HttpStatus.OK).body(itemCounts);
    }

    /**
     * Gets a list of expired fridge items belonging to a specific user.
     *
     * @param user the user whose expired fridge items are to be retrieved
     * @return a ResponseEntity containing the list of expired fridge items
     */
    public ResponseEntity<List<FridgeItem>> getExpiredFridgeItemsByUserId(User user) {
        Optional<Fridge> fridgeOptional = fridgeRepository.findFridgeByUser(user);
        if (fridgeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Fridge fridge = fridgeOptional.get();
        List<FridgeItem> allFridgeItems = fridgeRepository.findFridgeItemsByFridgeId(fridge.getId());
        List<FridgeItem> fridgeItems = new ArrayList<>();
        for (FridgeItem fridgeItem : allFridgeItems) {
            if (fridgeItem.getExpirationDate().isBefore(LocalDate.now())) {
                fridgeItems.add(fridgeItem);
            }
        }
        fridgeItems.sort(Comparator.comparing(FridgeItem::getExpirationDate));
        return ResponseEntity.status(HttpStatus.OK).body(fridgeItems);
    }

    public ResponseEntity<List<FridgeItem>> getAlmostExpired(User user){
        Optional<Fridge> fridgeOptional = fridgeRepository.findFridgeByUser(user);
        if (fridgeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Fridge fridge = fridgeOptional.get();
        List<FridgeItem> allFridgeItems = fridgeRepository.findFridgeItemsByFridgeId(fridge.getId());
        List<FridgeItem> fridgeItems = new ArrayList<>();
        for (FridgeItem fridgeItem : allFridgeItems) {
            if (fridgeItem.getExpirationDate().isBefore(LocalDate.now().plusDays(4)) && fridgeItem.getExpirationDate().isAfter(LocalDate.now().minusDays(1))) {
                fridgeItems.add(fridgeItem);
            }
        }
        fridgeItems.sort(Comparator.comparing(FridgeItem::getExpirationDate));
        return ResponseEntity.status(HttpStatus.OK).body(fridgeItems);
    }

    /**
     * Adds a list of fridge items
     *
     * @param user the user whose fridge items are to be added
     * @param fridgeItemRequests the list of fridge items to be added
     * @return a ResponseEntity containing a message indicating whether the fridge items were added successfully
     */
    public ResponseEntity<String> addListOfFridgeItems(User user, List<FridgeItemRequest> fridgeItemRequests) {
        Optional<Fridge> fridgeOptional = fridgeRepository.findFridgeByUser(user);
        List<FridgeItem> fridgeItems = convertListOfFridgeItemsRequestsToFridgeItems(fridgeItemRequests);
        if (fridgeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fridge not found");
        }
        Fridge fridge = fridgeOptional.get();
        for (FridgeItem fridgeItem : fridgeItems) {
            fridgeItem.setFridge(fridge);
            fridgeItemRepository.save(fridgeItem);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Fridge items added");
    }

    /**
     * Removes a list of fridge items from the database.
     *
     * @param fridgeItemIds the list of IDs of the fridge items to be removed
     * @param user the User of which fridge the fridge items are in
     * @return a ResponseEntity containing a message indicating if the item was removed successfully or not
     */
    public ResponseEntity<String> removeListOfFridgeItems(List<Long> fridgeItemIds, User user) {
        for (Long fridgeItemId : fridgeItemIds) {
            Optional<FridgeItem> fridgeItemOptional = fridgeItemRepository.findById(fridgeItemId);
            if (fridgeItemOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fridge item not found");
            }
            FridgeItem fridgeItem = fridgeItemOptional.get();
            if (!fridgeItem.getFridge().getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User does not own this fridge item");
            }
            fridgeItemRepository.delete(fridgeItem);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Fridge items removed");
    }

    /**
     * Updates the expiration date and quantity of a specific fridge item.
     *
     * @param updatedFridgeItem the updated fridge item object
     * @return a ResponseEntity containing the updated fridge item with a status code
     */
    public ResponseEntity<FridgeItem> editFridgeItem(FridgeItemRequest updatedFridgeItem, User user) {
        Optional<FridgeItem> fridgeItemOptional = fridgeItemRepository.findById(updatedFridgeItem.getItemId());
        if (fridgeItemOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        FridgeItem existingFridgeItem = fridgeItemOptional.get();
        if (!existingFridgeItem.getFridge().getUser().getId().equals(user.getId())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        existingFridgeItem.setExpirationDate(updatedFridgeItem.getExpirationDate());
        existingFridgeItem.setQuantity(updatedFridgeItem.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(fridgeItemRepository.save(existingFridgeItem));
    }

    /**
     * Removes a list of fridge items from the database.
     *
     * @param items the list of fridge items to be removed
     * @param user the User of which fridge the fridge items are in
     * @return a ResponseEntity containing a message indicating if the item was removed successfully or not
     */
    public ResponseEntity<String> removeFridgeItemsByRecipe(List<FridgeItemRequest> items, User user) {
        for (FridgeItemRequest item : items) {
            int quantity = item.getQuantity();
            List<FridgeItem> fridgeItems = fridgeItemRepository.findByUserIdAndItemId(user.getId(), item.getItemId());
            for (FridgeItem fridgeItem : fridgeItems) {
                if (fridgeItem.getQuantity() > quantity) {
                    fridgeItem.setQuantity(fridgeItem.getQuantity() - quantity);
                    fridgeItemRepository.save(fridgeItem);
                    break;
                } else {
                    quantity -= fridgeItem.getQuantity();
                    fridgeItemRepository.delete(fridgeItem);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Fridge items removed");
    }

    /**
     * Converts a list of FridgeItemRequests to a list of FridgeItems
     *
     * @param fridgeItemRequests the list of FridgeItemRequests to be converted
     * @return a list of fridgeItems generated with the values of the fridgeItemRequests
     */
    private List<FridgeItem> convertListOfFridgeItemsRequestsToFridgeItems(List<FridgeItemRequest> fridgeItemRequests) {
        List<FridgeItem> fridgeItems = new ArrayList<>();

        for (FridgeItemRequest fridgeItemRequest : fridgeItemRequests) {
            FridgeItem fridgeItem = new FridgeItem();
            fridgeItem.setItem(itemService.getItemById(fridgeItemRequest.getItemId()));
            LocalDate date = LocalDate.now().plusDays(getGoodForDaysFromCategory(fridgeItem
                    .getItem().getCategory().toString()));
            fridgeItem.setQuantity(fridgeItemRequest.getQuantity());
            fridgeItem.setExpirationDate(date);
            fridgeItems.add(fridgeItem);
        }
        return fridgeItems;
    }

    /**
     * Gets the number of days a fridge item is good for based on its category
     *
     * @param category the category of the fridge item
     * @return the number of days the fridge item is good for
     */
    private int getGoodForDaysFromCategory(String category) {
        return switch (category) {
            case "FISH", "CHICKEN", "MEAT", "VEGETABLES" -> 5;
            case "DAIRY" -> 7;
            case "EGG" -> 14;
            case "CHEESE" -> 20;
            default -> 60;
        };
    }
}
