package edu.ntnu.idatt2106.backend.repository;

import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeItemRepository extends JpaRepository<RecipeItem, Long> {

    @Query("SELECT ri FROM RecipeItem ri WHERE ri.item.name = CONCAT('\'', :itemName, '\'')")
    List<RecipeItem> findByItemName(@Param("itemName") String itemName);

    @Query("SELECT r.name AS recipe_name "
            + "FROM Recipe r "
            + "INNER JOIN RecipeItem ri ON r.id = ri.recipe.id "
            + "INNER JOIN Item i ON ri.item.id = i.id "
            + "WHERE i.name = " + "'Steak'" )
    List<String> findRecipeNamesByItemName(@Param("itemName") String itemName);
}
