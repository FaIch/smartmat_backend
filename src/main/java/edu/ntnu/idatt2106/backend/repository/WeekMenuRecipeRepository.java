package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.weekMenu.WeekMenuRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekMenuRecipeRepository extends JpaRepository<WeekMenuRecipe, Long> {
}