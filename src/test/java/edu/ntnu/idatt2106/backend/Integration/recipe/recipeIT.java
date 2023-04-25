package edu.ntnu.idatt2106.backend.Integration.recipe;

import edu.ntnu.idatt2106.backend.model.user.UserRequest;
import edu.ntnu.idatt2106.backend.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class recipeIT {

    @LocalServerPort
    public int port;
    @Autowired
    public TestRestTemplate restTemplate;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public SubUserRepository subUserRepository;

    @Autowired
    public ItemRepository itemRepository;

    @Autowired
    public FridgeRepository fridgeRepository;

    @Autowired
    public FridgeItemRepository fridgeItemRepository;

    private String baseURL;

    private HttpHeaders authHeaders;

    private HttpEntity<?> authRequest;

    @BeforeEach
    public void setUp() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        baseURL = "http://localhost:" + port;

        UserRequest userRequest = new UserRequest("testnewuser@test.com", "testPassword");

        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user-without-child", request, String.class);

        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/login", request, String.class);

        String jwtAccessToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTAccessToken="))
                .findFirst().get().substring("JWTAccessToken=".length());

        String jwtRefreshToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTRefreshToken="))
                .findFirst().get().substring("JWTRefreshToken=".length());

        authHeaders = new HttpHeaders();
        authHeaders.add(HttpHeaders.COOKIE, "JWTAccessToken=" + jwtAccessToken);
        authHeaders.add(HttpHeaders.COOKIE, "JWTRefreshToken=" + jwtRefreshToken);

        authRequest = new HttpEntity<>(authHeaders);
    }

    @AfterEach
    public void clearDatabase() {
        subUserRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("Test that all recipes can be retrieved when user logged in")
    public void testListAllRecipes(){
        ResponseEntity<String> response = restTemplate.exchange(baseURL + "/recipe/list",
                HttpMethod.GET, authRequest, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    @DisplayName("Test that recipes can be displayed based on item in the recipe")
    public void testGetRecipesByItemName(){
        String itemName = "Steak";
        ResponseEntity<String> response = restTemplate.exchange(baseURL + "/recipe/by-item-name?itemName=" + itemName,
                HttpMethod.GET, authRequest, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test that recipes gets sorted in order of the most items in recipe and in fridge")
    public void testSortRecipesByFridge(){
        ResponseEntity<String> response = restTemplate.exchange(baseURL + "/recipe/sorted-by-fridge",
                HttpMethod.GET, authRequest, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
