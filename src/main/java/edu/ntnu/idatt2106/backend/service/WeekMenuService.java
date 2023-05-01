package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenu;
import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenuData;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeWithFridgeCount;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.FridgeItemRepository;
import edu.ntnu.idatt2106.backend.repository.RecipeItemRepository;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
import edu.ntnu.idatt2106.backend.repository.WeekMenuRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class WeekMenuService {

    private final WeekMenuRepository weekMenuRepository;
    private final RecipeItemRepository recipeItemRepository;

    private final RecipeService recipeService;

    private final FridgeItemService fridgeItemService;
    private final FridgeItemRepository fridgeItemRepository;

    @Autowired
    public WeekMenuService(WeekMenuRepository weekMenuRepository, RecipeItemRepository recipeItemRepository, RecipeService recipeService, FridgeItemService fridgeItemService, FridgeItemRepository fridgeItemRepository) {
        this.weekMenuRepository = weekMenuRepository;
        this.recipeItemRepository = recipeItemRepository;
        this.recipeService = recipeService;
        this.fridgeItemService = fridgeItemService;
        this.fridgeItemRepository = fridgeItemRepository;
    }

    public ResponseEntity<List<Recipe>> getRandomWeekMenu() {
        List<Recipe> allRecipes = recipeService.getAllRecipes().getBody(); // get all recipes
        List<Recipe> randomRecipes = new ArrayList<>();

        assert allRecipes != null;
        Collections.shuffle(allRecipes);

        for (int i = 0; i < Math.min(5, allRecipes.size()); i++) {
            randomRecipes.add(allRecipes.get(i));
        }

        return new ResponseEntity<>(randomRecipes, HttpStatus.OK);
    }

    public ResponseEntity<WeekMenuData> getDataWeekMenu(List<Integer> recipeIds, User user) {
        recipeIds.remove(null);
        List<Recipe> recipes = getRecipesById(recipeIds);

        List<Long> fridgeItemIds = fridgeItemRepository.findItemIdsByUserId(user.getId());
        List<Long> expiringFridgeItemIds = fridgeItemService.getExpiringItemIdsByUserId(user.getId());
        List<RecipeWithFridgeCount> recipeWithFridgeCounts = new ArrayList<>();

        for (Recipe recipe : recipes) {
            List<Long> recipeItemIds = recipeItemRepository.findItemIdsByRecipeId(recipe.getId());
            int fridgeCount = (int) recipeItemIds.stream().filter(fridgeItemIds::contains).count();
            int expiringCount = (int) recipeItemIds.stream().filter(expiringFridgeItemIds::contains).count();
            recipeWithFridgeCounts.add(new RecipeWithFridgeCount(recipe.getId(), recipe, fridgeCount, expiringCount));
        }

        int totalAmountOfItems = 0;
        int totalAmountMissingItems = 0;
        int totalAmountOfItemsToExpire = 0;

        for (RecipeWithFridgeCount recipeWithFridgeCount: recipeWithFridgeCounts) {
            totalAmountOfItems += recipeWithFridgeCount.getRecipe().getNumberOfItems();
            totalAmountMissingItems += recipeWithFridgeCount.getRecipe().getNumberOfItems() - recipeWithFridgeCount.getAmountInFridge();
            totalAmountOfItemsToExpire += recipeWithFridgeCount.getAmountNearlyExpired();
        }
        WeekMenuData weekMenuData = new WeekMenuData(totalAmountOfItems,totalAmountMissingItems, totalAmountOfItemsToExpire);
        return ResponseEntity.ok(weekMenuData);
    }


    public ResponseEntity<List<RecipeWithFridgeCount>> getRecipesById(List<Integer> recipeIds, User user) {
        recipeIds.remove(null);
        List<Recipe>  recipes = getRecipesById(recipeIds);
        List<Long> fridgeItemIds = fridgeItemRepository.findItemIdsByUserId(user.getId());
        List<Long> expiringFridgeItemIds = fridgeItemService.getExpiringItemIdsByUserId(user.getId());
        List<RecipeWithFridgeCount> recipeWithFridgeCounts = new ArrayList<>();

        for (Recipe recipe : recipes) {
            List<Long> recipeItemIds = recipeItemRepository.findItemIdsByRecipeId(recipe.getId());
            int fridgeCount = (int) recipeItemIds.stream().filter(fridgeItemIds::contains).count();
            int expiringCount = (int) recipeItemIds.stream().filter(expiringFridgeItemIds::contains).count();
            recipeWithFridgeCounts.add(new RecipeWithFridgeCount(recipe.getId(), recipe, fridgeCount, expiringCount));
        }
        return ResponseEntity.status(HttpStatus.OK).body(recipeWithFridgeCounts);
    }

    public ResponseEntity<String> saveWeekMenu(List<Integer> recipeIds, String type, User user) {
            List<Recipe> recipes = getRecipesById(recipeIds);

            if (weekMenuRepository.findByUser(user).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Week menu already exists for this user");
            }

            WeekMenu weekMenu = new WeekMenu(user, recipes.get(0), recipes.get(1), recipes.get(2), recipes.get(3), recipes.get(4), type);
            weekMenuRepository.save(weekMenu);
            return ResponseEntity.status(HttpStatus.OK).body("Week menu saved");
        }


    @Transactional
    public ResponseEntity<String> removeWeekMenu(User user) {
        Optional<WeekMenu> weekMenu = weekMenuRepository.findByUser(user);

        weekMenu.ifPresent(weekMenuRepository::delete);
        return ResponseEntity.status(HttpStatus.OK).body("removed week menu");
    }

    public List<Recipe> getRecipesById(List<Integer> recipeIds) {
        List<Recipe> recipes = new ArrayList<>();
        for (Integer recipeId : recipeIds) {
            recipes.add(recipeService.getRecipeById(Long.valueOf(recipeId)).getBody());
        }
        return recipes;
    }

    public WeekMenu getWeekMenuByUser(User user) {
        Optional<WeekMenu> weekMenuOptional = weekMenuRepository.findByUser(user);
        return weekMenuOptional.orElse(null);
    }
}

