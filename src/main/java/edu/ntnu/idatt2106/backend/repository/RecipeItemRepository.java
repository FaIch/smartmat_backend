package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeItemRepository extends JpaRepository<RecipeItem, Long> {

    @Query("SELECT ri FROM RecipeItem ri WHERE ri.item.name = :itemName")
    List<RecipeItem> findByItemName(@Param("itemName") String itemName);

    @Query("SELECT ri.item.id FROM RecipeItem ri WHERE ri.recipe.id = :recipeId")
    List<Long> findItemIdsByRecipeId(Long recipeId);
}
