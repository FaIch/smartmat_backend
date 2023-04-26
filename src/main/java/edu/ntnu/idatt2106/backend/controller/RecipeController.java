package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.Response.RecipeSortedByFridgeResponse;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
@RequestMapping(value = "/recipe")
public class RecipeController {

    private final RecipeService recipeService;


    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Recipe>> list(){
        return recipeService.getAllRecipes();
    }

   @GetMapping("/by-item-name")
    public ResponseEntity<List<Recipe>> getRecipesByItemName(@RequestParam String itemName) {
        return recipeService.getRecipesByItemName(itemName);
    }

    @GetMapping("/sorted-by-fridge")
    public ResponseEntity<List<RecipeSortedByFridgeResponse>> getRecipesSortedByFridge() {
        return recipeService.getRecipesByFridge();
    }

 //todo: Sortere recipies etter varer som snart går ut på dato ???



}
