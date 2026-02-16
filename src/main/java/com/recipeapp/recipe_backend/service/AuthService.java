package com.recipeapp.recipe_backend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
        String url = "https://oyrfjahtvrawiecfwrlr.supabase.co/rest/v1/users";

        Map<String, Object> user = new HashMap<>();
        user.put("firebase_id", uid);
        user.put("name", name);
        user.put("email", email);

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im95cmZqYWh0dnJhd2llY2Z3cmxyIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc2MzM4NzIzMywiZXhwIjoyMDc4OTYzMjMzfQ.xYjxw0erDjs8YveHWBX8dJKSgjsk8CqtUa6qL7Wmpjo");
        headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im95cmZqYWh0dnJhd2llY2Z3cmxyIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc2MzM4NzIzMywiZXhwIjoyMDc4OTYzMjMzfQ.xYjxw0erDjs8YveHWBX8dJKSgjsk8CqtUa6qL7Wmpjo");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Prefer", "return=representation");


        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Supabase response: " + response.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("Supabase error: " + e.getResponseBodyAsString());
        }
    }

}
