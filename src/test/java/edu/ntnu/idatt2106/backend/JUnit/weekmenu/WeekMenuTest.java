package edu.ntnu.idatt2106.backend.JUnit.weekmenu;

import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenu;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeekMenuTest {

    @Test
    public void testConstructorAndGetters() {
        User user = new User();
        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        Recipe recipe3 = new Recipe();
        Recipe recipe4 = new Recipe();
        Recipe recipe5 = new Recipe();
        String type = "type";

        WeekMenu weekMenu = new WeekMenu(user, recipe1, recipe2, recipe3, recipe4, recipe5, type);

        assertEquals(user, weekMenu.getUser());
        assertEquals(recipe1, weekMenu.getRecipe1());
        assertEquals(recipe2, weekMenu.getRecipe2());
        assertEquals(recipe3, weekMenu.getRecipe3());
        assertEquals(recipe4, weekMenu.getRecipe4());
        assertEquals(recipe5, weekMenu.getRecipe5());
        assertEquals(type, weekMenu.getType());
    }

    @Test
    @DisplayName("Test equality of WeekMenu objects with the same field values")
    public void testEquals_sameFields() {
        User user = new User();
        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        Recipe recipe3 = new Recipe();
        Recipe recipe4 = new Recipe();
        Recipe recipe5 = new Recipe();
        String type = "Lunch";

        WeekMenu weekMenu1 = new WeekMenu(user, recipe1, recipe2, recipe3, recipe4, recipe5, type);
        WeekMenu weekMenu2 = new WeekMenu(user, recipe1, recipe2, recipe3, recipe4, recipe5, type);

        assertEquals(weekMenu1, weekMenu2);
    }

    @Test
    @DisplayName("Test inequality of WeekMenu objects with different field values")
    public void testEquals_differentFields() {
        User user1 = new User();
        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        Recipe recipe3 = new Recipe();
        Recipe recipe4 = new Recipe();
        Recipe recipe5 = new Recipe();
        String type1 = "Lunch";
        String type2 = "Dinner";

        WeekMenu weekMenu1 = new WeekMenu(user1, recipe1, recipe2, recipe3, recipe4, recipe5, type1);
        WeekMenu weekMenu2 = new WeekMenu(user1, recipe1, recipe2, recipe3, recipe4, recipe5, type2);

        assertNotEquals(weekMenu1.toString(), weekMenu2.toString());
    }

    @Test
    @DisplayName("Test hashCode method")
    public void testHashCode() {
        User user = new User();
        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        Recipe recipe3 = new Recipe();
        Recipe recipe4 = new Recipe();
        Recipe recipe5 = new Recipe();
        String type = "Lunch";

        WeekMenu weekMenu1 = new WeekMenu(user, recipe1, recipe2, recipe3, recipe4, recipe5, type);
        WeekMenu weekMenu2 = new WeekMenu(user, recipe1, recipe2, recipe3, recipe4, recipe5, type);

        assertEquals(weekMenu1.hashCode(), weekMenu2.hashCode());
    }
}
