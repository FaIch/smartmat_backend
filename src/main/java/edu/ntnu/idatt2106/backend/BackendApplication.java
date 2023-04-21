package edu.ntnu.idatt2106.backend;

import edu.ntnu.idatt2106.backend.model.item.Category;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Base64;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);

        BackendApplication backendApplication = new BackendApplication();
        backendApplication.addCategoryAndDatDB();
        backendApplication.addToItemDB();
        backendApplication.addRecipeToDB();

    }


    private void addToItemDB() {
        try {
            System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();

            String myUrl = "jdbc:mysql://localhost:3306/mydatabase?createDatabaseIfNotExist=true&serverTimezone=UTC&sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false";
            Connection conn = DriverManager.getConnection(myUrl, "root", "mypassword");

            String sql = " insert into item (category, name, price, short_desc, weight )"
                    + " values (?, ?, ?, ?, ?)";

            PreparedStatement preparedStmtMeat = conn.prepareStatement(sql);
            preparedStmtMeat.setString(1, Category.MEAT.toString());
            preparedStmtMeat.setString(2, "Steak");
            preparedStmtMeat.setDouble(3, 129);
            preparedStmtMeat.setString(4, "Finest steak from nmdkd");
            preparedStmtMeat.setDouble(5,250);

            preparedStmtMeat.execute();

            PreparedStatement preparedStmtPotatoes = conn.prepareStatement(sql);
            preparedStmtPotatoes.setString(1, Category.VEGETABLES.toString());
            preparedStmtPotatoes.setString(2, "Mashed potatoes");
            preparedStmtPotatoes.setDouble(3, 70);
            preparedStmtPotatoes.setString(4, "Mashed potatoes from Stryn");
            preparedStmtPotatoes.setDouble(5,450);

            preparedStmtPotatoes.execute();

            PreparedStatement preparedStmtPasta = conn.prepareStatement(sql);
            preparedStmtPasta.setString(1, Category.DRYGOODS.toString());
            preparedStmtPasta.setString(2, "Pasta");
            preparedStmtPasta.setDouble(3, 70);
            preparedStmtPasta.setString(4, "Full grain pasta");
            preparedStmtPasta.setDouble(5,450);

            preparedStmtPasta.execute();

            PreparedStatement preparedStmtSalmon = conn.prepareStatement(sql);
            preparedStmtSalmon.setString(1, Category.FISH.toString());
            preparedStmtSalmon.setString(2, "Salmon");
            preparedStmtSalmon.setDouble(3, 200);
            preparedStmtSalmon.setString(4, "Wild Salmon from nort of norway");
            preparedStmtSalmon.setDouble(5,450);

            preparedStmtSalmon.execute();

            System.out.println("fridge item added successfully to database...");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCategoryAndDatDB() {

        try {
            System.setProperty("jdbc.drivers","com.mysql.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();

            String myUrl = "jdbc:mysql://localhost:3306/mydatabase?createDatabaseIfNotExist=true&serverTimezone=UTC&sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false";
            Connection conn = DriverManager.getConnection(myUrl, "root", "mypassword");

            String sql = " insert into date (category, bad_in_days )"
                    + " values (?, ?)";

            PreparedStatement preparedStmtMeat = conn.prepareStatement(sql);
            preparedStmtMeat.setString(1, Category.MEAT.toString());
            preparedStmtMeat.setInt(2, 10);

            preparedStmtMeat.execute();

            PreparedStatement preparedStmtChicken = conn.prepareStatement(sql);
            preparedStmtChicken.setString(1, Category.VEGETABLES.toString());
            preparedStmtChicken.setInt(2, 5);

            preparedStmtChicken.execute();



            conn.close();

            System.out.println("Item date category added successfully to database....");

        }catch (Exception e) {
            System.err.println("Got an exception!");
            e.printStackTrace();
            System.out.println(e);
        }
    }

    private void addRecipeToDB() {

        try {
            System.setProperty("jdbc.drivers","com.mysql.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();

            String myUrl = "jdbc:mysql://localhost:3306/mydatabase?createDatabaseIfNotExist=true&serverTimezone=UTC&sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false";
            Connection conn = DriverManager.getConnection(myUrl, "root", "mypassword");

            String sql = " insert into recipe (name, description )"
                    + " values (?, ?)";

            PreparedStatement preparedStmtRecipeSteakPotatoes = conn.prepareStatement(sql);
            preparedStmtRecipeSteakPotatoes.setString(1, "Steak and Potatoes");
            preparedStmtRecipeSteakPotatoes.setString(2, "What to do");

            preparedStmtRecipeSteakPotatoes.execute();


            PreparedStatement preparedStmtRecipePastaSalmon = conn.prepareStatement(sql);
            preparedStmtRecipePastaSalmon.setString(1, "Pasta and Salmon");
            preparedStmtRecipePastaSalmon.setString(2, "What to do");

            preparedStmtRecipePastaSalmon.execute();


            String sqlRecipe_Item = " insert into recipe_item (item_id, recipe_id, quantity )"
                    + " values (?, ?, ?)";

            PreparedStatement preparedStmtRecipe1Steak = conn.prepareStatement(sqlRecipe_Item);
            preparedStmtRecipe1Steak.setLong(1, 1);
            preparedStmtRecipe1Steak.setLong(2, 1);
            preparedStmtRecipe1Steak.setDouble(3,4);

            preparedStmtRecipe1Steak.execute();

            PreparedStatement preparedStmtRecipe1Potatoes = conn.prepareStatement(sqlRecipe_Item);
            preparedStmtRecipe1Potatoes.setLong(1, 2);
            preparedStmtRecipe1Potatoes.setLong(2, 1);
            preparedStmtRecipe1Potatoes.setDouble(3,6);

            preparedStmtRecipe1Potatoes.execute();

            PreparedStatement preparedStmtRecipe1Pasta = conn.prepareStatement(sqlRecipe_Item);
            preparedStmtRecipe1Pasta.setLong(1, 3);
            preparedStmtRecipe1Pasta.setLong(2, 2);
            preparedStmtRecipe1Pasta.setDouble(3,10);

            preparedStmtRecipe1Pasta.execute();

            PreparedStatement preparedStmtRecipe2Salmon = conn.prepareStatement(sqlRecipe_Item);
            preparedStmtRecipe2Salmon.setLong(1, 4);
            preparedStmtRecipe2Salmon.setLong(2, 2);
            preparedStmtRecipe2Salmon.setDouble(3,4);

            preparedStmtRecipe2Salmon.execute();

            conn.close();

            System.out.println("Recipe added successfully to database....");

        }catch (Exception e) {
            System.err.println("Got an exception!");
            e.printStackTrace();
            System.out.println(e);
        }

    }

}
