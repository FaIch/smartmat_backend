package edu.ntnu.idatt2106.backend.dto;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeekMenuRecipeDTO {
    private Long id;
    private Recipe recipe;
    private boolean completed;
}
