package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class ProfileController {  

    @Autowired
    private ProfileRepository profileRepository;  // Correct repository

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerProfile(@Valid @RequestBody Profile profile) {  // Correct entity

        // Check if email already exists
        if (profileRepository.findByEmail(profile.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists!");
        }

        // Hash the password before saving
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));

        // Save the profile to the database
        profileRepository.save(profile);

        return ResponseEntity.ok("Profile registered successfully!");
    }
}
