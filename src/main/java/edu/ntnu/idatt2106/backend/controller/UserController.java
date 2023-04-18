package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.user.SubUserRequest;
import edu.ntnu.idatt2106.backend.model.user.UserRequest;
import edu.ntnu.idatt2106.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param userRequest email, phone number, address, role and password for the user being saved
     * @return the saved user
     */
    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        return service.createUser(userRequest);
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

    /**
     * Edits the phone number for the specified user.
     * @param email The email of the user to be edited.
     * @param phoneNumber The new phone number for the user.
     * @return A ResponseEntity containing a success or error message.
     */
    @PostMapping("/user/editPhoneNumber")
    public ResponseEntity<String> editPhoneNumber(@RequestParam String email, @RequestParam String phoneNumber){
        return service.editPhoneNumber(email, phoneNumber);
    }

    /**
     * Edits the address for the specified user.
     * @param email The email of the user to be edited.
     * @param address The new address for the user.
     * @return A ResponseEntity containing a success or error message.
     */
    @PostMapping("/user/editAddress")
    public ResponseEntity<String> editAddress(@RequestParam String email, @RequestParam String address){
        return service.editAddress(email, address);
    }

    /**
     * Edits the password for the specified user.
     * @param email The email of the user to be edited.
     * @param oldPassword The user's current password.
     * @param newPassword The user's new password.
     * @return A ResponseEntity containing a success or error message.
     */
    @PostMapping("/user/editPassword")
    public ResponseEntity<String> editPassword(@RequestParam String email, @RequestParam String oldPassword,
                                               @RequestParam String newPassword){
        return service.editPassword(email, oldPassword, newPassword);
    }

    /**
     * Creates a sub user with the provided user request and admin email.
     *
     * @param subUserRequest the user request containing the user's details
     * @return the newly created sub user
     * @throws IllegalArgumentException if the provided user details are incomplete or null
     */
    @PostMapping("/user/subUser")
    public ResponseEntity<String> createSubUser(@RequestBody SubUserRequest subUserRequest) {
        return service.createSubUser(subUserRequest);
    }

    @PostMapping("/user/subUser/edit")
    public ResponseEntity<String> editSubUserName(@RequestBody SubUserRequest subUserRequest) {
        return service.editSubUserName(subUserRequest);
    }

    @DeleteMapping("/user/subUser/delete")
    public ResponseEntity<String> deleteSubUser(@RequestBody SubUserRequest subUserRequest) {
        return service.deleteSubUser(subUserRequest);
    }
}
