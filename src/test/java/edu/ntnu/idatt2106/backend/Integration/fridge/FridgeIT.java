package edu.ntnu.idatt2106.backend.Integration.fridge;

import edu.ntnu.idatt2106.backend.model.fridge.Fridge;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItemRequest;
import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.user.User;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FridgeIT {

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

        authRequest = new HttpEntity<>(authHeaders);
    }

    @AfterEach
    public void clearDatabase() {
        fridgeItemRepository.deleteAll();
        fridgeRepository.deleteAll();
        subUserRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test that created user has fridge")
    public void testCreatedUserHasFridge(){
        ResponseEntity<String> response = restTemplate.exchange(baseURL + "/fridge/get",
                HttpMethod.GET, authRequest, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test that fridge items can be added")
    public void testAddFridgeItems() {
        Item item = new Item();
        item.setName("Milk");
        item.setShortDesc("Whole milk");
        item.setCategory(Category.DAIRY);
        item.setPrice(1.99);
        item.setWeightPerUnit(1.0);
        item.setImage(null);
        item = itemRepository.save(item);

        FridgeItemRequest fridgeItemRequest = new FridgeItemRequest(
                item.getId(),
                2,
                LocalDate.now().plusDays(10)
        );

        HttpEntity<List<FridgeItemRequest>> request = new HttpEntity<>(List.of(fridgeItemRequest), authHeaders);

        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/fridge/add", request
                , String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fridge items added", response.getBody());
    }

    @Test
    @DisplayName("Test that fridge items can be removed")
    public void testRemoveFridgeItems() {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase("testnewuser@test.com");
        if (optionalUser.isEmpty()) {
            fail("User not found");
        }
        User user = optionalUser.get();

        Fridge fridge = new Fridge();
        fridge.setUser(user);
        fridge = fridgeRepository.save(fridge);
        user.setFridge(fridge);
        userRepository.save(user);

        Item item = new Item();
        item.setName("Milk");
        item.setShortDesc("Whole milk");
        item.setCategory(Category.DAIRY);
        item.setPrice(1.99);
        item.setWeightPerUnit(1.0);
        item.setImage(null);
        item = itemRepository.save(item);

        FridgeItem fridgeItem = new FridgeItem();
        fridgeItem.setQuantity(1);
        fridgeItem.setExpirationDate(LocalDate.now().plusDays(10));
        fridgeItem.setItem(item);
        fridgeItem.setFridge(fridge);
        fridgeItem = fridgeItemRepository.save(fridgeItem);
        List<Long> fridgeItemIds = List.of(fridgeItem.getId());

        HttpEntity<?> removeRequest = new HttpEntity<>(fridgeItemIds, authHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                baseURL + "/fridge/remove",
                HttpMethod.DELETE,
                removeRequest,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fridge items removed", response.getBody());
        assertFalse(fridgeItemRepository.findById(fridgeItem.getId()).isPresent());
    }

    @Test
    @DisplayName("Test that fridge item can be updated")
    public void testEditFridgeItem() {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase("testnewuser@test.com");
        if (optionalUser.isEmpty()) {
            fail("User not found");
        }
        User user = optionalUser.get();

        Fridge fridge = new Fridge();
        fridge.setUser(user);
        fridge = fridgeRepository.save(fridge);
        user.setFridge(fridge);
        userRepository.save(user);

        Item item = new Item();
        item.setName("Milk");
        item.setShortDesc("Whole milk");
        item.setCategory(Category.DAIRY);
        item.setPrice(1.99);
        item.setWeightPerUnit(1.0);
        item.setImage(null);
        item = itemRepository.save(item);

        FridgeItem fridgeItem = new FridgeItem();
        fridgeItem.setQuantity(1);
        fridgeItem.setExpirationDate(LocalDate.now().plusDays(10));
        fridgeItem.setItem(item);
        fridgeItem.setFridge(fridge);
        fridgeItem = fridgeItemRepository.save(fridgeItem);

        FridgeItemRequest updatedFridgeItem = new FridgeItemRequest(item.getId(), 5, LocalDate.now().plusDays(20));

        HttpEntity<FridgeItemRequest> updateQuantityRequest = new HttpEntity<>(updatedFridgeItem, authHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                baseURL + "/fridge/edit/" + fridgeItem.getId(),
                HttpMethod.PUT,
                updateQuantityRequest,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        FridgeItem updatedFridgeItemInRepo = fridgeItemRepository.findById(fridgeItem.getId()).get();
        assertEquals(5, updatedFridgeItemInRepo.getQuantity());
        assertEquals(LocalDate.now().plusDays(20), updatedFridgeItemInRepo.getExpirationDate());
    }

}
