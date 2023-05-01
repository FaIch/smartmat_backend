package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.Response.ResponseWeekMenu;
import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenuData;
import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenuRequest;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeWithFridgeCount;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.WeekMenuService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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

    //TODO: ha tabell for ukes meny i db, vise en random ukes meny (og en anbefalt)? har mulighet til å lagre en uke meny av gangen
    //TODO: kan se på lagret ukesmeny og fjerne den fra db
    //TODO: metode for å legge til alle itemene som skal være i ukes menyen men som ikke har?
    public WeekMenuController(WeekMenuService weekMenuService) {
        this.weekMenuService = weekMenuService;
    }


    @GetMapping("/list-random")
    public ResponseEntity<List<Recipe>> list(){
        return weekMenuService.getRandomWeekMenu();
    }

    @PostMapping("/get-recipes-by-id")
    public ResponseEntity<List<RecipeWithFridgeCount>> listByIds(@RequestBody List<Integer> recipeIds, @AuthenticationPrincipal User user) {
        return weekMenuService.getRecipesById(recipeIds, user);
    }

    @PostMapping("/get-data-week-menu")
    public ResponseEntity<WeekMenuData> getWeekMenuData(@RequestBody List<Integer> recipeIds, @AuthenticationPrincipal User user) {
        System.out.println(recipeIds.toString());
        return weekMenuService.getDataWeekMenu(recipeIds, user);
    }

}