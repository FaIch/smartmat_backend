package edu.ntnu.idatt2106.backend.Integration.recipe;

import edu.ntnu.idatt2106.backend.model.fridge.Fridge;
import edu.ntnu.idatt2106.backend.model.fridge.FridgeItem;
import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.item.Unit;
import edu.ntnu.idatt2106.backend.model.recipe.Recipe;
import edu.ntnu.idatt2106.backend.model.recipe.RecipeItem;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.model.user.UserRequest;
import edu.ntnu.idatt2106.backend.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeIT {
/*

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
    public RecipeRepository recipeRepository;

    @Autowired
    public RecipeItemRepository recipeItemRepository;

    @Autowired
    public FridgeItemRepository fridgeItemRepository;

    private String baseURL;

    private HttpEntity<?> authRequest;

    private UserRequest userRequest;

    @BeforeEach
    public void setUp() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        baseURL = "http://localhost:" + port;

        userRequest = new UserRequest("testnewuser@test.com", "testPassword");

        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(baseURL + "/user/create", request, String.class);

        ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/user/login", request, String.class);

        String jwtAccessToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTAccessToken="))
                .findFirst().get().substring("JWTAccessToken=".length());

        String jwtRefreshToken = response.getHeaders().get("Set-Cookie").stream()
                .filter(header -> header.startsWith("JWTRefreshToken="))
                .findFirst().get().substring("JWTRefreshToken=".length());

        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.add(HttpHeaders.COOKIE, "JWTAccessToken=" + jwtAccessToken);
        authHeaders.add(HttpHeaders.COOKIE, "JWTRefreshToken=" + jwtRefreshToken);

        authRequest = new HttpEntity<>(authHeaders);

    }

    @AfterEach
    public void clearDatabase() {
        subUserRepository.deleteAll();
        userRepository.deleteAll();
        recipeRepository.deleteAll();
        itemRepository.deleteAll();
        fridgeRepository.deleteAll();
        fridgeItemRepository.deleteAll();
    }

*/
/*    public void setUpUserFridgeRecipe() {
        Recipe recipeWithSalmon = new Recipe();
        recipeWithSalmon.setName("Recipe salmon");
        recipeWithSalmon.setEstimatedTime("30 minutes");
        recipeWithSalmon.setDescription("This is a test recipeWithSalmon.");
        recipeWithSalmon.setNumberOfItems(1);
        recipeWithSalmon = recipeRepository.save(recipeWithSalmon);

        Item salmon = new Item(5L, 1, Category.FISH, "", "salmon", 120, "", Unit.ITEM, 100);
        salmon = itemRepository.save(salmon);

        RecipeItem recipeItem = new RecipeItem(5L, recipeWithSalmon, salmon, 1);
        recipeItem = recipeItemRepository.save(recipeItem);

        List<RecipeItem> salmonRecipe = new ArrayList<>();
        salmonRecipe.add(recipeItem);
        recipeWithSalmon.setRecipeItems(salmonRecipe);
        recipeWithSalmon = recipeRepository.save(recipeWithSalmon);

        Recipe emptyRecipe = new Recipe();
        emptyRecipe.setName("Recipe empty");
        emptyRecipe.setEstimatedTime("30 minutes");
        emptyRecipe.setDescription("This is a test empty recipe.");
        emptyRecipe.setNumberOfItems(0);
        emptyRecipe = recipeRepository.save(emptyRecipe);

        User user = new User("test@hotmail.com");
        user = userRepository.save(user);

        Fridge fridge = new Fridge(5L, user);
        fridgeRepository.save(fridge);
        user.setFridge(fridge);

        FridgeItem fridgeItem = new FridgeItem(1, LocalDate.now(), salmon);
        fridgeItem = fridgeItemRepository.save(fridgeItem);
        fridge.addFridgeItem(fridgeItem);

        fridgeRepository.save(fridge);
    }*//*



    @Test
    @DisplayName("Test that all recipes can be retrieved when user logged in")
    public void testListAllRecipes(){
        ResponseEntity<List<Recipe>> response = restTemplate.exchange(baseURL + "/recipe/list", HttpMethod.GET, authRequest, new ParameterizedTypeReference<List<Recipe>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Recipe> recipes = response.getBody();
        assertNotNull(recipes);
    }


    @Test
    @DisplayName("Test that recipes can be displayed based on item in the recipe")
    public void testGetRecipesByItemName(){
        String itemName = "Steak";
        ResponseEntity<String> response = restTemplate.exchange(baseURL + "/recipe/by-item-name?itemName=" + itemName,
                HttpMethod.GET, authRequest, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //TODO: why
*/
/*    @Test
    @DisplayName("Test that recipes gets sorted in order of the most items in recipe and in fridge")
    public void testSortRecipesByFridge() {
        ResponseEntity<List<Recipe>> response = restTemplate.exchange(baseURL + "/recipe/list/sorted",
                HttpMethod.GET, authRequest, new ParameterizedTypeReference<List<Recipe>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Recipe> recipes = response.getBody();
        System.out.println(recipes);
        assertNotNull(recipes);
    }*//*


*/

}
