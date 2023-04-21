package edu.ntnu.idatt2106.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

/**
 * WebSecurityConfig class,
 * configurations spring security for http requests authentication
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private JWTRequestFilter jwtRequestFilter;

    /**
     * Constructor WebSecurituConfig
     * @param jwtRequestFilter sets new jwt request filter from param jwtRequestFilter
     */
    public WebSecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Method filterChain
     * @param http security object
     * @return http request
     * @throws Exception if cant get or permit http
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);
        http.authorizeHttpRequests()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/user").permitAll()
                .requestMatchers("/items/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated();


        return http.build();
    }

}
