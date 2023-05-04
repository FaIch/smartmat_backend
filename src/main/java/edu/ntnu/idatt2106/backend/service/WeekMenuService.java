package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenu;
import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenuRecipe;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeWithFridgeCount;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class WeekMenuService {

    private final WeekMenuRepository weekMenuRepository;
    private final WeekMenuRecipeRepository weekMenuRecipeRepository;
    private final RecipeRepository recipeRepository;

    @Autowired
    public WeekMenuService(WeekMenuRepository weekMenuRepository, WeekMenuRecipeRepository weekMenuRecipeRepository, RecipeRepository recipeRepository) {
        this.weekMenuRepository = weekMenuRepository;
        this.weekMenuRecipeRepository = weekMenuRecipeRepository;
        this.recipeRepository = recipeRepository;
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
}
