package edu.ntnu.idatt2106.backend.model.WeekMenu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe1_id", nullable = true)
    private Recipe recipe1;

    @ManyToOne
    @JoinColumn(name = "recipe2_id", nullable = true)
    private Recipe recipe2;

    @ManyToOne
    @JoinColumn(name = "recipe3_id", nullable = true)
    private Recipe recipe3;

    @ManyToOne
    @JoinColumn(name = "recipe4_id", nullable = true)
    private Recipe recipe4;

    @ManyToOne
    @JoinColumn(name = "recipe5_id", nullable = true)
    private Recipe recipe5;

    @Column(nullable = false)
    private String type;

    public WeekMenu(User user, Recipe recipe1, Recipe recipe2, Recipe recipe3, Recipe recipe4, Recipe recipe5, String type) {
        this.user = user;
        this.recipe1 = recipe1;
        this.recipe2 = recipe2;
        this.recipe3 = recipe3;
        this.recipe4 = recipe4;
        this.recipe5 = recipe5;
        this.type = type;
    }
}
