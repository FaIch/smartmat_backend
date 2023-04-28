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

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeItemRepository recipeItemRepository;
    private final FridgeItemRepository fridgeItemRepository;
    private final FridgeItemService fridgeItemService;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, RecipeItemRepository recipeItemRepository,
                         FridgeItemRepository fridgeItemRepository, FridgeItemService fridgeItemService) {
        this.recipeRepository = recipeRepository;
        this.recipeItemRepository = recipeItemRepository;
        this.fridgeItemRepository = fridgeItemRepository;
        this.fridgeItemService = fridgeItemService;
    }

    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.status(HttpStatus.OK).body(recipeRepository.findAll());
    }


    public ResponseEntity<List<Recipe>> getRecipesByItemName(String itemName) {
        System.out.println(itemName);
        List<RecipeItem> recipeItems = recipeItemRepository.findByItemName(itemName);
        List<Recipe> recipes = new ArrayList<>();
        for (RecipeItem recipeItem : recipeItems) {
            recipes.add(recipeItem.getRecipe());
        }
        return ResponseEntity.status(HttpStatus.OK).body(recipes);
    }

    public ResponseEntity<List<RecipeWithFridgeCount>> getRecipesSorted(User user) {
        List<Recipe> recipes = recipeRepository.findAll();
        List<Long> fridgeItemIds = fridgeItemRepository.findItemIdsByUserId(user.getId());
        List<Long> expiringFridgeItemIds = fridgeItemService.getExpiringItemIdsByUserId(user.getId());
        List<RecipeWithFridgeCount> recipeWithFridgeCounts = new ArrayList<>();
        for (Recipe recipe : recipes) {
            List<Long> recipeItemIds = recipeItemRepository.findItemIdsByRecipeId(recipe.getId());
            int fridgeCount = (int) recipeItemIds.stream().filter(fridgeItemIds::contains).count();
            int expiringCount = (int) recipeItemIds.stream().filter(expiringFridgeItemIds::contains).count();
            recipeWithFridgeCounts.add(new RecipeWithFridgeCount(recipe, fridgeCount, expiringCount));
        }
        return ResponseEntity.status(HttpStatus.OK).body(recipeWithFridgeCounts);
    }

}



