package edu.ntnu.idatt2106.backend.model.weekMenu;

import com.fasterxml.jackson.annotation.*;
import edu.ntnu.idatt2106.backend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The WeekMenu class represents a weekly menu containing a list of recipes.
 * It is associated with a User and maintains a list of WeekMenuRecipe objects.
 *
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekMenu {

    /**
     * The unique identifier for the weekly menu.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user associated with this weekly menu.
     * It is set to be ignored by JSON serialization to prevent circular references.
     */
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The list of WeekMenuRecipe objects associated with this weekly menu.
     * It is set to be ignored by JSON serialization to prevent circular references.
     */
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "weekMenu")
    private List<WeekMenuRecipe> weekMenuRecipes;
}
