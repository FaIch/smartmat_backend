package edu.ntnu.idatt2106.backend.JUnit.recipe;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RecipeItemTest {

    RecipeItem recipeItemMac;
    Item itemMac;
    private Recipe recipeMacNCheese;

    @BeforeEach
    void setUp() {
        itemMac = new Item(1L,"Macaroni","Full grain pasta", Category.DRYGOODS, 39, 400, "");

        recipeItemMac = new RecipeItem(1L, recipeMacNCheese, itemMac, 200);


    }

    @Test
    void testNoArgsConstructor() {
        RecipeItem recipeItemEmpty = new RecipeItem();
        assertNull(recipeItemEmpty.getId());
        assertNull(recipeItemEmpty.getItem());
        assertNull(recipeItemEmpty.getRecipe());
        assertEquals(0.0, recipeItemEmpty.getQuantity());
    }

    @Test
    void testAllArgsConstructor() {
        assertEquals(1L, recipeItemMac.getId());
        assertEquals(recipeMacNCheese, recipeItemMac.getRecipe());
        assertEquals(itemMac, recipeItemMac.getItem());
        assertEquals(200, recipeItemMac.getQuantity());


    }

    @Test
    void testSettersAndGetters() {
        Recipe updatedRecipe = new Recipe();
        Item updatedItem = new Item();
        recipeItemMac.setId(2L);
        recipeItemMac.setRecipe(updatedRecipe);
        recipeItemMac.setItem(updatedItem);
        recipeItemMac.setQuantity(400);

        assertEquals(2L, recipeItemMac.getId());
        assertEquals(updatedRecipe, recipeItemMac.getRecipe());
        assertEquals(updatedItem, recipeItemMac.getItem());
        assertEquals(400, recipeItemMac.getQuantity());
    }

    @Test
    void testEqualsAndHashCode() {
        Recipe emptyRecipe = new Recipe();
        Item emptyItem = new Item();
        RecipeItem recipeItemEmpty = new RecipeItem(1L, emptyRecipe, emptyItem, 0);

        RecipeItem sameRecipeItemMac = new RecipeItem(recipeItemMac.getId(),recipeItemMac.getRecipe(),recipeItemMac.getItem(), recipeItemMac.getQuantity());

        assertNotEquals(recipeItemMac, recipeItemEmpty);
        assertNotEquals(recipeItemMac.hashCode(), recipeItemEmpty.hashCode());

        assertEquals(recipeItemMac, sameRecipeItemMac);
        assertEquals(recipeItemMac.hashCode(), sameRecipeItemMac.hashCode());
    }
}
