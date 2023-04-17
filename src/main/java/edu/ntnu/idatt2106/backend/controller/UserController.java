package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.user.UserRequest;
import edu.ntnu.idatt2106.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin("*")
public class UserController {
    /**
     * The service class for users
     */
    private final UserService service;

    /**
     * Autowired controller for instantiate the service class
     * @param service the service class for users
     */
    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Post endpoint for saving a user
     * @param user nickname, email and password for the user being saved
     * @return the saved user
     */
    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody UserRequest user) {
        return service.createUser(user);
    }

    /**
     * Handles HTTP POST requests to the "/login" endpoint for user login.
     * Authenticates the user with the specified email and password,
     * and returns a {@link UserRequest} object containing the user's details
     * and a JWT token if the authentication succeeds.
     *
     * @param user a {@link UserRequest} object containing the email and password of the user to authenticate
     * @return a {@link UserRequest} object containing the user's details and JWT token if the authentication succeeds,
     *         or null if the authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(
            @RequestBody UserRequest user
    ) {
        return service.loginAndGetToken(user.getEmail(), user.getPassword());
    }
}
