package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.fridge.Fridge;
import edu.ntnu.idatt2106.backend.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.backend.model.user.*;
import edu.ntnu.idatt2106.backend.repository.SubUserRepository;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.util.*;

/**
 * A service class that provides user management functionality, such as user creation, login and JWT token generation.
 */
@Service
public class UserService {

    /**
     * The secret key used to sign the JWT tokens.
     */
    public static final String keyStr = "idatt2106systemutvikling2sercretkeyforjwttoken";

    /**
     * The validity duration of JWT tokens.
     */
    private static final Duration JWT_TOKEN_VALIDITY = Duration.ofMinutes(5);

    /**
     * The repository for managing User entities.
     */
    private final UserRepository userRepository;
    private final SubUserRepository subUserRepository;

    private final JWTService jwtService;

    /**
     * Constructs a new UserService instance.
     *
     * @param userRepository the repository for managing User entities.
     * @param jwtService     jwt service class
     */
    @Autowired
    public UserService(UserRepository userRepository, JWTService jwtService, SubUserRepository subUserRepository) {

        this.userRepository = userRepository;
        this.subUserRepository = subUserRepository;
        this.jwtService = jwtService;
    }

    /**
     * Creates a new user with the given details.
     *
     * @param userRequest the details of the user to be created.
     * @return a ResponseEntity with the status code and response body indicating whether the operation was successful.
     */
    public ResponseEntity<String> createUserWithoutChild(UserRequest userRequest) {
        // Checks if the user already exists in the repository

        Optional<User> existingUser = userRepository.findByEmailIgnoreCase(userRequest.getEmail());
        if (existingUser.isPresent()) {
            String response = "User with given email already exists";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // Generates a salt and hashes the user's password before saving the user to the repository
        User user = new User(userRequest.getEmail());
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        user.setSalt(salt);
        byte[] hashedPassword = hashPassword(userRequest.getPassword(), salt);
        user.setPassword(hashedPassword);

        SubUser subUser = new SubUser("Your User", Role.PARENT, userRequest.getPasscode());
        Fridge fridge = new Fridge();
        ShoppingList shoppingList = new ShoppingList();

        user.setShoppingList(shoppingList);
        user.setFridge(fridge);

        User createdUser = userRepository.save(user);

        subUser.setMainUser(createdUser);
        fridge.setUser(createdUser);
        shoppingList.setUser(createdUser);

        subUserRepository.save(subUser);
        return ResponseEntity.ok("User created");
    }

    public ResponseEntity<String> createUserWithChild(UserRequest userRequest) {
        // Checks if the user already exists in the repository
        Optional<User> existingUser = userRepository.findByEmailIgnoreCase(userRequest.getEmail());
        if (existingUser.isPresent()) {
            String response = "User with given email already exists";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // Generates a salt and hashes the user's password before saving the user to the repository
        User user = new User(userRequest.getEmail());
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        user.setSalt(salt);
        byte[] hashedPassword = hashPassword(userRequest.getPassword(), salt);
        user.setPassword(hashedPassword);

        SubUser parentSubUser = new SubUser("Parent", Role.PARENT, userRequest.getPasscode());
        SubUser childSubUser = new SubUser("Child", Role.CHILD);
        Fridge fridge = new Fridge();
        ShoppingList shoppingList = new ShoppingList();

        user.setShoppingList(shoppingList);
        user.setFridge(fridge);

        User createdUser = userRepository.save(user);

        fridge.setUser(createdUser);
        shoppingList.setUser(createdUser);
        parentSubUser.setMainUser(createdUser);
        childSubUser.setMainUser(createdUser);

        subUserRepository.save(parentSubUser);
        subUserRepository.save(childSubUser);

        return ResponseEntity.ok("User created");
    }

    /**
     * Hashes a password using the PBKDF2WithHmacSHA1 algorithm and the given salt.
     *
     * @param password the password to be hashed.
     * @param salt     the salt to use in the hashing algorithm.
     * @return a byte array representing the hashed password.
     */
    private byte[] hashPassword(String password, byte[] salt) {
        int iterationCount = 65536;
        int keyLength = 128;
        KeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterationCount,
                keyLength
        );
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks if the given email and password match a user in the repository.
     *
     * @param email    the email of the user to check.
     * @param password the password of the user to check.
     * @return true if the email and password match a user in the repository, false otherwise.
     */
    private boolean tryLogin(String email, String password) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if (user.isEmpty()) {
            return false;
        }
        byte[] hashedPassword = hashPassword(password, user.get().getSalt());
        return Arrays.equals(hashedPassword, user.get().getPassword());
    }

    /**
     * Attempts to log in a user with the given email and password, and generates a JWT token if successful.
     *
     * @param email    the email of the user to log in.
     * @param password the password of the user to log in.
     * @return a ResponseEntity with the status code and response body indicating whether the login was successful and the JWT token if applicable.
     */
    public ResponseEntity<Map<String, Object>> loginAndGetToken(String email, String password,
                                                                HttpServletResponse httpServletResponse) {
        // Attempts to log in the user and generates a JWT token if successful
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isEmpty()) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "User with given email does not exist");
            responseBody.put("userRequest", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
        if (tryLogin(email, password)) {
            createTokens(optionalUser.get(), httpServletResponse);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful");
            responseBody.put("userEmail", optionalUser.get().getEmail());
            return ResponseEntity.ok(responseBody);
        }
        // Returns a response indicating that the login was unsuccessful
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Password is incorrect");
        response.put("userRequest", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // Extract the refresh token from the HttpOnly cookie
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWTRefreshToken")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            // Return an error response if the refresh token is not provided
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Refresh token is missing");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }

        // Verify the refresh token's validity
        if (!jwtService.isTokenValid(refreshToken)) {
            // Return an error response if the refresh token is invalid or expired
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Invalid or expired refresh token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }

        // Get the email from the refresh token
        String email = jwtService.getEmail(refreshToken);
        Optional<User> optionalUserEntity = userRepository.findByEmailIgnoreCase(email);

        if (optionalUserEntity.isPresent()) {
            createAccessToken(optionalUserEntity.get(), response);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access token refreshed");
            return ResponseEntity.ok(responseBody);
        } else {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
    }

    private void createTokens(User user, HttpServletResponse httpServletResponse) {
        createAccessToken(user, httpServletResponse);

        String refreshToken = jwtService.generateRefreshToken(user);
        Cookie jwtRefreshCookie = jwtService.generateJWTRefreshCookie(refreshToken);
        addCookieWithSameSiteAttribute(httpServletResponse, jwtRefreshCookie, "Strict", false);
    }

    private void createAccessToken(User user, HttpServletResponse httpServletResponse) {
        String accessToken = jwtService.generateAccessToken(user);
        Cookie jwtAccessToken = jwtService.generateJWTAccessCookie(accessToken);

        addCookieWithSameSiteAttribute(httpServletResponse, jwtAccessToken, "Strict", false);
    }

    public static void addCookieWithSameSiteAttribute(HttpServletResponse response, Cookie cookie, String sameSite,
                                                      boolean secure) {
        String cookieHeader = String.format("%s=%s; SameSite=%s; HttpOnly; Path=/; Max-Age=%d; %s",
                cookie.getName(), cookie.getValue(), sameSite, cookie.getMaxAge(), secure ? "Secure" : "");

        response.addHeader("Set-Cookie", cookieHeader);
    }

    public ResponseEntity<String> editPassword(User user, String oldPassword, String newPassword) {
        if (tryLogin(user.getEmail(), oldPassword)) {
            Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(user.getEmail());
            if (optionalUser.isPresent()) {
                User foundUser = optionalUser.get();
                byte[] salt = foundUser.getSalt();
                byte[] hashedPassword = hashPassword(newPassword, salt);
                foundUser.setPassword(hashedPassword);
                userRepository.save(foundUser);
                return ResponseEntity.ok("Password changed");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect password");
    }

    /**
     * Edits the phone number for the specified user.
     *
     * @param user       The user to be edited.
     * @param phoneNumber The new phone number.
     * @return A ResponseEntity containing a success message.
     */
    public ResponseEntity<String> editPhoneNumber(User user, String phoneNumber) {
        user.setPhoneNumber(Long.parseLong(phoneNumber));
        userRepository.save(user);
        return ResponseEntity.ok("Phone number changed");
    }

    /**
     * Edits the address for the specified user.
     *
     * @param user     The user to be edited.
     * @param address  The new address.
     * @return A ResponseEntity containing a success message.
     */
    public ResponseEntity<String> editAddress(User user, String address) {
        user.setAddress(address);
        userRepository.save(user);
        return ResponseEntity.ok("Address changed");
    }

    /**
     * Adds a new sub user to the specified user account with the given nickname and role.
     *
     * @param subUserRequest The request object containing the email of the main user, the nickname,
     *                       and the role of the sub user.
     * @return A ResponseEntity containing a success message if the sub user was added successfully,
     * or an error message if a sub user with the same nickname already exists.
     */
    public ResponseEntity<String> createSubUser(User user, SubUserRequest subUserRequest) {
        for (SubUser subUser : subUserRepository.findSubUserByMainUser(user)) {
            if (subUser.getNickname().equals(subUserRequest.getNickname())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sub User with given nickname already exists");
            }
        }
        SubUser subUser = new SubUser(subUserRequest.getNickname(), subUserRequest.getRole());
        subUserRepository.save(subUser);
        userRepository.save(user);
        return ResponseEntity.ok("Sub User added");
    }

    /**
     * Edits the nickname of the specified sub user.
     *
     * @param subUserRequest The request object containing the email of the main user, the nickname,
     *                       and the role of the sub user.
     * @return A ResponseEntity containing a success message if the sub user was edited successfully,
     * or an error message if the user or sub user does not exist.
     */
    public ResponseEntity<String> editSubUserName(User user, SubUserRequest subUserRequest) {
        for (SubUser subUser : subUserRepository.findSubUserByMainUser(user)) {
            if (subUser.getNickname().equals(subUserRequest.getNickname())) {
                subUser.setNickname(subUserRequest.getNickname());
                userRepository.save(user);
                subUserRepository.save(subUser);
                return ResponseEntity.ok("Name changed");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sub User with given id does not exist");
    }

    /**
     * Edits the role of the specified sub user.
     *
     * @param subUserRequest The request object containing the email of the main user, the nickname,
     *                       and the role of the sub user.
     * @return A ResponseEntity containing a success message if the sub user was edited successfully,
     * or an error message if the user or sub user does not exist.
     */
    public ResponseEntity<String> deleteSubUser(User user, SubUserRequest subUserRequest) {
        for (SubUser subUser : subUserRepository.findSubUserByMainUser(user)) {
            if (subUser.getNickname().equals(subUserRequest.getNickname())) {
                subUserRepository.deleteById(subUser.getId());
                return ResponseEntity.ok("Sub User deleted");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sub User with given id does not exist");
    }

    /**
     * Gets a list of all sub users for the specified user.
     *
     * @param user The user to get the sub users for.
     * @return A ResponseEntity containing a list of sub users if the user has any, or an empty list
     * if the user does not have any sub users.
     */
    public ResponseEntity<List<SubUser>> getSubUsers(User user) {
        List<SubUser> optionalSubUserList = subUserRepository.findSubUserByMainUser(user);
        if (optionalSubUserList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalSubUserList);
    }
}
