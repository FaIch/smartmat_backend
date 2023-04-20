package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.CustomItem;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.CustomItemRepository;
import edu.ntnu.idatt2106.backend.repository.ItemRepository;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;


import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;



@Service
public class ItemService {



    private final ItemRepository itemRepository;
    private final CustomItemRepository customItemRepository;

    private final UserRepository userRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, CustomItemRepository customItemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.customItemRepository = customItemRepository;
        this.userRepository = userRepository;
    }

    /**
     * Method getAllItems
     * @return list of all items in database
     */

    public ResponseEntity<List<Item>> getAllItems() {

        return ResponseEntity.status(HttpStatus.OK).body(itemRepository.findAll());
    }


    /**
     * Method getItemByCategory
     * @param category string of specific category which we want to get items of
     * @return list of items with the specific category
     */

    public  ResponseEntity<List<Item>> getItemByCategory(String category) {
        return ResponseEntity.status(HttpStatus.OK).body(itemRepository.findByCategory(category));
    }
    /**
     * Method saveOrUpdate
     *
     * @param customItem entity of the item which we want to save or update
     * @return
     */

    public ResponseEntity<String> saveOrUpdateCustomItemWithDate(CustomItem customItem, LocalDate date) {

        // Checks if the user already exists in the repository
        Optional<User> existingUser = userRepository.findByEmailIgnoreCase(customItem.getUser().getEmail());
        if (existingUser.isPresent()) {
            LocalDate presentDate = LocalDate.now();
            long daysUntilBad = presentDate.until(date, ChronoUnit.DAYS);
            customItem.setBad_in_days(daysUntilBad);
            customItemRepository.save(customItem);
            return ResponseEntity.ok("Custom item added");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with given email does not exist");

    }

    public ResponseEntity<String> saveOrUpdateCustomItemWithoutDate(CustomItem customItem) {
        Optional<User> existingUser = userRepository.findByEmailIgnoreCase(customItem.getUser().getEmail());
        if (existingUser.isPresent()) {

            try {
                System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
                Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();

                String myUrl = "jdbc:mysql://localhost:3306/mydatabase?createDatabaseIfNotExist=true&serverTimezone=UTC&sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false";
                Connection connection = DriverManager.getConnection(myUrl, "root", "mypassword");

                Class.forName("com.mysql.jdbc.Driver");

                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT bad_in_days FROM date WHERE category='" + customItem.getCategory() +"'");

                while (resultSet.next()) {
                    int badInDays = resultSet.getInt("bad_in_days");
                    customItem.setBad_in_days(badInDays);
                }
                resultSet.close();
                statement.close();
                connection.close();

                customItemRepository.save(customItem);
                return ResponseEntity.ok("Custom item added");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with given email does not exist");

    }

    public ResponseEntity<List<CustomItem>> getCustomItemByUser(User user) {

        return ResponseEntity.status(HttpStatus.OK).body(customItemRepository.findAllByUser(user));
    }
}
