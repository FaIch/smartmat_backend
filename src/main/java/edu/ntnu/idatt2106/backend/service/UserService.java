package edu.ntnu.idatt2106.backend.service;


import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.model.user.UserRequest;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    public static final String keyStr =
            "idatt2106systemutvikling2sercretkeyforjwttoken";

    /**
     * The validity duration of JWT tokens.
     */
    private static final Duration JWT_TOKEN_VALIDITY = Duration.ofMinutes(5);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> createUser(UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findById(userRequest.getEmail());

        if (existingUser.isPresent()) {
            String response = "User with given email already exists";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        User user = new User(userRequest.getEmail(), userRequest.getPhoneNumber(), userRequest.getAddress(), userRequest.getRole());
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        user.setSalt(salt);
        byte[] hashedPassword = hashPassword(userRequest.getPassword(), salt);
        user.setPassword(hashedPassword);

        userRepository.save(user);

        return ResponseEntity.ok("User created");
    }

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

    private boolean tryLogin(String email, String password) {
        Optional<User> user = userRepository.findById(email);
        if (user.isEmpty()) {
            return false;
        }
        byte[] hashedPassword = hashPassword(password, user.get().getSalt());
        return Arrays.equals(hashedPassword, user.get().getPassword());
    }

    public ResponseEntity<Map<String, Object>> loginAndGetToken(String email, String password) {
        if (tryLogin(email, password)) {
            Optional<User> optionalUser = userRepository.findById(email);
            if(optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (!optionalUser.get().getEmail().equals(email)) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "User with given email does not exist");
                    response.put("userRequest", null);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                String token = generateToken(email);
                UserRequest userRequest = new UserRequest(optionalUser.get().getEmail(), token);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("userRequest", userRequest);
                return ResponseEntity.ok(response);
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Username or password is incorrect");
        response.put("userRequest", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

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
}
