package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.Response.RecipeSortedByFridgeResponse;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import edu.ntnu.idatt2106.backend.repository.FridgeItemRepository;
import edu.ntnu.idatt2106.backend.repository.RecipeRepository;
import edu.ntnu.idatt2106.backend.service.ItemService;
import edu.ntnu.idatt2106.backend.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
@RequestMapping(value = "/recipe")
public class RecipeController {

    //TODO: why need token for accessing recipe????
    private final RecipeService recipeService;
    private final ItemService itemService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private FridgeItemRepository fridgeItemRepository;

    @Autowired
    public RecipeController(RecipeService recipeService, ItemService itemService) {
        this.recipeService = recipeService;
        this.itemService = itemService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Recipe>> list(){
        return recipeService.getAllRecipes();
    }

   @GetMapping("/by-item-name")
    public List<Recipe> getRecipesByItemName(@RequestParam String itemName) {
        return recipeService.getRecipesByItemName(itemName);
    }


    //Todo: move to service
    @GetMapping("/sorted-by-fridge")
    public List<RecipeSortedByFridgeResponse> getRecipesSortedByFridge() {
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
        return sortedRecipes;
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


    //todo: Sortere recipies etter varer som snart går ut på dato ???

    private static class RecipeWithFridgeCount implements Comparable<RecipeWithFridgeCount> {
        private Recipe recipe;
        private int fridgeCount;

        public RecipeWithFridgeCount(Recipe recipe, int fridgeCount) {
            this.recipe = recipe;
            this.fridgeCount = fridgeCount;
        }

        public Recipe getRecipe() {
            return recipe;
        }

        public int getFridgeCount() {
            return fridgeCount;
        }

        @Override
        public int compareTo(RecipeWithFridgeCount other) {
            return Integer.compare(other.fridgeCount, this.fridgeCount);
        }
    }
}
