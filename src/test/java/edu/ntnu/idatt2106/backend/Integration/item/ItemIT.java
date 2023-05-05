package edu.ntnu.idatt2106.backend.Integration.item;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.item.Unit;
import edu.ntnu.idatt2106.backend.model.user.UserRequest;
import edu.ntnu.idatt2106.backend.repository.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ItemRepository itemRepository;

    @LocalServerPort
    public int port;

    private String baseURL;
    private HttpHeaders authHeaders;

    @BeforeEach
    public void setUp() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        baseURL = "http://localhost:" + port;

        UserRequest userRequest = new UserRequest("testnewuser@test.com", "testPassword");

        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user/create", request, String.class);

        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user/login", request, String.class);

        String jwtAccessToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTAccessToken="))
                .findFirst().get().substring("JWTAccessToken=".length());

        String jwtRefreshToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTRefreshToken="))
                .findFirst().get().substring("JWTRefreshToken=".length());

        authHeaders = new HttpHeaders();
        authHeaders.add(HttpHeaders.COOKIE, "JWTAccessToken=" + jwtAccessToken);
        authHeaders.add(HttpHeaders.COOKIE, "JWTRefreshToken=" + jwtRefreshToken);

        HttpEntity<?> authRequest = new HttpEntity<>(authHeaders);
    }

    @AfterEach
    public void tearDown() {
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("Test that you can get a list of all items")
    public void testList() {
        HttpEntity<String> getRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<List> getResponse = restTemplate.exchange(baseURL + "/items/list",
                HttpMethod.GET, getRequest, List.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    }

    @Test
    @DisplayName("Test that you can get an item by its id")
    public void testGetItemById() {
        Item item = new Item();
        item.setId(100L);
        item.setName("Test Item");
        item.setShortDesc("Test Description");
        item.setCategory(Category.DAIRY);
        item.setPrice(1.99);
        item.setWeightPerUnit(1.0);
        item.setImage("");
        item.setUnit(Unit.ITEM);
        item.setBaseAmount(10);
        item = itemRepository.save(item);

        HttpEntity<String> getRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<Item> getResponse = restTemplate.exchange(baseURL + "/items/list/" + item.getId(),
                HttpMethod.GET, getRequest, Item.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(item.getName(), getResponse.getBody().getName());
    }

    @Test
    @DisplayName("Test that you can get all categories")
    public void testGetCategories() {
        HttpEntity<String> getRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<List> getResponse = restTemplate.exchange(baseURL + "/items/categories",
                HttpMethod.GET, getRequest, List.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(Category.values().length, getResponse.getBody().size());
    }

    @Test
    @DisplayName("Test that you can get a list of items by category")
    public void testGetItemsByCategory() {
        HttpEntity<String> getRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<List> getResponse = restTemplate.exchange(baseURL + "/items/list/category?category=DAIRY",
                HttpMethod.GET, getRequest, List.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    }
}
