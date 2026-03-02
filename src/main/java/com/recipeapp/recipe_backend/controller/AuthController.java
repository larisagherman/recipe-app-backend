package com.recipeapp.recipe_backend.controller;

import com.recipeapp.recipe_backend.service.AuthService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/firebase")
    public ResponseEntity<Map<String, String>> loginWithFirebase(@RequestHeader("Authorization") String authHeader)
            throws FirebaseAuthException {

        // Remove "Bearer " prefix
        System.out.println("Authorization header: " + authHeader);

        String token = authHeader.replace("Bearer ", "");

        // Delegate all business logic to AuthService
        Map<String, String> userInfo = authService.loginWithFirebaseToken(token);

        return ResponseEntity.ok(userInfo);
    }
}

