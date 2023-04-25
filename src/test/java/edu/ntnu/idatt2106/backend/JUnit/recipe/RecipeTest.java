package edu.ntnu.idatt2106.backend.JUnit.recipe;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RecipeTest {

    private Recipe recipeMacNCheese;

    RecipeItem recipeItemMac,recipeItemCheese;
    Item itemMac, itemCheese;

    List<RecipeItem> recipeItemList;

    @BeforeEach
    void setUp() {
        itemMac = new Item(1L,"Macaroni","Full grain pasta", Category.DRYGOODS, 39, 400, "");
        itemCheese = new Item(1L,"Cheddar","Real cheddar from idunno", Category.CHEESE, 55, 200, "");
         recipeItemMac = new RecipeItem(1L, recipeMacNCheese, itemMac, 200);
         recipeItemCheese = new RecipeItem(2L, recipeMacNCheese, itemCheese, 150);

      recipeItemList = new ArrayList<>();
      recipeItemList.add(recipeItemCheese);
      recipeItemList.add(recipeItemMac);

        recipeMacNCheese = new Recipe(1L, "Mac and Cheese", "Macaroni and cheese", 4, recipeItemList);
    }

    @Test
    void testNoArgsConstructor() {
        Recipe recipeEmpty = new Recipe();
        assertNull(recipeEmpty.getId());
        assertNull(recipeEmpty.getName());
        assertNull(recipeEmpty.getDescription());
        assertEquals(0.0, recipeEmpty.getNumberOfItems());
    }

    @Test
    void testAllArgsConstructor() {
        assertEquals(1L, recipeMacNCheese.getId());
        assertEquals("Mac and Cheese", recipeMacNCheese.getName());
        assertEquals("Macaroni and cheese", recipeMacNCheese.getDescription());
        assertEquals(4, recipeMacNCheese.getNumberOfItems());
        assertEquals(recipeItemList, recipeMacNCheese.getRecipeItems());

    }

    @Test
    void testSettersAndGetters() {
        recipeMacNCheese.setId(2L);
        recipeMacNCheese.setName("updated Name");
        recipeMacNCheese.setDescription("updated description");
        recipeMacNCheese.setNumberOfItems(0);
        recipeMacNCheese.setRecipeItems(new ArrayList<>());


        assertEquals(2L, recipeMacNCheese.getId());
        assertEquals("updated Name", recipeMacNCheese.getName());
        assertEquals("updated description", recipeMacNCheese.getDescription());
        assertEquals(0, recipeMacNCheese.getNumberOfItems());
        assertEquals(new ArrayList<>(), recipeMacNCheese.getRecipeItems());

    }

    @Test
    void testEqualsAndHashCode() {
       Recipe recipeTestEmpty = new Recipe(2L,"TestItem2", "", 0,new ArrayList<>());

       Recipe sameRecipeMacNCheese = new Recipe(recipeMacNCheese.getId(),recipeMacNCheese.getName(),recipeMacNCheese.getDescription(),recipeMacNCheese.getNumberOfItems(), recipeMacNCheese.getRecipeItems());

        assertNotEquals(recipeMacNCheese, recipeTestEmpty);
        assertNotEquals(recipeMacNCheese.hashCode(), recipeTestEmpty.hashCode());

        assertEquals(recipeMacNCheese, sameRecipeMacNCheese);
        assertEquals(recipeMacNCheese.hashCode(), sameRecipeMacNCheese.hashCode());
    }
}
