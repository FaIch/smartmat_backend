//package edu.ntnu.idatt2106.backend.shoppinglist;
//
//import edu.ntnu.idatt2106.backend.model.item.Category;
//import edu.ntnu.idatt2106.backend.model.item.Item;
//import edu.ntnu.idatt2106.backend.model.user.UserRequest;
//import edu.ntnu.idatt2106.backend.repository.FridgeItemRepository;
//import edu.ntnu.idatt2106.backend.repository.FridgeRepository;
//import edu.ntnu.idatt2106.backend.repository.ItemRepository;
//import edu.ntnu.idatt2106.backend.repository.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.*;
//import org.springframework.test.context.ActiveProfiles;
//import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
//import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//
//@ActiveProfiles("test")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class ShoppingListIntegrationTest {
//
//        @LocalServerPort
//        public int port;
//
//        @Autowired
//        public TestRestTemplate restTemplate;
//
//        @Autowired
//        public UserRepository userRepository;
//
//        @Autowired
//        public ItemRepository itemRepository;
//
//        @Autowired
//        public FridgeRepository fridgeRepository;
//
//        @Autowired
//        public FridgeItemRepository fridgeItemRepository;
//
//        private String baseURL;
//
//        private HttpHeaders authHeaders;
//
//        private HttpEntity<?> authRequest;
//
//        @BeforeEach
//        public void setUp() throws IOException {
//            baseURL = "http://localhost:" + port + "/api/v1/shoppinglist";
//            authHeaders = new HttpHeaders();
//            authHeaders.setContentType(MediaType.APPLICATION_JSON);
//            authHeaders.set("Authorization", "Bearer " + getAuthToken());
//            authRequest = new HttpEntity<>(authHeaders);
//        }
//
//        @AfterEach
//        public void tearDown() {
//            fridgeItemRepository.deleteAll();
//            fridgeRepository.deleteAll();
//            itemRepository.deleteAll();
//            userRepository.deleteAll();
//        }
//
//        @Test
//        @DisplayName("Test that a user can add an item to their shopping list")
//        public void testAddItemToShoppingList() {
//            Item item = new Item("Test item", Category.OTHER, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
//            itemRepository.save(item);
//            ResponseEntity<String> response = restTemplate.exchange(baseURL + "/add/" + item.getId(), HttpMethod.POST, authRequest, String.class);
//            assertEquals(HttpStatus.OK, response.getStatusCode());
//        }
//
//        @Test
//        @DisplayName("Test that a user can remove an item from their shopping list")
//        public void testRemoveItemFromShoppingList() {
//            Item item = new Item("Test item", Category.OTHER, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
//            itemRepository.save(item);
//            restTemplate.exchange(baseURL + "/add/" + item.getId(), HttpMethod.POST, authRequest, String.class);
//            ResponseEntity<String> response = restTemplate.exchange(baseURL + "/remove/" + item.getId(), HttpMethod.POST, authRequest, String.class);
//            assertEquals(HttpStatus.OK, response.getStatusCode());
//        }
//
//        @Test
//        @DisplayName("Test that a user can get their shopping list")
//        public void testGetShoppingList() {
//            Item item = new Item("Test item", Category.OTHER,
//}
