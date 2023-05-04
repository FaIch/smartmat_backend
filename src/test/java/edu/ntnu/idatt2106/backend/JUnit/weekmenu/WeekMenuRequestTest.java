package edu.ntnu.idatt2106.backend.JUnit.weekmenu;

import edu.ntnu.idatt2106.backend.model.WeekMenu.WeekMenuRequest;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WeekMenuRequestTest {

    @Test
    public void testWeekMenuRequest() {
        List<Integer> intList = Arrays.asList(1, 2, 3);
        String message = "Test message";
        WeekMenuRequest request = new WeekMenuRequest(intList, message);

        assertNotNull(request.getIntList());
        assertNotNull(request.getMessage());
        assertEquals(intList, request.getIntList());
        assertEquals(message, request.getMessage());
    }

}