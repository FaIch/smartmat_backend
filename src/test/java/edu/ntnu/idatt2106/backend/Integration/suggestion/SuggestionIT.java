package edu.ntnu.idatt2106.backend.Integration.suggestion;

import edu.ntnu.idatt2106.backend.model.fridge.Fridge;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.model.user.UserRequest;
import edu.ntnu.idatt2106.backend.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SuggestionIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private FridgeItemRepository fridgeItemRepository;

    @Autowired
    private UserRepository userRepository;

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
        fridgeItemRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("Test that a user can get suggested items")
    public void testGetSuggestedItems() {
        Item item1 = new Item();
        item1.setName("Milk");
        item1.setShortDesc("Whole milk");
        item1.setCategory(Category.DAIRY);
        item1.setPrice(1.99);
        item1.setWeightPerUnit(1.0);
        item1.setImage(null);
        item1 = itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Milk");
        item2.setShortDesc("Whole milk");
        item2.setCategory(Category.DAIRY);
        item2.setPrice(1.99);
        item2.setWeightPerUnit(1.0);
        item2.setImage(null);
        itemRepository.save(item2);

        Item item3 = new Item();
        item3.setName("Milk");
        item3.setShortDesc("Whole milk");
        item3.setCategory(Category.DAIRY);
        item3.setPrice(1.99);
        item3.setWeightPerUnit(1.0);
        item3.setImage(null);
        itemRepository.save(item3);


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

        FridgeItem fridgeItem = new FridgeItem();
        fridgeItem.setQuantity(1);
        fridgeItem.setExpirationDate(LocalDate.now().plusDays(10));
        fridgeItem.setItem(item1);
        fridgeItem.setFridge(fridge);
        fridgeItem = fridgeItemRepository.save(fridgeItem);

        ShoppingListItem shoppingListItemRequest = new ShoppingListItem(2, item1);
        shoppingListItemRepository.save(shoppingListItemRequest);

        ResponseEntity<List<Item>> response = restTemplate.exchange(
                baseURL + "/suggestion/get",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}