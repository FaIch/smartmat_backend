package edu.ntnu.idatt2106.backend.JUnit.date;
import edu.ntnu.idatt2106.backend.model.date.Date;
import edu.ntnu.idatt2106.backend.model.item.Category;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DateTest {

    @Test
    public void testEquals() {
        Date date1 = new Date(1L, Category.VEGETABLES, 10);
        Date date2 = new Date(1L, Category.VEGETABLES, 10);

        assertEquals(date1, date2);
    }

    @Test
    public void testHashCode() {
        Date date1 = new Date(1L, Category.VEGETABLES, 5);
        Date date2 = new Date(1L, Category.VEGETABLES, 10);

        assertNotEquals(date1.hashCode(), date2.hashCode());
    }

    @Test
    public void testGettersAndSetters() {
        Date date = new Date();

        date.setId(1L);
        date.setCategory(Category.VEGETABLES);
        date.setBadInDays(7);

        assertEquals(1L, date.getId());
        assertEquals(Category.VEGETABLES, date.getCategory());
        assertEquals(7, date.getBadInDays());
    }
}
