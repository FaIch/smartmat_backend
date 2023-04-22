package edu.ntnu.idatt2106.backend.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import edu.ntnu.idatt2106.backend.service.JWTService;
import edu.ntnu.idatt2106.backend.repository.UserRepository;
import edu.ntnu.idatt2106.backend.model.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * JWT request filter class,
 * creates a tolkien for logged-in user
 */
@Component
public class JWTRequestFilter extends OncePerRequestFilter {

  private final JWTService jwtService;
  private final UserRepository userRepository;

  /**
   * Constructor JWTRequestFilter
   * @param jwtService sets new jwt service from jwtService param
   * @param userRepository sets new user repository from userRepository param
   */
  public JWTRequestFilter(JWTService jwtService, UserRepository userRepository) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
  }


  /**
   * Method doFilterInternal
   * @param request a http request
   * @param response the http requests response
   * @param filterChain filterChain used for filtering jwt
   * @throws ServletException gets thrown if the server request or response fails
   * @throws IOException gets thrown if the user is not found
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    // Extract JWT token from HttpOnly cookie
    String jwtToken = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("JWT")) {
          jwtToken = cookie.getValue();
          break;
        }
      }
    }
    if (jwtToken != null) {
      try{
        String username = jwtService.getEmail(jwtToken);
        Optional<User> optionalUserEntity = userRepository.findByEmailIgnoreCase(username);

        if(optionalUserEntity.isPresent()){
          User user = optionalUserEntity.get();
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, new ArrayList());
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

      }catch (JWTDecodeException ignored){
      }

    }

    filterChain.doFilter(request, response);
  }
}
