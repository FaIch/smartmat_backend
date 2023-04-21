package edu.ntnu.idatt2106.backend.repository;


import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    /*@Query("SELECT r.name AS recipeName FROM Recipe r JOIN r.recipeItems ri JOIN ri.item i WHERE i.name = :itemName")
    List<String> findRecipeNamesByItemName(@Param("itemName") String itemName);

    @Query("SELECT r FROM Recipe r JOIN r.recipeItems ri WHERE ri.item IN :fridgeItems GROUP BY r.id ORDER BY COUNT(ri.item) DESC, SUM(ri.quantity) ASC")
    List<Recipe> findRecipesByFridgeItems(@Param("fridgeItems") List<Item> fridgeItems);*/
}
