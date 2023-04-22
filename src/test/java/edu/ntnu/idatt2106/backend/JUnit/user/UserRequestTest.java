package edu.ntnu.idatt2106.backend.JUnit.user;

import edu.ntnu.idatt2106.backend.model.user.UserRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestTest {

    @Test
    void testEqualsAndHashCode() {
        UserRequest userRequest1 = new UserRequest("test@example.com", 123456789L, "Test Address", "password123");
        UserRequest userRequest2 = new UserRequest("test@example.com", 123456789L, "Test Address", "password123");
        UserRequest userRequest3 = new UserRequest("test2@example.com", 987654321L, "Test Address 2", "password456");

        assertEquals(userRequest1, userRequest2);
        assertEquals(userRequest1.hashCode(), userRequest2.hashCode());

        assertNotEquals(userRequest1, userRequest3);
        assertNotEquals(userRequest1.hashCode(), userRequest3.hashCode());
    }

    @Test
    void testAllArgsConstructor() {
        UserRequest userRequest = new UserRequest("test@example.com", 123456789L, "Test Address", "password123");

        assertEquals("test@example.com", userRequest.getEmail());
        assertEquals(123456789L, userRequest.getPhoneNumber());
        assertEquals("Test Address", userRequest.getAddress());
        assertEquals("password123", userRequest.getPassword());
    }
}
