package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.repository.RecipeRepository;
import edu.ntnu.idatt2106.backend.service.ItemService;
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

    //TODO: why need token for accessing recipe????
    private final RecipeService recipeService;
    private final ItemService itemService;


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

/*   @GetMapping("/sorted-by-fridge-items")
    public List<Recipe> getRecipesSortedByFridgeItems() {
        return recipeService.getAllRecipesSortedByFridgeItems();
    }*/

 /*   @GetMapping("/sortedByFridgeItemsDate")
    public List<Recipe> getRecipesSortedByFridgeItemsDate() {
        return recipeService.getRecipesSortedByFridgeItems();
    }*/




    //todo: sortere items etter best match i kjøleskapet


    //todo: sjekke oppskrifter opp mot varer i kjøleskap

    //todo: hente alle oppskrift med spesifikk vare

    //todo: legge til i liste med varer som går ut på dato snart

}
