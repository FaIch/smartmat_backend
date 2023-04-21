package edu.ntnu.idatt2106.backend.controller;

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
    public List<Recipe> list(){
        return recipeService.getAllRecipes();
    }

    @GetMapping("/findByItemName")
    public List<String> getRecipesByItemName2(@RequestParam("itemName") String itemName) {
        System.out.println(itemName);
        return recipeService.findRecipeNamesByItemName(itemName);
    }


    @GetMapping("/by-item-name/{itemName}")
    public List<Recipe> getRecipesByItemName(@PathVariable String itemName) {
        itemName = "\"" + itemName + "\"";
        System.out.println(itemName);
        return recipeService.getRecipesByItemName(itemName);
    }



    //todo: sortere items etter best match i kjøleskapet


    //todo: sjekke oppskrifter opp mot varer i kjøleskap

    //todo: hente alle oppskrift med spesifikk vare

    //todo: legge til i liste med varer som går ut på dato snart

    //todo: sortere oppskrifter etter pris
}
