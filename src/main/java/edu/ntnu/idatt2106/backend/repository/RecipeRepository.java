package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe getRecipeById(Long id);
}
