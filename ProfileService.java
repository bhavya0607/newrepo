package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get All Profiles
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    // Get Profile by ID
    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    // Create Profile (Ensuring unique email and hashing password)
    public Profile createProfile(Profile profile) {
        if (!profile.getPassword().equals(profile.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and Confirm Password must match");
        }
        if (profileRepository.findByEmail(profile.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }
        profile.setPassword(passwordEncoder.encode(profile.getPassword())); // Hash password
        return profileRepository.save(profile);
    }

    // Update Profile (Only update fields, avoid overwriting password)
    public Profile updateProfile(Long id, Profile updatedProfile) {
        return profileRepository.findById(id)
                .map(profile -> {
                    profile.setFirstName(updatedProfile.getFirstName());
                    profile.setLastName(updatedProfile.getLastName());
                    profile.setEmail(updatedProfile.getEmail());
                    profile.setPhoneNumber(updatedProfile.getPhoneNumber());
                    return profileRepository.save(profile);
                }).orElseThrow(() -> new RuntimeException("Profile not found!"));
    }

    // Delete Profile
    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new RuntimeException("Profile not found!");
        }
        profileRepository.deleteById(id);
    }
}