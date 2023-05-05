package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.dto.WeekMenuDTO;
import edu.ntnu.idatt2106.backend.model.weekMenu.WeekMenu;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
import edu.ntnu.idatt2106.backend.service.WeekMenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public ResponseEntity<WeekMenuDTO> getWeekMenuByUserId(@AuthenticationPrincipal User user) {
        WeekMenu weekMenu = weekMenuService.getWeekMenuByUser(user);
        if (weekMenu != null) {
            return ResponseEntity.ok(weekMenuService.toWeekMenuDTO(weekMenu));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{weekMenuRecipeId}/toggle-completed")
    public ResponseEntity<String> toggleRecipeCompleted(@PathVariable("weekMenuRecipeId") Long weekMenuRecipeId, @AuthenticationPrincipal User user) {
        weekMenuService.toggleRecipeCompleted(weekMenuRecipeId);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/get-recipes-items")
    public ResponseEntity<List<RecipeItem>> getRecipeItems(@RequestBody List<Integer> recipeIds, @AuthenticationPrincipal User user) {
        return weekMenuService.getRecipeItems(recipeIds);
    }

    @PutMapping("/reroll/{currentRecipeId}")
    public ResponseEntity<String> rerollRecipe(@PathVariable Long currentRecipeId, @AuthenticationPrincipal User user) {
        weekMenuService.rerollRecipe(currentRecipeId, user);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("/new-menu")
    public ResponseEntity<String> newMenu(@AuthenticationPrincipal User user) {
        weekMenuService.newMenu(user);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}
