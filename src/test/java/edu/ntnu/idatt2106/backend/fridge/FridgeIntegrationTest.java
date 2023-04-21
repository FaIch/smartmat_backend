package edu.ntnu.idatt2106.backend.fridge;

import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.user.UserRequest;
import edu.ntnu.idatt2106.backend.repository.FridgeItemRepository;
import edu.ntnu.idatt2106.backend.repository.FridgeRepository;
import edu.ntnu.idatt2106.backend.repository.ItemRepository;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FridgeIntegrationTest {

    @LocalServerPort
    public int port;

    @Autowired
    public TestRestTemplate restTemplate;

    @Autowired
    public UserRepository userRepository;

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
        restTemplate.postForEntity(baseURL + "/user", request, String.class);

        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/login", request, String.class);
        Map<String, Object> responseMap = new ObjectMapper()
                .readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

        Map<String, Object> userRequestMap = (Map<String, Object>) responseMap.get("userRequest");

        authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth((String) userRequestMap.get("password"));

        authRequest = new HttpEntity<>(authHeaders);
    }

    @AfterEach
    public void clearDatabase() {
        userRepository.deleteAll();
        fridgeRepository.deleteAll();
        fridgeItemRepository.deleteAll();
    }

    @Test
    @DisplayName("Test that created user has fridge")
    public void testCreatedUserHasFridge(){
        ResponseEntity<String> response = restTemplate.exchange(baseURL + "/user/fridge-items",
                HttpMethod.GET, authRequest, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test that fridge item can be added")
    public void testAddFridgeItem() {
        Item item = new Item();
        item.setName("Milk");
        item.setShortDesc("Whole milk");
        item.setCategory(Category.DAIRY);
        item.setPrice(1.99);
        item.setWeight(1.0);
        item.setImage(null);
        item = itemRepository.save(item);

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("quantity", String.valueOf(2));
        paramMap.add("expirationDate", String.valueOf(LocalDate.now().plusDays(10)));
        paramMap.add("itemId", String.valueOf(item.getId()));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, authHeaders);

        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/fridge/add", request
                , String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fridge item added", response.getBody());
    }

    @Test
    @DisplayName("Test that fridge item can be removed")
    public void testRemoveFridgeItem() {
        Item item = new Item();
        item.setName("Milk");
        item.setShortDesc("Whole milk");
        item.setCategory(Category.DAIRY);
        item.setPrice(1.99);
        item.setWeight(1.0);
        item.setImage(null);
        item = itemRepository.save(item);

        FridgeItem fridgeItem = new FridgeItem();
        fridgeItem.setQuantity(1);
        fridgeItem.setExpirationDate(LocalDate.now().plusDays(10));
        fridgeItem.setItem(item);

        fridgeItem = fridgeItemRepository.save(fridgeItem);

        HttpEntity<?> removeRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                baseURL + "/fridge-items/" + fridgeItem.getId(),
                HttpMethod.DELETE,
                removeRequest,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fridge item removed", response.getBody());

        assertFalse(fridgeItemRepository.findById(fridgeItem.getId()).isPresent());
    }

    @Test
    @DisplayName("Test that fridge item quantity can be updated")
    public void testUpdateFridgeItemQuantity() {
        Item item = new Item();
        item.setName("Milk");
        item.setShortDesc("Whole milk");
        item.setCategory(Category.DAIRY);
        item.setPrice(1.99);
        item.setWeight(1.0);
        item.setImage(null);
        item = itemRepository.save(item);

        FridgeItem fridgeItem = new FridgeItem();
        fridgeItem.setQuantity(1);
        fridgeItem.setExpirationDate(LocalDate.now().plusDays(10));
        fridgeItem.setItem(item);

        fridgeItem = fridgeItemRepository.save(fridgeItem);

        FridgeItem updatedFridgeItem = new FridgeItem();
        updatedFridgeItem.setQuantity(2);

        HttpEntity<FridgeItem> updateQuantityRequest = new HttpEntity<>(updatedFridgeItem, authHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                baseURL + "/fridge-items/editQuantity/" + fridgeItem.getId(),
                HttpMethod.PUT,
                updateQuantityRequest,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fridge item quantity updated", response.getBody());

        FridgeItem updatedFridgeItemInRepo = fridgeItemRepository.findById(fridgeItem.getId()).get();
        assertEquals(2, updatedFridgeItemInRepo.getQuantity());
    }

    @Test
    @DisplayName("Test that fridge item expiration date can be updated")
    public void testUpdateFridgeItemExpirationDate() {
        Item item = new Item();
        item.setName("Milk");
        item.setShortDesc("Whole milk");
        item.setCategory(Category.DAIRY);
        item.setPrice(1.99);
        item.setWeight(1.0);
        item.setImage(null);
        item = itemRepository.save(item);

        FridgeItem fridgeItem = new FridgeItem();
        fridgeItem.setQuantity(1);
        fridgeItem.setExpirationDate(LocalDate.now().plusDays(10));
        fridgeItem.setItem(item);

        fridgeItem = fridgeItemRepository.save(fridgeItem);

        FridgeItem updatedFridgeItem = new FridgeItem();
        updatedFridgeItem.setExpirationDate(LocalDate.now().plusDays(20));

        HttpEntity<FridgeItem> updateExpirationDateRequest = new HttpEntity<>(updatedFridgeItem, authHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                baseURL + "/fridge-items/editExpirationDate/" + fridgeItem.getId(),
                HttpMethod.PUT,
                updateExpirationDateRequest,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fridge item expiration date updated", response.getBody());

        FridgeItem updatedFridgeItemInRepo = fridgeItemRepository.findById(fridgeItem.getId()).get();
        assertEquals(LocalDate.now().plusDays(20), updatedFridgeItemInRepo.getExpirationDate());
    }
}
