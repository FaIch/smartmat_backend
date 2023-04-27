package edu.ntnu.idatt2106.backend.Integration.shoppinglist;

import edu.ntnu.idatt2106.backend.model.fridge.FridgeItemRequest;
import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItemRequest;
import edu.ntnu.idatt2106.backend.model.user.UserRequest;
import edu.ntnu.idatt2106.backend.repository.ItemRepository;
import edu.ntnu.idatt2106.backend.repository.ShoppingListItemRepository;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingListIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;

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
    }

    @AfterEach
    public void tearDown() {
        shoppingListItemRepository.deleteAll();
        itemRepository.deleteAll();
    }


    @Test
    @DisplayName("Test that a shopping list can be retrieved by user id")
    public void testGetShoppingListItemsByUserId() {
        HttpEntity<String> getRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<List> getResponse = restTemplate.exchange(baseURL + "/shopping-list/get",
                HttpMethod.GET, getRequest, List.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    }

    @Test
    @DisplayName("Test that a shopping list item can be added")
    public void testAddShoppingListItem() {
        Item item = new Item();
        item.setName("Milk");
        item.setShortDesc("Whole milk");
        item.setCategory(Category.DAIRY);
        item.setPrice(1.99);
        item.setWeightPerUnit(1.0);
        item.setImage(null);
        item = itemRepository.save(item);

        ShoppingListItemRequest shoppingListItemRequest = new ShoppingListItemRequest(item.getId(), 2);

        HttpEntity<?> request = new HttpEntity<>(List.of(shoppingListItemRequest), authHeaders);
        ResponseEntity<String> postResponse = restTemplate.postForEntity(baseURL + "/shopping-list/add", request,
                String.class);

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
    }

    @Test
    @DisplayName("Test that you can remove a shopping list item by its id")
    public void testRemoveShoppingListItem() {
        Item item = new Item();
        item.setName("Milk");
        item.setShortDesc("Whole milk");
        item.setCategory(Category.DAIRY);
        item.setPrice(1.99);
        item.setWeightPerUnit(1.0);
        item.setImage(null);
        item = itemRepository.save(item);

        ShoppingListItemRequest shoppingListItemRequest = new ShoppingListItemRequest(item.getId(), 2);
        HttpEntity<?> addRequest = new HttpEntity<>(List.of(shoppingListItemRequest), authHeaders);
        ResponseEntity<String> addResponse = restTemplate.postForEntity(baseURL + "/shopping-list/add", addRequest, String.class);
        assertEquals(HttpStatus.OK, addResponse.getStatusCode());

        HttpEntity<String> getRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<List> getResponse = restTemplate.exchange(baseURL + "/shopping-list/get",
                HttpMethod.GET, getRequest, List.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        List<Map<String, Object>> shoppingListItems = getResponse.getBody();
        assert shoppingListItems != null;
        assertFalse(shoppingListItems.isEmpty());

        Long shoppingListItemId = ((Number) shoppingListItems.get(0).get("id")).longValue();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseURL + "/shopping-list/remove")
                .queryParam("shoppingListItemIds", shoppingListItemId);

        HttpEntity<?> removeRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<String> removeResponse = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, removeRequest, String.class);

        assertEquals(HttpStatus.OK, removeResponse.getStatusCode());
    }

    @Test
    @DisplayName("Test updateShoppingListItemQuantity")
    public void testUpdateShoppingListItemQuantity() {
        Item item = new Item();
        item.setName("Milk");
        item.setShortDesc("Whole milk");
        item.setCategory(Category.DAIRY);
        item.setPrice(1.99);
        item.setWeightPerUnit(1.0);
        item.setImage(null);
        item = itemRepository.save(item);

        ShoppingListItemRequest shoppingListItemRequest = new ShoppingListItemRequest(item.getId(), 2);
        HttpEntity<?> addRequest = new HttpEntity<>(List.of(shoppingListItemRequest), authHeaders);
        ResponseEntity<String> addResponse = restTemplate.postForEntity(baseURL + "/shopping-list/add", addRequest, String.class);
        assertEquals(HttpStatus.OK, addResponse.getStatusCode());

        HttpEntity<String> getRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<List> getResponse = restTemplate.exchange(baseURL + "/shopping-list/get",
                HttpMethod.GET, getRequest, List.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        List<Map<String, Object>> shoppingListItems = getResponse.getBody();
        assertFalse(shoppingListItems.isEmpty());

        Long shoppingListItemId = ((Number) shoppingListItems.get(0).get("id")).longValue();
        int updatedShoppingListItemQuantity = 3;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseURL + "/shopping-list/update")
                .queryParam("shoppingListItemId", shoppingListItemId)
                .queryParam("updatedShoppingListItemQuantity", updatedShoppingListItemQuantity);

        HttpEntity<?> updateRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<String> updateResponse = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, updateRequest, String.class);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
    }
}
