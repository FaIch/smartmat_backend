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

        }catch (Exception e)
        {
            System.err.println("Got an exception!");
            e.printStackTrace();
            System.out.println(e);
        }
    }

}
