package edu.ntnu.idatt2106.backend.JUnit.user;

import edu.ntnu.idatt2106.backend.model.user.Role;
import edu.ntnu.idatt2106.backend.model.user.SubUserRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubUserRequestTest {

    @Test
    void testAllArgsConstructor() {
        SubUserRequest request = new SubUserRequest("test@example.com", "TestNickname", Role.PARENT, 1234);

        assertEquals("test@example.com", request.getUserEmail());
        assertEquals("TestNickname", request.getNickname());
        assertEquals(Role.PARENT, request.getRole());
    }

    @Test
    void testConstructorWithoutRole() {
        SubUserRequest request = new SubUserRequest("test@example.com", "TestNickname");

        assertEquals("test@example.com", request.getUserEmail());
        assertEquals("TestNickname", request.getNickname());
        assertNull(request.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        SubUserRequest request1 = new SubUserRequest("test@example.com", "TestNickname", Role.PARENT, 1234);
        SubUserRequest request2 = new SubUserRequest("test@example.com", "TestNickname", Role.CHILD, 4321);
        SubUserRequest request3 = new SubUserRequest("test@example.com", "TestNickname2", Role.PARENT, 2134);
        SubUserRequest request4 = new SubUserRequest("test@example.com", "TestNickname", Role.PARENT, 1234);

        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());

        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());

        assertEquals(request1, request4);
        assertEquals(request1.hashCode(), request4.hashCode());
    }
}
