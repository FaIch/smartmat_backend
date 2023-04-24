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
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * JWT request filter class,
 * creates a tolkien for logged-in user
 */
@Component
public class JWTRequestFilter extends OncePerRequestFilter {

  private final JWTService jwtService;
  private final UserRepository userRepository;
  private static final List<RequestMatcher> PUBLIC_URLS = Arrays.asList(
          new AntPathRequestMatcher("/login"),
          new AntPathRequestMatcher("/user-without-child"),
          new AntPathRequestMatcher("/user-with-child"),
          new AntPathRequestMatcher("/auth/refreshToken"),
          new AntPathRequestMatcher("/swagger-ui/**"),
          new AntPathRequestMatcher("/v3/api-docs/**")
  );
  private final RequestMatcher allowedUrls = new OrRequestMatcher(PUBLIC_URLS);
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

    // Allow access to public endpoints
    if (allowedUrls.matches(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    // Extract JWT token from HttpOnly cookie
    String jwtAccessToken = null;
    boolean invalidToken = false;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("JWTAccessToken")) {
          jwtAccessToken = cookie.getValue();
        }
        if (jwtAccessToken != null) {
          break;
        }
      }
    }

    if (jwtAccessToken != null && jwtService.isTokenValid(jwtAccessToken)) {
      try {
        String username = jwtService.getEmail(jwtAccessToken);
        Optional<User> optionalUserEntity = userRepository.findByEmailIgnoreCase(username);

        if (optionalUserEntity.isPresent()) {
          User user = optionalUserEntity.get();
          UsernamePasswordAuthenticationToken authenticationToken =
                  new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      } catch (JWTDecodeException ignored) {
      }
    } else {
      invalidToken = true;
    }

    if (invalidToken) {
      HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(response);
      wrappedResponse.setStatus(600);
      wrappedResponse.getWriter().write("Invalid JWT token");
      wrappedResponse.getWriter().flush();
      wrappedResponse.getWriter().close();
    } else {
      filterChain.doFilter(request, response);
    }
  }
}
