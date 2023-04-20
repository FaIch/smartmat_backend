package edu.ntnu.idatt2106.backend.user;

import edu.ntnu.idatt2106.backend.model.user.Role;
import edu.ntnu.idatt2106.backend.model.user.SubUser;
import edu.ntnu.idatt2106.backend.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SubUserJUnitTest {
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
}
