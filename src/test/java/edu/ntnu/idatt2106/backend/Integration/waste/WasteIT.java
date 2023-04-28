package edu.ntnu.idatt2106.backend.Integration.waste;


import edu.ntnu.idatt2106.backend.model.waste.WasteRequest;
import edu.ntnu.idatt2106.backend.repository.SubUserRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import edu.ntnu.idatt2106.backend.model.user.UserRequest;
import edu.ntnu.idatt2106.backend.model.waste.Waste;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
import edu.ntnu.idatt2106.backend.repository.WasteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WasteIT {

    @LocalServerPort
    public int port;

    @Autowired
    public TestRestTemplate restTemplate;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public SubUserRepository subUserRepository;

    @Autowired
    public WasteRepository wasteRepository;

    private String baseURL;

    private HttpHeaders authHeaders;

    private HttpEntity<?> authRequest;


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

        authRequest = new HttpEntity<>(authHeaders);
    }

    @AfterEach
    public void clearDatabase() {
        subUserRepository.deleteAll();
        wasteRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test that waste entry can be added")
    public void testAddWasteEntry() {
        WasteRequest wasteRequest = new WasteRequest(
                5,
                LocalDate.now().toString()
        );
        HttpEntity<WasteRequest> request = new HttpEntity<>(wasteRequest, authHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/waste/add", request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Waste was added successfully", response.getBody());
    }

    @Test
    @DisplayName("Test that total waste by user between dates can be retrieved")
    public void testGetTotalWasteByUserBetweenDates() {
        Waste waste1 = new Waste();
        waste1.setUser(userRepository.findAll().get(0));
        waste1.setWeight(5);
        waste1.setEntryDate(LocalDate.now().minusDays(1));
        wasteRepository.save(waste1);

        Waste waste2 = new Waste();
        waste2.setUser(userRepository.findAll().get(0));
        waste2.setWeight(7);
        waste2.setEntryDate(LocalDate.now().minusDays(3));
        wasteRepository.save(waste2);

        Waste waste3 = new Waste();
        waste3.setUser(userRepository.findAll().get(0));
        waste3.setWeight(3);
        waste3.setEntryDate(LocalDate.now().minusDays(5));
        wasteRepository.save(waste3);

        String startDate = LocalDate.now().minusDays(4).toString();
        String endDate = LocalDate.now().minusDays(1).toString();

        String url = baseURL + "/waste/total/date-range?startDate=" + startDate + "&endDate=" + endDate;

        ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.GET, authRequest, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(12, response.getBody());
    }

}
