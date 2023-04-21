package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import edu.ntnu.idatt2106.backend.repository.FridgeItemRepository;
import edu.ntnu.idatt2106.backend.repository.RecipeItemRepository;
import edu.ntnu.idatt2106.backend.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Recipe> getAllRecipes() {
        return (List<Recipe>) recipeRepository.findAll();
    }

    public List<String> findRecipeNamesByItemName(String itemName) {
        return recipeItemRepository.findRecipeNamesByItemName(itemName);
    }


   public List<Recipe> getRecipesByItemName(String itemName) {
        List<RecipeItem> recipeItems = recipeItemRepository.findByItemName(itemName);
        List<Recipe> recipes = new ArrayList<>();
        for (RecipeItem recipeItem : recipeItems) {
            recipes.add(recipeItem.getRecipe());
        }
        return recipes;
    }

}
