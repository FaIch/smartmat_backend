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

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    //todo: hente herfra på week menu
    @GetMapping("/list/sorted")
    public ResponseEntity<List<RecipeWithFridgeCount>> getRecipesSorted(@AuthenticationPrincipal User user) {
        return recipeService.getRecipesSorted(user);
    }

    @GetMapping("/recipe-items/{id}")
    public ResponseEntity<List<RecipeItem>> getRecipeItems(@PathVariable Long id) {
        return recipeService.getRecipeItems(id);
    }

}
