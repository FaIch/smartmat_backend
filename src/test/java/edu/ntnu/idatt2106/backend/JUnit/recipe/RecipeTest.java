package edu.ntnu.idatt2106.backend.JUnit.recipe;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RecipeTest {

    private Recipe recipeMacNCheese;
    Item itemMac, itemCheese;

    List<RecipeItem> recipeItemList;

    @BeforeEach
    void setUp() {
        itemMac = new Item(1L,"Macaroni","Full grain pasta", Category.DRYGOODS, 39, 400, "");
        itemCheese = new Item(1L,"Cheddar","Real cheddar from idunno", Category.CHEESE, 55, 200, "");
        RecipeItem recipeItemMac = new RecipeItem(1L, recipeMacNCheese, itemMac, 200);
        RecipeItem recipeItemCheese = new RecipeItem(2L, recipeMacNCheese, itemCheese, 150);

      recipeItemList = new ArrayList<>();
      recipeItemList.add(recipeItemCheese);
      recipeItemList.add(recipeItemMac);

        recipeMacNCheese = new Recipe(1L, "Mac and Cheese", "Macaroni and cheese", 4, recipeItemList);
    }

    @Test
    void testNoArgsConstructor() {
        Recipe recipeItemEmpty = new Recipe();
        assertNull(recipeItemEmpty.getId());
        assertNull(recipeItemEmpty.getName());
        assertNull(recipeItemEmpty.getDescription());
        assertEquals(0.0, recipeItemEmpty.getNumberOfItems());
    }

    @Test
    void testAllArgsConstructor() {
        assertEquals(1L, recipeMacNCheese.getId());
        assertEquals("Mac and Cheese", recipeMacNCheese.getName());
        assertEquals("Macaroni and cheese", recipeMacNCheese.getDescription());
        assertEquals(4, recipeMacNCheese.getNumberOfItems());
        assertEquals(recipeItemList, recipeMacNCheese.getRecipeItems());

    }

/*    @Test
    void testSettersAndGetters() {
        item.setId(2L);
        item.setName("UpdatedItem");
        item.setShortDesc("Updated description");
        item.setCategory(Category.EGG);
        item.setPrice(50.0);
        item.setWeight(2.0);
        item.setImage("UpdatedImage");

        assertEquals(2L, item.getId());
        assertEquals("UpdatedItem", item.getName());
        assertEquals("Updated description", item.getShortDesc());
        assertEquals(Category.EGG, item.getCategory());
        assertEquals(50.0, item.getPrice());
        assertEquals(2.0, item.getWeight());
        assertEquals("UpdatedImage", item.getImage());
    }*/
}
