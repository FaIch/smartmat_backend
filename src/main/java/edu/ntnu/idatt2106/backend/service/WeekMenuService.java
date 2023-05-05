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

    public List<WeekMenu> getAllWeekMenus() {
        return weekMenuRepository.findAll();
    }

    public WeekMenu saveWeekMenu(WeekMenu weekMenu) {
        return weekMenuRepository.save(weekMenu);
    }

    public WeekMenuRecipe toggleRecipeCompleted(Long weekMenuRecipeId) {
        Optional<WeekMenuRecipe> optionalWeekMenuRecipe = weekMenuRecipeRepository.findById(weekMenuRecipeId);

        if (optionalWeekMenuRecipe.isPresent()) {
            WeekMenuRecipe weekMenuRecipe = optionalWeekMenuRecipe.get();
            weekMenuRecipe.setCompleted(!weekMenuRecipe.isCompleted());
            return weekMenuRecipeRepository.save(weekMenuRecipe);
        }

        return null;
    }

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

    public WeekMenuDTO toWeekMenuDTO(WeekMenu weekMenu) {
        WeekMenuDTO dto = new WeekMenuDTO();
        dto.setId(weekMenu.getId());
        dto.setWeekMenuRecipes(weekMenu.getWeekMenuRecipes().stream()
                .map(this::toWeekMenuRecipeDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public WeekMenuRecipeDTO toWeekMenuRecipeDTO(WeekMenuRecipe weekMenuRecipe) {
        WeekMenuRecipeDTO dto = new WeekMenuRecipeDTO();
        dto.setId(weekMenuRecipe.getId());
        dto.setRecipe(weekMenuRecipe.getRecipe());
        dto.setCompleted(weekMenuRecipe.isCompleted());
        return dto;
    }

    public WeekMenu rerollRecipe(Long currentRecipeId, User user) {
        Optional<WeekMenu> weekMenuOptional = weekMenuRepository.findByUser(user);
        WeekMenu weekMenu = weekMenuOptional.get();
        List<WeekMenuRecipe> weekMenuRecipes = weekMenu.getWeekMenuRecipes();

        // Find the WeekMenuRecipe object that contains the currentRecipeId
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

        // Replace the recipe in the WeekMenuRecipe object
        Random random = new Random();
        Recipe newRecipe = availableRecipes.get(random.nextInt(availableRecipes.size()));
        currentWeekMenuRecipe.setRecipe(newRecipe);

        // Save the updated WeekMenu
        return weekMenuRepository.save(weekMenu);
    }

}
