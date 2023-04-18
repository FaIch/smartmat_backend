package edu.ntnu.idatt2106.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.model.user.User;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * JWTService class,
 * contains methods for encryption
 */
@Component
@Service
public class JWTService {

  @Value("${jwt.algorithm.key}")
  private String algorithmKey;
  @Value("${jwt.issuer}")
  private String issuer;
  @Value("${jwt.expiryInSeconds}")
  private int expiryInSeconds;
  private Algorithm algorithm;
  private static final String USERNAME_KEY = "EMAIL";
  @PostConstruct
  public void postConstruct(){
    algorithm = Algorithm.HMAC256(algorithmKey);
  }

  /**
   * Method generateJWT
   * @param user entity of user which will get session tolkin jwt
   * @return jwt string
   */
  public String generateJWT(User user){
    return JWT.create()
            .withClaim(USERNAME_KEY, user.getEmail())
            .withExpiresAt(new Date(System.currentTimeMillis() + (1000*expiryInSeconds)))
            .withIssuer(issuer)
            .sign(algorithm);
  }

  /**
   * Method getUserName
   * @param token string tolkin of user
   * @return username of user which has tolkin
   */
  public String getEmail(String token){
    return JWT.decode(token).getClaim(USERNAME_KEY).asString();
  }



}
