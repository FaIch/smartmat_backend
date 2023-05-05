package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.dto.WeekMenuDTO;
import edu.ntnu.idatt2106.backend.dto.WeekMenuRecipeDTO;
import edu.ntnu.idatt2106.backend.model.weekMenu.WeekMenu;
import edu.ntnu.idatt2106.backend.model.weekMenu.WeekMenuRecipe;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * The WeekMenuService is a service class responsible for managing the week menu functionality.
 * It provides methods for creating and managing a week menu, toggling recipe completion status,
 * getting a list of recipe items, rerolling recipes, and generating a new menu for the week.
 */
@Service
public class WeekMenuService {

    private final WeekMenuRepository weekMenuRepository;
    private final WeekMenuRecipeRepository weekMenuRecipeRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeItemRepository recipeItemRepository;

    @Autowired
    public WeekMenuService(WeekMenuRepository weekMenuRepository, WeekMenuRecipeRepository weekMenuRecipeRepository, RecipeRepository recipeRepository, RecipeItemRepository recipeItemRepository) {
        this.weekMenuRepository = weekMenuRepository;
        this.weekMenuRecipeRepository = weekMenuRecipeRepository;
        this.recipeRepository = recipeRepository;
        this.recipeItemRepository = recipeItemRepository;
    }

    /**
     * Toggles the completion status of a recipe in the week menu.
     *
     * @param weekMenuRecipeId The ID of the week menu recipe to toggle.
     * @return The updated WeekMenuRecipe object with the new completion status.
     */
    public WeekMenuRecipe toggleRecipeCompleted(Long weekMenuRecipeId) {
        Optional<WeekMenuRecipe> optionalWeekMenuRecipe = weekMenuRecipeRepository.findById(weekMenuRecipeId);

        if (optionalWeekMenuRecipe.isPresent()) {
            WeekMenuRecipe weekMenuRecipe = optionalWeekMenuRecipe.get();
            weekMenuRecipe.setCompleted(!weekMenuRecipe.isCompleted());
            return weekMenuRecipeRepository.save(weekMenuRecipe);
        }

        return null;
    }

    /**
     * Retrieves or creates a week menu for the given user.
     *
     * @param user The User object for which to retrieve or create the week menu.
     * @return The WeekMenu object for the given user.
     */
    public WeekMenu getWeekMenuByUser(User user) {
        WeekMenu weekMenu = weekMenuRepository.findByUser(user).orElse(null);

        if (weekMenu == null) {
            weekMenu = new WeekMenu();
            weekMenu.setUser(user);
        }

        if (weekMenu.getWeekMenuRecipes().size() < 5) {
            List<Recipe> allRecipes = recipeRepository.findAll();
            List<WeekMenuRecipe> weekMenuRecipes = populateWeekMenuWithRandomRecipes(weekMenu, allRecipes, 5);
            weekMenu.setWeekMenuRecipes(weekMenuRecipes);
            weekMenuRepository.save(weekMenu);
        }

        return weekMenu;
    }

    /**
     * Populates the given week menu with random recipes.
     *
     * @param weekMenu   The WeekMenu object to populate.
     * @param allRecipes The list of all available recipes.
     * @param targetSize The target size of the week menu recipes list.
     * @return The list of WeekMenuRecipe objects added to the week menu.
     */
    private List<WeekMenuRecipe> populateWeekMenuWithRandomRecipes(WeekMenu weekMenu, List<Recipe> allRecipes, int targetSize) {
        Collections.shuffle(allRecipes);
        List<WeekMenuRecipe> weekMenuRecipes = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            if (weekMenuRecipes.size() >= targetSize) {
                break;
            }
            WeekMenuRecipe weekMenuRecipe = new WeekMenuRecipe();
            weekMenuRecipe.setWeekMenu(weekMenu);
            weekMenuRecipe.setRecipe(recipe);
            weekMenuRecipe.setCompleted(false);
            weekMenuRecipes.add(weekMenuRecipe);
        }

