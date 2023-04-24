package edu.ntnu.idatt2106.backend.JUnit.waste;

import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.model.waste.Waste;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class WasteTest {

    private User user;
    private LocalDate entryDate;
    private Waste waste1;
    private Waste waste2;
    private Waste waste3;

    @BeforeEach
    public void setUp() {
        user = new User();
        entryDate = LocalDate.now();

        waste1 = new Waste(user, 5, entryDate);
        waste2 = new Waste(user, 5, entryDate);
        waste3 = new Waste(user, 10, entryDate);
    }

    @Test
    public void testConstructorsAndGetters() {
        Waste waste = new Waste(user, 7, entryDate);
        assertEquals(user, waste.getUser());
        assertEquals(7, waste.getWeight());
        assertEquals(entryDate, waste.getEntryDate());
    }

    @Test
    public void testSetters() {
        Waste waste = new Waste();
        waste.setUser(user);
        waste.setWeight(8);
        waste.setEntryDate(entryDate);

        assertEquals(user, waste.getUser());
        assertEquals(8, waste.getWeight());
        assertEquals(entryDate, waste.getEntryDate());
    }

    @Test
    public void testEqualsAndHashCode() {
        assertEquals(waste1, waste1);
        assertEquals(waste1.hashCode(), waste1.hashCode());

        waste1.setId(1L);
        waste2.setId(1L);
        assertEquals(waste1, waste2);
        assertEquals(waste1.hashCode(), waste2.hashCode());

        assertNotEquals(waste1, waste3);
        assertNotEquals(waste1.hashCode(), waste3.hashCode());

        assertNotEquals(waste1, null);
    }
}