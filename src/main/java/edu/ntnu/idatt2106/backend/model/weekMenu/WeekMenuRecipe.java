package edu.ntnu.idatt2106.backend.model.weekMenu;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The WeekMenu class represents a weekly menu containing a list of recipes.
 * It is associated with a User and maintains a list of WeekMenuRecipe objects.
 *
 * @author YourName
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekMenuRecipe {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_menu_id")
    private WeekMenu weekMenu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private boolean completed;

}
