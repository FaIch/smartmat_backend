package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.Response.ResponseWeekMenu;
import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenu;
import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenuRecipe;
import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenuRequest;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeWithFridgeCount;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
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

    private UserRepository userRepository;

    public WeekMenuController(WeekMenuService weekMenuService, UserRepository userRepository) {
        this.weekMenuService = weekMenuService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<WeekMenu> saveWeeklyMenu(@RequestParam("userId") Long userId, @RequestBody WeekMenu weekMenu) {
        if (weekMenu.getWeekMenuRecipes().size() == 5) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                weekMenu.setUser(user);
                WeekMenu savedWeeklyMenu = weekMenuService.saveWeekMenu(weekMenu);
                return new ResponseEntity<>(savedWeeklyMenu, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<WeekMenu> getWeekMenuByUserId(@AuthenticationPrincipal User user) {
        WeekMenu weekMenu = weekMenuService.getWeekMenuByUser(user);
        if (weekMenu != null) {
            return ResponseEntity.ok(weekMenu);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/week-menu-recipe/{weekMenuRecipeId}/toggle-completed")
    public ResponseEntity<WeekMenuRecipe> toggleRecipeCompleted(@PathVariable("weekMenuRecipeId") Long weekMenuRecipeId) {
        WeekMenuRecipe updatedWeekMenuRecipe = weekMenuService.toggleRecipeCompleted(weekMenuRecipeId);

        if (updatedWeekMenuRecipe != null) {
            return new ResponseEntity<>(updatedWeekMenuRecipe, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }




}
