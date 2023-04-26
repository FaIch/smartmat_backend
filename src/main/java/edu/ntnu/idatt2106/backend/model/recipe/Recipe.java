package edu.ntnu.idatt2106.backend.model.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String estimatedTime;

    private String description;

    private int numberOfItems;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;


    @JsonIgnore
    @OneToMany(mappedBy = "recipe")
    private List<RecipeItem> recipeItems;


}
