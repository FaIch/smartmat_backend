package edu.ntnu.idatt2106.backend.service;


import edu.ntnu.idatt2106.backend.model.fridge.Fridge;
import edu.ntnu.idatt2106.backend.model.user.*;
import edu.ntnu.idatt2106.backend.repository.SubUserRepository;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.time.Instant;
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
        fridge.setUser(user);
        user.setFridge(fridge);
        subUser.setMainUser(userRepository.save(user));
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

        fridge.setUser(user);
        user.setFridge(fridge);
        User createdUser = userRepository.save(user);
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
    public ResponseEntity<Map<String, Object>> loginAndGetToken(String email, String password) {
        // Attempts to log in the user and generates a JWT token if successful
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User with given email does not exist");
            response.put("userRequest", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (tryLogin(email, password)) {
            User user = optionalUser.get();
            String token = jwtService.generateJWT(user);
            UserRequest userRequest = new UserRequest(optionalUser.get().getEmail(), token);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userRequest", userRequest);
            return ResponseEntity.ok(response);
        }
        // Returns a response indicating that the login was unsuccessful
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Password is incorrect");
        response.put("userRequest", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Generates a JWT token for the given user ID.
     *
     * @param userId the ID of the user for whom to generate the token.
     * @return a String representing the JWT token.
     */
    public String generateToken(final String userId) {
        final Instant now = Instant.now();
        final Algorithm hmac512 = Algorithm.HMAC512(keyStr);
        final JWTVerifier verifier = JWT.require(hmac512).build();
        return JWT
                .create()
                .withSubject(userId)
                .withIssuer("idatt2106_SmartFood_app")
                .withIssuedAt(now)
                .withExpiresAt(now.plusMillis(JWT_TOKEN_VALIDITY.toMillis()))
                .sign(hmac512);
    }

    /**
     * Edits the password for the specified user.
     *
     * @param email       The email of the user to be edited.
     * @param oldPassword The user's current password.
     * @param newPassword The user's new password.
     * @return A ResponseEntity containing a success or error message.
     */
    public ResponseEntity<String> editPassword(String email, String oldPassword, String newPassword) {
        if (tryLogin(email, oldPassword)) {
            Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                byte[] salt = user.getSalt();
                byte[] hashedPassword = hashPassword(newPassword, salt);
                user.setPassword(hashedPassword);
                userRepository.save(user);
                return ResponseEntity.ok("Password changed");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect password");
    }

    /**
     * Edits the phone number for the specified user.
     *
     * @param email       The email of the user to be edited.
     * @param phoneNumber The new phone number for the user.
     * @return A ResponseEntity containing a success or error message.
     */
    public ResponseEntity<String> editPhoneNumber(String email, String phoneNumber) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPhoneNumber(Long.parseLong(phoneNumber));
            userRepository.save(user);
            return ResponseEntity.ok("Phone number changed");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with given email does not exist");
    }

    /**
     * Edits the address for the specified user.
     *
     * @param email   The email of the user to be edited.
     * @param address The new address for the user.
     * @return A ResponseEntity containing a success or error message.
     */
    public ResponseEntity<String> editAddress(String email, String address) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setAddress(address);
            userRepository.save(user);
            return ResponseEntity.ok("Address changed");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with given email does not exist");
    }

    /**
     * Adds a new subuser to the specified user account with the given nickname and role.
     *
     * @param subUserRequest The request object containing the email of the main user, the nickname,
     *                       and the role of the subuser.
     * @return A ResponseEntity containing a success message if the subuser was added successfully,
     * or an error message if the user does not exist or a subuser with the same nickname already exists.
     */
    public ResponseEntity<String> createSubUser(String userEmail, SubUserRequest subUserRequest) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(userEmail);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with given email does not exist");
        }
        User user = optionalUser.get();
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
     * Changes the nickname of the specified subuser to the given name.
     *
     * @param subUserRequest The request object containing the email of the main user,
     *                       the current nickname of the subuser, and the new nickname of the subuser.
     * @return A ResponseEntity containing a success message if the nickname was changed successfully,
     * or an error message if the user does not exist or the subuser does not exist.
     */
    public ResponseEntity<String> editSubUserName(String userEmail, SubUserRequest subUserRequest) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(userEmail);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with given email does not exist");
        }
        User user = optionalUser.get();
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
     * Deletes the specified subuser from the database.
     *
     * @param subUserRequest The request object containing the email of the main user and the nickname of
     *                       the subuser to be deleted.
     * @return A ResponseEntity containing a success message if the subuser was deleted successfully,
     * or an error message if the user or subuser does not exist.
     */
    public ResponseEntity<String> deleteSubUser(String userEmail, SubUserRequest subUserRequest) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(userEmail);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with given email does not exist");
        }
        User user = optionalUser.get();
        for (SubUser subUser : subUserRepository.findSubUserByMainUser(user)) {
            if (subUser.getNickname().equals(subUserRequest.getNickname())) {
                subUserRepository.deleteById(subUser.getId());
                return ResponseEntity.ok("Sub User deleted");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sub User with given id does not exist");
    }

    /**
     * Gets the list of subusers for the specified user.
     * @param email The email of the user to get the subusers for.
     * @return A ResponseEntity containing the list of subusers if the user exists, or null if the user does not exist.
     */
    public ResponseEntity<List<SubUser>> getSubUsers(String email) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<SubUser> optionalSubUserList = subUserRepository.findSubUserByMainUser(optionalUser.get());
        if (optionalSubUserList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(optionalSubUserList);
    }
}
