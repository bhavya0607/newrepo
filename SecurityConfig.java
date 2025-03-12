package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.JwtRequestFilter;
import com.example.demo.ProfileRepository;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final ProfileRepository profileRepository;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, ProfileRepository profileRepository) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.profileRepository = profileRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF protection for APIs
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**", "/api/auth/register", "/api/auth/login").permitAll()  // Allow public endpoints
                .anyRequest().authenticated()  // Secure all other endpoints
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Use JWT (stateless)
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> profileRepository.findByEmail(email)
            .map(profile -> new org.springframework.security.core.userdetails.User(
                profile.getEmail(),                // Username (email)
                profile.getPassword(),             // Encrypted password
                Collections.emptyList()            // Roles (Authorities) - Change this if needed
            ))
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Secure password encoding
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
