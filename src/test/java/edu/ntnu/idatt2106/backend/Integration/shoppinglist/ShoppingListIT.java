package edu.ntnu.idatt2106.backend.Integration.shoppinglist;

import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.backend.model.user.UserRequest;
import edu.ntnu.idatt2106.backend.repository.ItemRepository;
import edu.ntnu.idatt2106.backend.repository.ShoppingListItemRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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






    @Test
    @DisplayName("Test getShoppingListItemsByUserId")
    public void testGetShoppingListItemsByUserId() {
        HttpEntity<String> getRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<List> getResponse = restTemplate.exchange(baseURL + "/shopping-list/get",
                HttpMethod.GET, getRequest, List.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    }

    @Test
    @DisplayName("Test addShoppingListItem")
    public void testAddShoppingListItem() {
        int itemId = 1;
        int quantity = 3;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseURL + "/shopping-list/add")
                .queryParam("id", itemId)
                .queryParam("quantity", quantity);

        HttpEntity<String> postRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<String> postResponse = restTemplate.postForEntity(uriBuilder.toUriString(), postRequest, String.class);

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
    }

}
