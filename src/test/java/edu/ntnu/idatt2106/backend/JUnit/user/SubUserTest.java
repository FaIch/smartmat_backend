package edu.ntnu.idatt2106.backend.JUnit.user;

import edu.ntnu.idatt2106.backend.model.user.Role;
import edu.ntnu.idatt2106.backend.model.user.SubUser;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubUserTest {
    private SubUser subUser;
    private User mainUser;

    @BeforeEach
    void setUp() {
        mainUser = new User("test@example.com", 1234567890L, "Test Address");
        subUser = new SubUser("TestNickname", Role.PARENT);
    }

    @Test
    void testNoArgsConstructor() {
        SubUser emptySubUser = new SubUser();
        assertNull(emptySubUser.getNickname());
        assertNull(emptySubUser.getRole());
        assertNull(emptySubUser.getMainUser());
    }

    @Test
    void testAllArgsConstructor() {
        assertEquals("TestNickname", subUser.getNickname());
        assertEquals(Role.PARENT, subUser.getRole());
        assertNull(subUser.getMainUser());
    }

    @Test
    void testSettersAndGetters() {
        subUser.setId(1L);
        subUser.setNickname("UpdatedNickname");
        subUser.setRole(Role.CHILD);

        assertEquals(1L, subUser.getId());
        assertEquals("UpdatedNickname", subUser.getNickname());
        assertEquals(Role.CHILD, subUser.getRole());
    }

    @Test
    void testMainUserRelationship() {
        subUser.setMainUser(mainUser);
        assertEquals(mainUser, subUser.getMainUser());
    }

    @Test
    void testEqualsAndHashCode() {
        User mainUser = new User("test@example.com", 1234567890L, "Test Address");

        SubUser subUser1 = new SubUser("TestNickname1", Role.PARENT);
        subUser1.setMainUser(mainUser);
        subUser1.setId(1L);

        SubUser subUser2 = new SubUser("TestNickname2", Role.CHILD);
        subUser2.setMainUser(mainUser);
        subUser2.setId(2L);

        SubUser subUser3 = new SubUser("TestNickname1", Role.PARENT);
        subUser3.setMainUser(mainUser);
        subUser3.setId(1L);

        assertNotEquals(subUser1, subUser2);
        assertNotEquals(subUser1.hashCode(), subUser2.hashCode());

        assertEquals(subUser1, subUser3);
        assertEquals(subUser1.hashCode(), subUser3.hashCode());
    }
}
