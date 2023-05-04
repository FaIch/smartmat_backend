package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenu;
import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenuData;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
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

    /**
     * Constructs a {@code WeekMenuService} instance with the specified repositories and services.
     *
     * @param weekMenuRepository    the week menu repository
     * @param recipeItemRepository  the recipe item repository
     * @param recipeService         the recipe service
     * @param fridgeItemService     the fridge item service
     * @param fridgeItemRepository  the fridge item repository
     */
    @Autowired
    public WeekMenuService(WeekMenuRepository weekMenuRepository, RecipeItemRepository recipeItemRepository, RecipeService recipeService, FridgeItemService fridgeItemService, FridgeItemRepository fridgeItemRepository) {
        this.weekMenuRepository = weekMenuRepository;
        this.recipeItemRepository = recipeItemRepository;
        this.recipeService = recipeService;
        this.fridgeItemService = fridgeItemService;
        this.fridgeItemRepository = fridgeItemRepository;
    }

    /**
     * Generates a random week menu containing 5 recipes.
     *
     * @return a {@link ResponseEntity} containing the list of random recipes with a {@link HttpStatus} of OK
     */
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

    /**
     * Generates a recommended week menu for the specified user based on their preferences and available ingredients.
     *
     * @param user the user for whom the week menu is being generated
     * @return a {@link ResponseEntity} containing the list of recommended recipes with a {@link HttpStatus} of OK
     */
    public ResponseEntity<List<Recipe>> getRecommendedWeekMenu(User user) {
        List<Recipe> recommendedRecipes = new ArrayList<>();

     List<RecipeWithFridgeCount> recipesWithFridgeCounts =  Objects.requireNonNull(recipeService.getRecipesSorted(user, 4).getBody()).subList(0,5);

     for (RecipeWithFridgeCount recipeWithFridgeCount: recipesWithFridgeCounts) {
        recommendedRecipes.add(recipeWithFridgeCount.getRecipe());
     }
     return ResponseEntity.status(HttpStatus.OK).body(recommendedRecipes);
    }

    /**
     * Gets the week menu data for the specified list of recipe IDs and user.
     *
     * @param recipeIds the list of recipe IDs
     * @param user      the user for whom the data is being fetched
     * @return a {@link ResponseEntity} containing the {@link WeekMenuData} object with a {@link HttpStatus} of OK
     */
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

    /**
     * Retrieves a list of {@link RecipeWithFridgeCount} objects for the specified list of recipe IDs and user.
     *
     * @param recipeIds the list of recipe IDs
     * @param user      the user for whom the recipes are being fetched
     * @return a {@link ResponseEntity} containing the list of {@link RecipeWithFridgeCount} objects with a {@link HttpStatus} of OK
     */
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

    /**
     * Saves a week menu for the specified user with the given list of recipe IDs and menu type.
     *
     * @param recipeIds the list of recipe IDs
     * @param type      the type of the week menu
     * @param user      the user for whom the week menu is being saved
     * @return a {@link ResponseEntity} containing a success message or an error message with the corresponding {@link HttpStatus}
     */
    public ResponseEntity<String> saveWeekMenu(List<Integer> recipeIds, String type, User user) {
            List<Recipe> recipes = getRecipesById(recipeIds);

            if (weekMenuRepository.findByUser(user).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Week menu already exists for this user");
            }

            WeekMenu weekMenu = new WeekMenu(user, recipes.get(0), recipes.get(1), recipes.get(2), recipes.get(3), recipes.get(4), type);
            weekMenuRepository.save(weekMenu);
            return ResponseEntity.status(HttpStatus.OK).body("Week menu saved");
        }

    /**
     * Removes the week menu associated with the specified user.
     *
     * @param user the user whose week menu is to be removed
     * @return a {@link ResponseEntity} containing a success message with a {@link HttpStatus} of OK
     */
    @Transactional
    public ResponseEntity<String> removeWeekMenu(User user) {
        Optional<WeekMenu> weekMenu = weekMenuRepository.findByUser(user);

        weekMenu.ifPresent(weekMenuRepository::delete);
        return ResponseEntity.status(HttpStatus.OK).body("removed week menu");
    }

    /**
     * Retrieves a list of {@link Recipe} objects for the specified list of recipe IDs.
     *
     * @param recipeIds the list of recipe IDs
     * @return a list of {@link Recipe} objects
     */
    public List<Recipe> getRecipesById(List<Integer> recipeIds) {
        List<Recipe> recipes = new ArrayList<>();
        for (Integer recipeId : recipeIds) {
            recipes.add(recipeService.getRecipeById(Long.valueOf(recipeId)).getBody());
        }
        return recipes;
    }

    /**
     * Retrieves the week menu associated with the specified user.
     *
     * @param user the user whose week menu is to be retrieved
     * @return the {@link WeekMenu} object associated with the user or {@code null} if no week menu is found
     */
    public WeekMenu getWeekMenuByUser(User user) {
        Optional<WeekMenu> weekMenuOptional = weekMenuRepository.findByUser(user);
        return weekMenuOptional.orElse(null);
    }

    public ResponseEntity<List<RecipeItem>> getRecipeItems(List<Integer> recipeIds) {
        Map<Long, RecipeItem> recipeItemsMap = new HashMap<>();
        for (Integer recipeId : recipeIds) {
            if (recipeId != null) {
                for (RecipeItem recipeItem : recipeItemRepository.findAllByRecipeId(Long.valueOf(recipeId))) {
                    RecipeItem existingRecipeItem = recipeItemsMap.get(recipeItem.getItem().getId());
                    if (existingRecipeItem == null) {
                        recipeItemsMap.put(recipeItem.getItem().getId(), recipeItem);
                    } else {
                        existingRecipeItem.setQuantity(existingRecipeItem.getQuantity() + recipeItem.getQuantity());
                    }
                }
            }
        }
        List<RecipeItem> recipeItems = new ArrayList<>(recipeItemsMap.values());
        return ResponseEntity.status(HttpStatus.OK).body(recipeItems);
    }
}

