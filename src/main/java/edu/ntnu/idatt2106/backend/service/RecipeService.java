package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.Response.RecipeSortedByFridgeResponse;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeWithFridgeCount;
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

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, RecipeItemRepository recipeItemRepository, FridgeItemRepository fridgeItemRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeItemRepository = recipeItemRepository;
        this.fridgeItemRepository = fridgeItemRepository;
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


    public ResponseEntity<List<RecipeSortedByFridgeResponse>> getRecipesByFridge() {
        List<Recipe> recipes = recipeRepository.findAll();
        List<RecipeWithFridgeCount> recipeCounts = new ArrayList<>();
        for (Recipe recipe : recipes) {

            int count = getCountOfIngredientsInFridge(recipe);
            recipeCounts.add(new RecipeWithFridgeCount(recipe, count));
        }
        Collections.sort(recipeCounts);
        List<RecipeSortedByFridgeResponse> sortedRecipes = new ArrayList<>();
        for (RecipeWithFridgeCount recipeCount : recipeCounts) {

            RecipeSortedByFridgeResponse recipeSorted = new RecipeSortedByFridgeResponse();
            recipeSorted.setName(recipeCount.getRecipe().getName());
            recipeSorted.setId(recipeCount.getRecipe().getId());
            recipeSorted.setDescription(recipeCount.getRecipe().getDescription());
            recipeSorted.setNumberOfItemsRecipe(recipeCount.getRecipe().getNumberOfItems());
            recipeSorted.setNumberOfItemsFridge(recipeCount.getFridgeCount());

            sortedRecipes.add(recipeSorted);
        }
        return ResponseEntity.status(HttpStatus.OK).body(sortedRecipes);
    }

    private int getCountOfIngredientsInFridge(Recipe recipe) {
        int count = 0;
        List<RecipeItem> recipeItems = recipe.getRecipeItems();
        for (RecipeItem recipeItem : recipeItems) {
            Long itemId = recipeItem.getItem().getId();
            FridgeItem fridgeItem = fridgeItemRepository.findByItemId(itemId);
            if (fridgeItem != null && fridgeItem.getQuantity() >= recipeItem.getQuantity()) {
                count++;
            }
        }
        return count;
    }

}



