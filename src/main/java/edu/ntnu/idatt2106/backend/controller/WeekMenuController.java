package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.Response.ResponseWeekMenu;
import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenu;
import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenuData;
import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenuRequest;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeWithFridgeCount;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.WeekMenuService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
@RequestMapping(value = "/week-menu")
public class WeekMenuController {
    private final WeekMenuService weekMenuService;

    public WeekMenuController(WeekMenuService weekMenuService) {
        this.weekMenuService = weekMenuService;
    }

    /*
     * Return a list of random recipes
     */
    @GetMapping("/list-random")
    public ResponseEntity<List<Recipe>> listRandom(){
        return weekMenuService.getRandomWeekMenu();
    }

    /*
     * Return a list of recommended recipes
     */
    @GetMapping("/list-recommended")
    public ResponseEntity<List<Recipe>> listRecommended(@AuthenticationPrincipal User user){
        return weekMenuService.getRecommendedWeekMenu(user);
    }

    /*
        * Return a list of random recipes
     */
    public ResponseEntity<List<Recipe>> list(){
        return weekMenuService.getRandomWeekMenu();
    }

    /*
    * get recipes by id
     */
    @PostMapping("/get-recipes-by-id")
    public ResponseEntity<List<RecipeWithFridgeCount>> listByIds(@RequestBody List<Integer> recipeIds, @AuthenticationPrincipal User user) {
        return weekMenuService.getRecipesById(recipeIds, user);
    }

    /*
    * get data for week menu
     */
    @PostMapping("/get-data-week-menu")
    public ResponseEntity<WeekMenuData> getWeekMenuData(@RequestBody List<Integer> recipeIds, @AuthenticationPrincipal User user) {
        return weekMenuService.getDataWeekMenu(recipeIds, user);
    }

    @PostMapping("/get-recipes-items")
    public ResponseEntity<List<RecipeItem>> getRecipeItems(@RequestBody List<Integer> recipeIds) {
        return weekMenuService.getRecipeItems(recipeIds);
    }

    /*
     * save week menu
     */
    @PostMapping("/save")
    public ResponseEntity<String> saveMenu(@RequestBody WeekMenuRequest request, @AuthenticationPrincipal User user ) {
        return weekMenuService.saveWeekMenu(request.getIntList(), request.getMessage(), user);
    }

    /*
     * get week menu by user id
     */
    @GetMapping("/get")
    public ResponseEntity<WeekMenu> getWeekMenuByUserId(@AuthenticationPrincipal User user) {
        WeekMenu weekMenu = weekMenuService.getWeekMenuByUser(user);
        if (weekMenu != null) {
            return ResponseEntity.ok(weekMenu);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /*
     * remove week menu by user id
     */
    @GetMapping("/remove")
    public ResponseEntity<String> removeMenu(@AuthenticationPrincipal User user) {
        return weekMenuService.removeWeekMenu(user);
    }

}
