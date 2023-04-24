package edu.ntnu.idatt2106.backend.Integration.user;

import edu.ntnu.idatt2106.backend.model.user.UserRequest;
import edu.ntnu.idatt2106.backend.repository.SubUserRepository;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
import org.junit.jupiter.api.*;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import java.io.IOException;
import org.springframework.http.*;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIT {

    @LocalServerPort
    public int port;

    @Autowired
    public TestRestTemplate restTemplate;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public SubUserRepository subUserRepository;

    private String token;
    private HttpHeaders headers;
    private String baseURL;
    private UserRequest userRequest;

    @BeforeEach
    public void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        baseURL = "http://localhost:" + port;
        userRequest = new UserRequest("testnewuser@test.com", "testPassword");
    }

    @AfterEach
    public void clearDatabase() {
        subUserRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test that a user can be created and that the response is OK")
    public void testCreateUser() {
        userRequest = new UserRequest("testnewuser@test.com", "testPassword");
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user-without-child",
                request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User created", response.getBody());
    }

    @Test
    @DisplayName ("Test that a user with same email cannot be created")
    public void testCreateDuplicateUser() {
        UserRequest userRequest = new UserRequest("testnewuser@test.com", "testPassword");
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user-without-child", request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User created", response.getBody());


        UserRequest duplicateUserRequest = new UserRequest("testnewuser@test.com", "testPassword2");
        HttpEntity<UserRequest> duplicateRequest = new HttpEntity<>(duplicateUserRequest, headers);
        ResponseEntity<String> duplicateResponse = restTemplate.postForEntity(baseURL + "/user-without-child", duplicateRequest, String.class);
        assertEquals(HttpStatus.CONFLICT, duplicateResponse.getStatusCode());
        assertEquals("User with given email already exists", duplicateResponse.getBody());
    }

    @Test
    @DisplayName("Test that a user can be logged in and that the response is OK")
    public void testLogin() throws IOException {
        userRequest = new UserRequest("testnewuser@test.com", "testPassword");
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user-without-child", request, String.class);

        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/login", request, String.class);
        String responseBody = response.getBody();
        Map<String, Object> responseMap = new ObjectMapper()
                .readValue(responseBody, new TypeReference<Map<String, Object>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", responseMap.get("message"));
        token = (String) responseMap.get("token");
    }

    @Test
    @DisplayName("Test that logging in with a non-existent email returns a HTTP Unauthorized status")
    public void testLoginWithNonExistentEmail() throws IOException {
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user-without-child", request, String.class);

        UserRequest invalidUserRequest = new UserRequest("nonexistentuser@test.com", "testPassword");
        HttpEntity<UserRequest> invalidRequest = new HttpEntity<>(invalidUserRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/login", invalidRequest, String.class);
        Map<String, Object> responseMap = new ObjectMapper()
                .readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User with given email does not exist", responseMap.get("message"));
    }

    @Test
    @DisplayName("Test that logging in with a wrong password returns a HTTP Unauthorized status")
    public void testLoginWithWrongPassword() throws IOException {
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user-without-child", request, String.class);

        UserRequest invalidUserRequest = new UserRequest("testnewuser@test.com", "wrongPassword");
        HttpEntity<UserRequest> invalidRequest = new HttpEntity<>(invalidUserRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/login", invalidRequest, String.class);
        Map<String, Object> responseMap = new ObjectMapper()
                .readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Password is incorrect", responseMap.get("message"));
    }

    @Test
    @DisplayName("Test that getting subusers returns the subusers of the logged in user")
    public void testGetSubusers() throws IOException {
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user-without-child", request, String.class);

        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/login", request, String.class);
        Map<String, Object> responseMap = new ObjectMapper()
                .readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

        Map<String, Object> userRequestMap = (Map<String, Object>) responseMap.get("userRequest");

        token = (String) userRequestMap.get("password");
        System.out.println(token);

        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth(token);

        HttpEntity<?> authRequest = new HttpEntity<>(authHeaders);


        ResponseEntity<String> subUsersResponse = restTemplate.exchange(baseURL + "/user/getSubUsers",
                HttpMethod.GET, authRequest, String.class);
        assertEquals(HttpStatus.OK, subUsersResponse.getStatusCode());
    }


}
