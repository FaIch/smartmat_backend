package edu.ntnu.idatt2106.backend.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entity for when saving in database(?)
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String shortDesc;

    @Enumerated(EnumType.STRING)
    private Category category;
    private double price;

    private double weight;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    @JsonIgnore
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeItem> recipeItems;

    @JsonIgnore
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FridgeItem> fridgeItems;

    @JsonIgnore
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingListItem> shoppingItems;


    public Item(String name) {
        this.name = name;
    }
    public Item (long id,String name,String shortDesc,Category category,double price,double weight,String itemImg) {
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
        this.category = category;
        this.price = price;
        this.weight = weight;
        this.image = itemImg;
    }
}
