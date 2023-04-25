package edu.ntnu.idatt2106.backend.Integration.shoppinglist;

import edu.ntnu.idatt2106.backend.model.user.UserRequest;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingListIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    public int port;

    private String baseURL;
    private HttpHeaders headers;
    private UserRequest userRequest;

    @BeforeEach
    public void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        baseURL = "http://localhost:" + port;
        userRequest = new UserRequest("testnewuser@test.com", "testPassword");
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user-without-child", request, String.class);
    }

    private HttpHeaders getAuthenticatedHeaders() {
        HttpEntity<UserRequest> loginRequest = new HttpEntity<>(userRequest, headers);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(baseURL + "/login", loginRequest, String.class);

        String jwtAccessToken = loginResponse.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTAccessToken="))
                .findFirst().orElse("").substring("JWTAccessToken=".length());

        String jwtRefreshToken = loginResponse.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTRefreshToken="))
                .findFirst().orElse("").substring("JWTRefreshToken=".length());

        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.add(HttpHeaders.COOKIE, "JWTAccessToken=" + jwtAccessToken);
        authHeaders.add(HttpHeaders.COOKIE, "JWTRefreshToken=" + jwtRefreshToken);

        return authHeaders;
    }

    /*

    @Test
    @DisplayName("Test getShoppingListItemsByUserId")
    public void testGetShoppingListItemsByUserId() {
        HttpHeaders authHeaders = getAuthenticatedHeaders();

        HttpEntity<String> getRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<List> getResponse = restTemplate.exchange(baseURL + "/user/shopping-list-items",
                HttpMethod.GET, getRequest, List.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    }

    @Test
    @DisplayName("Test addShoppingListItem")
    public void testAddShoppingListItem() {
        HttpHeaders authHeaders = getAuthenticatedHeaders();

        int itemId = 1;
        int quantity = 3;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseURL + "/shopping-list/add")
                .queryParam("id", itemId)
                .queryParam("quantity", quantity);

        HttpEntity<String> postRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<String> postResponse = restTemplate.postForEntity(uriBuilder.toUriString(), postRequest, String.class);

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
    }

     */

}