        return weekMenuRecipes;
    }

    /**
     * Retrieves a list of RecipeItem objects based on the provided list of recipe IDs.
     *
     * @param recipeIds The list of recipe IDs.
     * @return A ResponseEntity containing the status code and the list of RecipeItem objects.
     */
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

    /**
     * Converts a WeekMenu object to a WeekMenuDTO object.
     *
     * @param weekMenu The WeekMenu object to convert.
     * @return The converted WeekMenuDTO object.
     */
    public WeekMenuDTO toWeekMenuDTO(WeekMenu weekMenu) {
        WeekMenuDTO dto = new WeekMenuDTO();
        dto.setId(weekMenu.getId());
        dto.setWeekMenuRecipes(weekMenu.getWeekMenuRecipes().stream()
                .map(this::toWeekMenuRecipeDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    /**
     * Converts a WeekMenuRecipe object to a WeekMenuRecipeDTO object.
     *
     * @param weekMenuRecipe The WeekMenuRecipe object to convert.
     * @return The converted WeekMenuRecipeDTO object.
     */
    public WeekMenuRecipeDTO toWeekMenuRecipeDTO(WeekMenuRecipe weekMenuRecipe) {
        WeekMenuRecipeDTO dto = new WeekMenuRecipeDTO();
        dto.setId(weekMenuRecipe.getId());
        dto.setRecipe(weekMenuRecipe.getRecipe());
        dto.setCompleted(weekMenuRecipe.isCompleted());
        return dto;
    }

    /**
     * Replaces a current recipe in the week menu with a new one.
     *
     * @param currentRecipeId The ID of the current recipe.
     * @param user The User object for which to reroll the recipe.
     * @return The updated WeekMenu object with the new recipe.
     */
    public WeekMenu rerollRecipe(Long currentRecipeId, User user) {
        Optional<WeekMenu> weekMenuOptional = weekMenuRepository.findByUser(user);
        WeekMenu weekMenu = weekMenuOptional.get();
        List<WeekMenuRecipe> weekMenuRecipes = weekMenu.getWeekMenuRecipes();

        Optional<WeekMenuRecipe> currentWeekMenuRecipeOptional = weekMenuRecipes.stream()
                .filter(weekMenuRecipe -> weekMenuRecipe.getRecipe().getId().equals(currentRecipeId))
                .findFirst();

        if (!currentWeekMenuRecipeOptional.isPresent()) {
            throw new IllegalStateException("Current recipe not found in the week menu.");
        }

        WeekMenuRecipe currentWeekMenuRecipe = currentWeekMenuRecipeOptional.get();

        List<Recipe> allRecipes = recipeRepository.findAll();
        List<Long> weekMenuRecipeIds = weekMenuRecipes.stream()
                .map(weekMenuRecipe -> weekMenuRecipe.getRecipe().getId())
                .collect(Collectors.toList());

        List<Recipe> availableRecipes = allRecipes.stream()
                .filter(recipe -> !weekMenuRecipeIds.contains(recipe.getId()) && !recipe.getId().equals(currentRecipeId))
                .collect(Collectors.toList());

        if (availableRecipes.isEmpty()) {
            throw new IllegalStateException("No available recipes to reroll.");
        }

        Random random = new Random();
        Recipe newRecipe = availableRecipes.get(random.nextInt(availableRecipes.size()));
        currentWeekMenuRecipe.setRecipe(newRecipe);

        return weekMenuRepository.save(weekMenu);
    }

    /**
     * Generates a new menu for the week, replacing all recipes in the week menu.
     *
     * @param user The User object for which to generate a new menu.
     * @return The updated WeekMenu object with the new recipes.
     */
    public WeekMenu newMenu(User user) {
        Optional<WeekMenu> weekMenuOptional = weekMenuRepository.findByUser(user);
        WeekMenu weekMenu = weekMenuOptional.get();
        List<WeekMenuRecipe> weekMenuRecipes = weekMenu.getWeekMenuRecipes();
        List<Recipe> allRecipes = recipeRepository.findAll();
        List<Long> weekMenuRecipeIds = weekMenuRecipes.stream()
                .map(weekMenuRecipe -> weekMenuRecipe.getRecipe().getId())
                .collect(Collectors.toList());

        List<Recipe> availableRecipes = allRecipes.stream()
                .filter(recipe -> !weekMenuRecipeIds.contains(recipe.getId()))
                .collect(Collectors.toList());

        if (availableRecipes.isEmpty()) {
            throw new IllegalStateException("No available recipes to reroll.");
        }

        Random random = new Random();
        for (WeekMenuRecipe weekMenuRecipe : weekMenuRecipes) {
            Recipe newRecipe = availableRecipes.get(random.nextInt(availableRecipes.size()));
            availableRecipes.remove(newRecipe);
            weekMenuRecipe.setRecipe(newRecipe);
            weekMenuRecipe.setCompleted(false);
        }

        return weekMenuRepository.save(weekMenu);
    }
}
