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
import org.springframework.web.util.UriComponentsBuilder;
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
    @DisplayName("Test that a user can be created without child and that the response is OK")
    public void testCreateUserWithoutChild() {
        userRequest = new UserRequest("testnewuser@test.com", "testPassword");
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user/create",
                request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test that a user can be created with child and that the response is OK")
    public void testCreateUserWithChild() {
        userRequest = new  UserRequest("testnewuser@test.com", "testPassword");
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user/create/child",
                request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName ("Test that a user with same email cannot be created")
    public void testCreateDuplicateUser() {
        UserRequest userRequest = new UserRequest("testnewuser@test.com", "testPassword");
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user/create", request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());


        UserRequest duplicateUserRequest = new UserRequest("testnewuser@test.com", "testPassword2");
        HttpEntity<UserRequest> duplicateRequest = new HttpEntity<>(duplicateUserRequest, headers);
        ResponseEntity<String> duplicateResponse = restTemplate.postForEntity(baseURL + "/user/create", duplicateRequest, String.class);
        assertEquals(HttpStatus.CONFLICT, duplicateResponse.getStatusCode());
        assertEquals("Bruker med gitt e-post eksisterer allerede", duplicateResponse.getBody());
    }

    @Test
    @DisplayName("Test that a user can be logged in and that the response is OK")
    public void testLogin() throws IOException {
        userRequest = new UserRequest("testnewuser@test.com", "testPassword");
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user/create", request, String.class);

        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user/login", request, String.class);
        String responseBody = response.getBody();
        Map<String, Object> responseMap = new ObjectMapper()
                .readValue(responseBody, new TypeReference<Map<String, Object>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test that logging in with a non-existent email returns a HTTP Unauthorized status")
    public void testLoginWithNonExistentEmail() throws IOException {
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user/create", request, String.class);

        UserRequest invalidUserRequest = new UserRequest("nonexistentuser@test.com", "testPassword");
        HttpEntity<UserRequest> invalidRequest = new HttpEntity<>(invalidUserRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user/login", invalidRequest, String.class);
        Map<String, Object> responseMap = new ObjectMapper()
                .readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bruker med gitt e-post eksisterer ikke", responseMap.get("message"));
    }

    @Test
    @DisplayName("Test that logging in with a wrong password returns a HTTP Unauthorized status")
    public void testLoginWithWrongPassword() throws IOException {
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user/create", request, String.class);

        UserRequest invalidUserRequest = new UserRequest("testnewuser@test.com", "wrongPassword");
        HttpEntity<UserRequest> invalidRequest = new HttpEntity<>(invalidUserRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user/login", invalidRequest, String.class);
        Map<String, Object> responseMap = new ObjectMapper()
                .readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Test that getting subusers returns the subusers of the logged in user")
    public void testGetSubUsers() {
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user/create", request, String.class);

        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user/login", request, String.class);

        String jwtAccessToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTAccessToken="))
                .findFirst().get().substring("JWTAccessToken=".length());

        String jwtRefreshToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTRefreshToken="))
                .findFirst().get().substring("JWTRefreshToken=".length());

        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.add(HttpHeaders.COOKIE, "JWTAccessToken=" + jwtAccessToken);
        authHeaders.add(HttpHeaders.COOKIE, "JWTRefreshToken=" + jwtRefreshToken);

        HttpEntity<?> authRequest = new HttpEntity<>(authHeaders);

        ResponseEntity<String> subUsersResponse = restTemplate.exchange(baseURL + "/user/sub-user/get",
                HttpMethod.GET, authRequest, String.class);
        assertEquals(HttpStatus.OK, subUsersResponse.getStatusCode());
    }

    @Test
    @DisplayName("Test that you can edit a user's phone number")
    public void editPhoneNumber() {
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user/create", request, String.class);

        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user/login", request, String.class);

        String jwtAccessToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTAccessToken="))
                .findFirst().get().substring("JWTAccessToken=".length());

        String jwtRefreshToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTRefreshToken="))
                .findFirst().get().substring("JWTRefreshToken=".length());

        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.add(HttpHeaders.COOKIE, "JWTAccessToken=" + jwtAccessToken);
        authHeaders.add(HttpHeaders.COOKIE, "JWTRefreshToken=" + jwtRefreshToken);

        String newPhoneNumber = "93828792";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseURL + "/user/edit/phone")
                .queryParam("phoneNumber", newPhoneNumber);

        HttpEntity<String> putRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<String> putResponse = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, putRequest,
                String.class);
        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
    }

    @Test
    @DisplayName("Test that you can edit a user's address")
    public void editAddress() {
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user/create", request, String.class);

        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user/login", request, String.class);

        String jwtAccessToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTAccessToken="))
                .findFirst().get().substring("JWTAccessToken=".length());

        String jwtRefreshToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTRefreshToken="))
                .findFirst().get().substring("JWTRefreshToken=".length());

        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.add(HttpHeaders.COOKIE, "JWTAccessToken=" + jwtAccessToken);
        authHeaders.add(HttpHeaders.COOKIE, "JWTRefreshToken=" + jwtRefreshToken);

        // Prepare the PUT request to edit the user's address
        String newAddress = "123 New Street, New City, ST 12345";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseURL + "/user/edit/address")
                .queryParam("address", newAddress);

        HttpEntity<String> putRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<String> putResponse = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, putRequest,
                String.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
    }


    @Test
    @DisplayName("Test that you can edit a user's password")
    public void editPassword() {
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user/create", request, String.class);

        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user/login", request, String.class);

        String jwtAccessToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTAccessToken="))
                .findFirst().get().substring("JWTAccessToken=".length());

        String jwtRefreshToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTRefreshToken="))
                .findFirst().get().substring("JWTRefreshToken=".length());

        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.add(HttpHeaders.COOKIE, "JWTAccessToken=" + jwtAccessToken);
        authHeaders.add(HttpHeaders.COOKIE, "JWTRefreshToken=" + jwtRefreshToken);

        // Prepare the PUT request to edit the user's password
        String oldPassword = "testPassword";
        String newPassword = "newTestPassword";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseURL + "/user/edit/password")
                .queryParam("oldPassword", oldPassword)
                .queryParam("newPassword", newPassword);

        HttpEntity<String> putRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<String> putResponse = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, putRequest, String.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
    }
}
