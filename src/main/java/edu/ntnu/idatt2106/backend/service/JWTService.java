package edu.ntnu.idatt2106.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import edu.ntnu.idatt2106.backend.model.user.User;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * JWTService class,
 * contains methods for encryption
 */
@Service
public class JWTService {

  @Value("${jwt.algorithm.key}")
  private String algorithmKey;
  @Value("${jwt.issuer}")
  private String issuer;
  @Value("${jwt.accessToken.expiryInSeconds}")
  private int accessTokenExpiryInSeconds;
  @Value("${jwt.refreshToken.expiryInSeconds}")
  private int refreshTokenExpiryInSeconds;
  private Algorithm algorithm;
  private static final String USERNAME_KEY = "EMAIL";
  @PostConstruct
  public void postConstruct(){
    algorithm = Algorithm.HMAC256(algorithmKey);
  }

  /**
   * Method generateJWT
   * @param user entity of user which will get session token jwt
   * @return jwt string
   */
  public String generateAccessToken(User user){
    return JWT.create()
            .withClaim(USERNAME_KEY, user.getEmail())
            .withClaim("token_type", "access")
            .withExpiresAt(new Date(System.currentTimeMillis() + (1000L * accessTokenExpiryInSeconds)))
            .withIssuer(issuer)
            .sign(algorithm);
  }

  public String generateRefreshToken(User user) {
    return JWT.create()
            .withClaim(USERNAME_KEY, user.getEmail())
            .withClaim("token_type", "refresh")
            .withExpiresAt(new Date(System.currentTimeMillis() + (1000L * refreshTokenExpiryInSeconds)))
            .withIssuer(issuer)
            .sign(algorithm);
  }

  /**
   * Method generateJWTCookie
   * @param JWT String token
   * @return Cookie containing JWT token
   */
  public Cookie generateJWTAccessCookie(String JWT) {
    Cookie jwtAccessCookie = new Cookie("JWTAccessToken", JWT);
    jwtAccessCookie.setHttpOnly(true);
    jwtAccessCookie.setPath("/");
    jwtAccessCookie.setMaxAge(accessTokenExpiryInSeconds);
    return jwtAccessCookie;
  }

  public Cookie generateJWTRefreshCookie(String JWT) {
    Cookie jwtRefreshCookie = new Cookie("JWTRefreshToken", JWT);
    jwtRefreshCookie.setHttpOnly(true);
    jwtRefreshCookie.setPath("/");
    jwtRefreshCookie.setMaxAge(refreshTokenExpiryInSeconds);

    return jwtRefreshCookie;
  }

  public boolean isTokenValid(String token) {
    try {
      // Verify the token's signature and check if it has expired
      JWT.require(algorithm)
              .withIssuer(issuer)
              .build()
              .verify(token);
      return true;
    } catch (JWTVerificationException e) {
      return false;
    }
  }
  /**
   * Method getUserName
   * @param token string token of user
   * @return username of user which has token
   */
  public String getEmail(String token){
    return JWT.decode(token).getClaim(USERNAME_KEY).asString();
  }
}
