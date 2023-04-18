package edu.ntnu.idatt2106.backend.model.recipe;

import edu.ntnu.idatt2106.backend.model.item.Item;
import jakarta.persistence.*;

public class Recipe_Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;


    public Recipe_Item(Long id, Item item, Recipe recipe) {
        this.id = id;
        this.item = item;
        this.recipe = recipe;
    }
}
