package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.item.CustomItem;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.repository.CustomItemRepository;
import edu.ntnu.idatt2106.backend.repository.ItemRepository;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.sql.*;


import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;



@Service
public class ItemService {



    private ItemRepository itemRepository;
    private CustomItemRepository customItemRepository;

    private UserRepository userRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, CustomItemRepository customItemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.customItemRepository = customItemRepository;
        this.userRepository = userRepository;
    }

    //Todo: fix methods when connected backend to database
    /**
     * Method getAllItems
     * @return list of all items in database
     */

    public List<Item> getAllItems() {
        return (List<Item>) itemRepository.findAll();
    }

    /**
     * Method getItems
     * @param id long of item
     * @return item entity which has specific id
     */

    public Item getItem(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

   /* *//**
     * Method getItemByUser
     * @param user entity of specific user which we want to get items of
     * @return list of items of specific user
     *//*

    public List<ItemEntity> getItemByUser(User user) {
        return itemRepository.findAllByUser(user);
    }*/

    /**
     * Method getItemByCategory
     * @param category string of specific category which we want to get items of
     * @return list of items with the specific category
     */

    public List<Item> getItemByCategory(String category) {
        return itemRepository.findByCategory(category);
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
