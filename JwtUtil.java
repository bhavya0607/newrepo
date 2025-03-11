package com.example.demo;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

    public String extractUsername(String jwt) {

        throw new UnsupportedOperationException("Unimplemented method 'extractUsername'");
    }

    public boolean validateToken(String jwt, UserDetails userDetails) {

        throw new UnsupportedOperationException("Unimplemented method 'validateToken'");
    }

}
