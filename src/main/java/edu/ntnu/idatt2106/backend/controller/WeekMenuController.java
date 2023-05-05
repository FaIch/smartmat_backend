package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.dto.WeekMenuDTO;
import edu.ntnu.idatt2106.backend.model.weekMenu.WeekMenu;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.WeekMenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The WeekMenuController is a REST controller that exposes endpoints related to week menu management.
 * It handles HTTP requests and communicates with the WeekMenuService to perform operations on week menus.
 */
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/week-menu")
public class WeekMenuController {

    private final WeekMenuService weekMenuService;

    public WeekMenuController(WeekMenuService weekMenuService) {
        this.weekMenuService = weekMenuService;
    }

    /**
     * Retrieves the week menu for the authenticated user.
     *
     * @param user The authenticated User object.
     * @return A ResponseEntity containing the WeekMenuDTO object and the HTTP status code.
     */
    @GetMapping("/get")
    public ResponseEntity<WeekMenuDTO> getWeekMenuByUserId(@AuthenticationPrincipal User user) {
        WeekMenu weekMenu = weekMenuService.getWeekMenuByUser(user);
        if (weekMenu != null) {
            return ResponseEntity.ok(weekMenuService.toWeekMenuDTO(weekMenu));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Toggles the completion status of a recipe in the week menu.
     *
     * @param weekMenuRecipeId The ID of the week menu recipe to toggle.
     * @param user The authenticated User object.
     * @return A ResponseEntity containing the success message and the HTTP status code.
     */
    @PutMapping("/{weekMenuRecipeId}/toggle-completed")
    public ResponseEntity<String> toggleRecipeCompleted(@PathVariable("weekMenuRecipeId") Long weekMenuRecipeId, @AuthenticationPrincipal User user) {
        weekMenuService.toggleRecipeCompleted(weekMenuRecipeId);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /**
     * Retrieves a list of RecipeItem objects for the specified recipes.
     *
     * @param recipeIds The list of recipe IDs.
     * @param user The authenticated User object.
     * @return A ResponseEntity containing the list of RecipeItem objects and the HTTP status code.
     */
    @PostMapping("/get-recipes-items")
    public ResponseEntity<List<RecipeItem>> getRecipeItems(@RequestBody List<Integer> recipeIds, @AuthenticationPrincipal User user) {
        return weekMenuService.getRecipeItems(recipeIds);
    }

    /**
     * Replaces a current recipe in the week menu with a new one.
     *
     * @param currentRecipeId The ID of the current recipe.
     * @param user The authenticated User object.
     * @return A ResponseEntity containing the success message and the HTTP status code.
     */
    @PutMapping("/reroll/{currentRecipeId}")
    public ResponseEntity<String> rerollRecipe(@PathVariable Long currentRecipeId, @AuthenticationPrincipal User user) {
        weekMenuService.rerollRecipe(currentRecipeId, user);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /**
     * Generates a new menu for the week, replacing all recipes in the week menu.
     *
     * @param user The authenticated User object.
     * @return A ResponseEntity containing the success message and the HTTP status code.
     */
    @GetMapping("/new-menu")
    public ResponseEntity<String> newMenu(@AuthenticationPrincipal User user) {
        weekMenuService.newMenu(user);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
