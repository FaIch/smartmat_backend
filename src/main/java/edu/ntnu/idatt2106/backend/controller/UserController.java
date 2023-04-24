package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.user.SubUser;
import edu.ntnu.idatt2106.backend.model.user.SubUserRequest;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.model.user.UserRequest;
import edu.ntnu.idatt2106.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class UserController {
    /**
     * The service class for users
     */
    private final UserService userService;

    /**
     * Autowired controller for instantiate the service class
     * @param userService the service class for users
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Post endpoint for saving a user
     * @param userRequest email, phone number, address, role and password for the user being saved
     * @return the saved user
     */
    @PostMapping("/user-without-child")
    public ResponseEntity<String> createUserWithoutChild(@RequestBody UserRequest userRequest) {
        return userService.createUserWithoutChild(userRequest);
    }

    @PostMapping("/user-with-child")
    public ResponseEntity<String> createUserWithChild(@RequestBody UserRequest userRequest) {
        return userService.createUserWithChild(userRequest);
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
            @RequestBody UserRequest user,
            HttpServletResponse response) {
        return userService.loginAndGetToken(user.getEmail(), user.getPassword(), response);
    }

    @PostMapping("/auth/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request,
                                          HttpServletResponse response) {
        return userService.refreshToken(request, response);
    }

    /**
     * Edits the phone number for the specified user.
     * @param phoneNumber The new phone number for the user.
     * @return A ResponseEntity containing a success or error message.
     */
    @PutMapping("/user/editPhoneNumber")
    public ResponseEntity<String> editPhoneNumber(@RequestParam String phoneNumber, @AuthenticationPrincipal User user){
        return userService.editPhoneNumber(user.getEmail(), phoneNumber);
    }

    /**
     * Edits the address for the specified user.
     * @param address The new address for the user.
     * @return A ResponseEntity containing a success or error message.
     */
    @PutMapping("/user/editAddress")
    public ResponseEntity<String> editAddress(@RequestParam String address, @AuthenticationPrincipal User user){
        return userService.editAddress(user.getEmail(), address);
    }

    /**
     * Edits the password for the specified user.
     * @param oldPassword The user's current password.
     * @param newPassword The user's new password.
     * @return A ResponseEntity containing a success or error message.
     */
    @PutMapping("/user/editPassword")
    public ResponseEntity<String> editPassword(@RequestParam String oldPassword,
                                               @RequestParam String newPassword, @AuthenticationPrincipal User user){
        return userService.editPassword(user.getEmail(), oldPassword, newPassword);
    }

    /**
     * Creates a sub user with the provided user request and admin email.
     *
     * @param subUserRequest the user request containing the user's details
     * @return the newly created sub user
     * @throws IllegalArgumentException if the provided user details are incomplete or null
     */
    @PostMapping("/user/subUser")
    public ResponseEntity<String> createSubUser(@RequestBody SubUserRequest subUserRequest
            , @AuthenticationPrincipal User user) {
        return userService.createSubUser(user.getEmail(), subUserRequest);
    }

    /**
     * Edits the name of a sub user.
     * @param subUserRequest the user request containing the user's details
     * @return ResponseEntity containing a success or error message
     */
    @PutMapping("/user/subUser/edit")
    public ResponseEntity<String> editSubUserName(@RequestBody SubUserRequest subUserRequest
            , @AuthenticationPrincipal User user) {
        return userService.editSubUserName(user.getEmail(), subUserRequest);
    }

    /**
     * Deletes a sub user with the provided user request and admin email.
     * @param subUserRequest the user request containing the user's details
     * @return the deleted sub user
     */
    @DeleteMapping("/user/subUser/delete")
    public ResponseEntity<String> deleteSubUser(@RequestBody SubUserRequest subUserRequest
            , @AuthenticationPrincipal User user) {
        return userService.deleteSubUser(user.getEmail(), subUserRequest);
    }

    /**
     * Get endpoint for getting all sub users for a user
     * @param user the user to get sub users for
     * @return a list of sub users
     */
    @GetMapping("/user/getSubUsers")
    public ResponseEntity<List<SubUser>> getSubUsers(@AuthenticationPrincipal User user) {
        return userService.getSubUsers(user.getEmail());
    }
}
