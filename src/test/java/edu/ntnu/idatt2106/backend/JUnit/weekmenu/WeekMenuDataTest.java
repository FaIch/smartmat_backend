package edu.ntnu.idatt2106.backend.JUnit.weekmenu;

import edu.ntnu.idatt2106.backend.model.weekMenu.WeekMenuData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeekMenuDataTest {

    @Test
    public void testGettersAndSetters() {
        WeekMenuData weekMenuData = new WeekMenuData();

        weekMenuData.setTotalAmountOfItems(5);
        assertEquals(5, weekMenuData.getTotalAmountOfItems());

        weekMenuData.setTotalAmountOfMissingItems(2);
        assertEquals(2, weekMenuData.getTotalAmountOfMissingItems());

        weekMenuData.setTotalAmountOfItemsToExpire(3);
        assertEquals(3, weekMenuData.getTotalAmountOfItemsToExpire());
    }
}
