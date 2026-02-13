package com.recipeapp.recipe_backend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, String> loginWithFirebaseToken(String token) throws FirebaseAuthException {
        // 1️⃣ Verify Firebase token
        FirebaseToken decoded = FirebaseAuth.getInstance().verifyIdToken(token);

        String uid = decoded.getUid();
        String email = decoded.getEmail();
        String name = decoded.getName();

        // 2️⃣ Save user in Supabase
        saveUserToSupabase(uid, name, email);

        // 3️⃣ Return user info
        Map<String, String> response = new HashMap<>();
        response.put("uid", uid);
        response.put("email", email);
        response.put("name", name);

        return response;
    }

    private void saveUserToSupabase(String uid, String name, String email) {
        String url = "https://<your-supabase-project>.supabase.co/rest/v1/users";

        Map<String, String> user = new HashMap<>();
        user.put("id", uid);
        user.put("name", name);
        user.put("email", email);

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", "<your-supabase-key>");
        headers.set("Authorization", "Bearer <your-supabase-key>");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(user, headers);

        restTemplate.postForEntity(url, request, String.class);
    }
}
