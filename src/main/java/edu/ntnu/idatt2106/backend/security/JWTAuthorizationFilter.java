package edu.ntnu.idatt2106.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import edu.ntnu.idatt2106.backend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A filter that intercepts incoming HTTP requests and extracts JWT tokens from the
 * Authorization header. It then validates the token, sets the authentication object in the
 * SecurityContext and forwards the request to the next filter in the chain.
 */
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    public static final String USER = "USER";
    public static final String ROLE_USER = "ROLE_" + USER;

    /**
     * Filters the incoming HTTP requests. If a JWT token is found in the Authorization header,
     * it is validated and the authentication object is set in the SecurityContext.
     *
     * @param request the incoming HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if an error occurs while processing the request
     * @throws IOException if an error occurs while processing the request
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        final String username = validateTokenAndGetUserId(token);
        if (username == null) {
            // validation failed or token expired
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    /**
     * Validates a JWT token using the HMAC512 algorithm and retrieves the user ID from the token.
     *
     * @param token the JWT token to validate
     * @return the user ID retrieved from the token, or "Invalid" if the token is invalid
     */
    public String validateTokenAndGetUserId(final String token) {
        try {
            final Algorithm hmac512 = Algorithm.HMAC512(UserService.keyStr);
            final JWTVerifier verifier = JWT.require(hmac512).build();
            return verifier.verify(token).getSubject();
        } catch (final JWTVerificationException verificationEx) {
            System.out.println("Token is invalid");
            return "Invalid";
        }
    }
}