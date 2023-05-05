/**
 * This class defines the RecipeController, which is responsible for handling
 * HTTP requests related to recipes.
 * It provides methods for listing all recipes, getting recipes by item name,
 * getting a recipe by ID, getting sorted recipes based on the user's fridge,
 * and getting the recipe items for a specific recipe.
 */
package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeWithFridgeCount;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * Constructor with dependency injection for the RecipeService.
     * @param recipeService The RecipeService instance to be used for handling recipes.
     */
    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * Retrieves a list of all recipes.
     * @return A ResponseEntity containing a list of Recipe objects.
     */
    @GetMapping("/list")
    public ResponseEntity<List<Recipe>> list(){
        return recipeService.getAllRecipes();
    }

    /**
     * Retrieves a list of recipes by a specific item name.
     * @param itemName The name of the item to search for in the recipes.
     * @return A ResponseEntity containing a list of Recipe objects.
     */
    @GetMapping("/by-item-name")
    public ResponseEntity<List<Recipe>> getRecipesByItemName(@RequestParam String itemName) {
        return recipeService.getRecipesByItemName(itemName);
    }

    /**
     * Retrieves a recipe by its ID.
     * @param id The ID of the recipe to retrieve.
     * @return A ResponseEntity containing a Recipe object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    /**
     * Retrieves a list of recipes sorted based on the user's fridge.
     * @param user The authenticated user making the request.
     * @return A ResponseEntity containing a list of RecipeWithFridgeCount objects.
     */
    @GetMapping("/list/sorted")
    public ResponseEntity<List<RecipeWithFridgeCount>> getRecipesSorted(@AuthenticationPrincipal User user, @RequestParam int amount) {
        return recipeService.getRecipesSorted(user, amount);
    }

    /**
     * Retrieves a list of recipe items for a specific recipe by its ID.
     * @param id The ID of the recipe to get the items for.
     * @return A ResponseEntity containing a list of RecipeItem objects.
     */
    @GetMapping("/recipe-items/{id}")
    public ResponseEntity<List<RecipeItem>> getRecipeItems(@PathVariable Long id) {
        return recipeService.getRecipeItems(id);
    }
}