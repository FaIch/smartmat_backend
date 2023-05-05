package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeWithFridgeCount;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.FridgeItemRepository;
import edu.ntnu.idatt2106.backend.repository.RecipeItemRepository;
import edu.ntnu.idatt2106.backend.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * RecipeService is a service class responsible for handling the business logic related to recipes.
 * It uses various repositories to interact with the database and retrieve the necessary data.
 */
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeItemRepository recipeItemRepository;
    private final FridgeItemRepository fridgeItemRepository;
    private final FridgeItemService fridgeItemService;

    /**
     * Constructs a RecipeService with the provided repository and service instances.
     *
     * @param recipeRepository     an instance of RecipeRepository
     * @param recipeItemRepository an instance of RecipeItemRepository
     * @param fridgeItemRepository an instance of FridgeItemRepository
     * @param fridgeItemService    an instance of FridgeItemService
     */
    @Autowired
    public RecipeService(RecipeRepository recipeRepository, RecipeItemRepository recipeItemRepository,
                         FridgeItemRepository fridgeItemRepository, FridgeItemService fridgeItemService) {
        this.recipeRepository = recipeRepository;
        this.recipeItemRepository = recipeItemRepository;
        this.fridgeItemRepository = fridgeItemRepository;
        this.fridgeItemService = fridgeItemService;
    }

    /**
     * Retrieves all recipes from the database.
     *
     * @return ResponseEntity containing a list of all recipes
     */
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.status(HttpStatus.OK).body(recipeRepository.findAll());
    }

    /**
     * Retrieves a recipe with the specified ID from the database.
     *
     * @param id the ID of the recipe to retrieve
     * @return ResponseEntity containing the recipe with the specified ID
     */
    public ResponseEntity<Recipe> getRecipeById(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(recipeRepository.findById(id).orElse(null));
    }

    /**
     * Retrieves a list of recipes containing an item with the specified name.
     *
     * @param itemName the name of the item to search for in the recipes
     * @return ResponseEntity containing a list of recipes with the specified item
     */
    public ResponseEntity<List<Recipe>> getRecipesByItemName(String itemName) {
        System.out.println(itemName);
        List<RecipeItem> recipeItems = recipeItemRepository.findByItemName(itemName);
        List<Recipe> recipes = new ArrayList<>();
        for (RecipeItem recipeItem : recipeItems) {
            recipes.add(recipeItem.getRecipe());
        }
        return ResponseEntity.status(HttpStatus.OK).body(recipes);
    }

    /**
     * Retrieves a sorted list of recipes with their fridge count and nearly expired count.
     * Sorted by the number of items in the fridge and nearly expired items.
     *
     * @param user an instance of the User for whom to retrieve the data
     * @return ResponseEntity containing a sorted list of recipes with fridge and nearly expired counts
     */
    public ResponseEntity<List<RecipeWithFridgeCount>> getRecipesSorted(User user, int amount) {
        List<Recipe> recipes = recipeRepository.findAll();
        List<FridgeItem> fridgeItems = fridgeItemRepository.findByUserId(user.getId());
        List<Long> expiringFridgeItemIds = fridgeItemService.getExpiringItemIdsByUserId(user.getId());
        List<RecipeWithFridgeCount> recipeWithFridgeCounts = new ArrayList<>();
        for (Recipe recipe : recipes) {
            List<Long> recipeItemIds = recipeItemRepository.findItemIdsByRecipeId(recipe.getId());
            int fridgeCount = 0;
            for (FridgeItem item : fridgeItems) {
                if (recipeItemIds.contains(item.getItem().getId())) {
                    if (recipeItemRepository.findByItemIdAndRecipeId(item.getItem().getId(), recipe.getId()).getQuantity() * amount / 4 <= item.getQuantity()) {
                        fridgeCount++;
                    }
                }
            }
            int expiringCount = (int) recipeItemIds.stream().filter(expiringFridgeItemIds::contains).count();
            recipeWithFridgeCounts.add(new RecipeWithFridgeCount(recipe.getId(), recipe, fridgeCount, expiringCount));
        }
        recipeWithFridgeCounts.sort(Comparator.comparingInt(RecipeWithFridgeCount::getAmountInFridge).reversed());
        recipeWithFridgeCounts.sort(Comparator.comparingInt(RecipeWithFridgeCount::getAmountNearlyExpired).reversed());
        return ResponseEntity.status(HttpStatus.OK).body(recipeWithFridgeCounts);
    }

    /**
     * Retrieves a list of RecipeItems associated with the specified recipe ID.
     *
     * @param id the ID of the recipe for which to retrieve the RecipeItems
     * @return ResponseEntity containing a list of RecipeItems associated with the specified recipe
     */
    public ResponseEntity<List<RecipeItem>> getRecipeItems(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(recipeItemRepository.findAllByRecipeId(id));
    }
}



