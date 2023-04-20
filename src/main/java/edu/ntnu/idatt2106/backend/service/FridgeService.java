package edu.ntnu.idatt2106.backend.service;


import edu.ntnu.idatt2106.backend.model.fridge.Fridge;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.FridgeItemRepository;
import edu.ntnu.idatt2106.backend.repository.FridgeRepository;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FridgeService {

    private final UserRepository userRepository;
    private final FridgeRepository fridgeRepository;
    private final FridgeItemRepository fridgeItemRepository;

    /**
     * Constructor for FridgeService that injects dependencies for the UserRepository, FridgeRepository,
     * and FridgeItemRepository.
     *
     * @param userRepository       the UserRepository
     * @param fridgeRepository     the FridgeRepository
     * @param fridgeItemRepository the FridgeItemRepository
     */
    @Autowired
    public FridgeService(UserRepository userRepository, FridgeRepository fridgeRepository
            , FridgeItemRepository fridgeItemRepository) {
        this.userRepository = userRepository;
        this.fridgeRepository = fridgeRepository;
        this.fridgeItemRepository = fridgeItemRepository;
    }

    /**
     * Gets a list of fridge items belonging to a specific user.
     *
     * @param userId the ID of the user
     * @return a ResponseEntity containing the list of fridge items if the user is found, or a NOT_FOUND status
     * code if the user is not found
     */
    public ResponseEntity<List<FridgeItem>> getFridgeItemsByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        User user = userOptional.get();
        Optional<Fridge> fridgeOptional = fridgeRepository.findFridgeByUser(user);
        return fridgeOptional.map(fridge -> ResponseEntity.status(HttpStatus.OK).body(fridge.getFridgeItems()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    /**
     * Adds a new fridge item to a specific fridge.
     *
     * @param fridgeItem the new fridge item to be added
     * @return a ResponseEntity containing a "Fridge item added" message if the fridge is found and the item is
     * added successfully, or a NOT_FOUND status code if the fridge is not found
     */
    public ResponseEntity<String> addFridgeItem(Long userId, FridgeItem fridgeItem) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        Optional<Fridge> fridgeOptional = fridgeRepository.findFridgeByUser(userOptional.get());
        if (fridgeOptional.isPresent()) {
            Fridge fridge = fridgeOptional.get();
            fridgeItem.setFridge(fridge);
            fridgeItemRepository.save(fridgeItem);
            return ResponseEntity.status(HttpStatus.OK).body("Fridge item added");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fridge not found");
    }

    /**
     * Removes a fridge item from the database.
     *
     * @param fridgeItemId the ID of the fridge item to be removed
     * @return a ResponseEntity containing a "Fridge item removed" message if the item is found and removed
     * successfully, or a NOT_FOUND status code if the item is not found
     */
    public ResponseEntity<String> removeFridgeItem(Long fridgeItemId) {
        Optional<FridgeItem> fridgeItemOptional = fridgeItemRepository.findById(fridgeItemId);
        if (fridgeItemOptional.isPresent()) {
            fridgeItemRepository.deleteById(fridgeItemId);
            return ResponseEntity.status(HttpStatus.OK).body("Fridge item removed");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fridge item not found");
    }

    /**
     * Updates the quantity of a specific fridge item.
     *
     * @param fridgeItemId      the ID of the fridge item to be updated
     * @param updatedFridgeItem the updated fridge item object
     * @return a ResponseEntity containing a "Fridge item quantity updated" message if the item is found and updated
     * successfully, or a NOT_FOUND status code if the item is not found
     */
    public ResponseEntity<String> updateFridgeItemQuantity(Long fridgeItemId, FridgeItem updatedFridgeItem) {
        Optional<FridgeItem> fridgeItemOptional = fridgeItemRepository.findById(fridgeItemId);
        if (fridgeItemOptional.isPresent()) {
            FridgeItem existingFridgeItem = fridgeItemOptional.get();
            existingFridgeItem.setQuantity(updatedFridgeItem.getQuantity());
            fridgeItemRepository.save(existingFridgeItem);
            return ResponseEntity.status(HttpStatus.OK).body("Fridge item quantity updated");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fridge item not found");
    }

    /**
     * Updates the expiration date of a specific fridge item.
     *
     * @param fridgeItemId      the ID of the fridge item to be updated
     * @param updatedFridgeItem the updated fridge item object
     * @return a ResponseEntity containing a "Fridge item expiration date updated" message if the item is found and
     * updated successfully, or a NOT_FOUND status code if the item is not found
     */
    public ResponseEntity<String> updateFridgeItemExpirationDate(Long fridgeItemId, FridgeItem updatedFridgeItem) {
        Optional<FridgeItem> fridgeItemOptional = fridgeItemRepository.findById(fridgeItemId);
        if (fridgeItemOptional.isPresent()) {
            FridgeItem existingFridgeItem = fridgeItemOptional.get();
            existingFridgeItem.setExpirationDate(updatedFridgeItem.getExpirationDate());
            fridgeItemRepository.save(existingFridgeItem);
            return ResponseEntity.status(HttpStatus.OK).body("Fridge item expiration date updated");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fridge item not found");
    }
}
