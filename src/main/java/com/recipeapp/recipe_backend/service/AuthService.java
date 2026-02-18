package com.recipeapp.recipe_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final RestTemplate restTemplate;
    private final String supabaseApiKey;
    private final String supabaseUrl;

    public AuthService(RestTemplate restTemplate, @Value("${supabase.apikey}") String supabaseApiKey,@Value("${SUPABASE_URL}") String supabaseUrl) {
        this.restTemplate = restTemplate;
        this.supabaseApiKey = supabaseApiKey;
        this.supabaseUrl = supabaseUrl;
    }

    public Map<String, String> loginWithFirebaseToken(String token) throws FirebaseAuthException {
        // 1️⃣ Verify Firebase token
        FirebaseToken decoded = FirebaseAuth.getInstance().verifyIdToken(token);

        String uid = decoded.getUid();
        String email = decoded.getEmail();
        String name = decoded.getName();

        // 2️⃣ Save user in Supabase and get returned record
        Map<String, Object> savedUser = saveUserToSupabase(uid, name, email);

        if (savedUser == null) {
            throw new RuntimeException("Failed to save user to Supabase");
        }

        // 3️⃣ Return user info
        Map<String, String> response = new HashMap<>();
        response.put("id", String.valueOf(savedUser.get("id"))); // <-- backend-generated ID
        response.put("uid", uid);       // optional Firebase UID
        response.put("email", email);
        response.put("name", name);

        return response;
    }


    private Map<String, Object> saveUserToSupabase(String uid, String name, String email) {
        String url = supabaseUrl + "?firebase_id=eq." + uid;

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseApiKey);
        headers.set("Authorization", "Bearer " + supabaseApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> existingUsers = mapper.readValue(response.getBody(), List.class);

            if (!existingUsers.isEmpty()) {
                // User already exists, return it
                return existingUsers.get(0);
            }

            // User does not exist → create new
            Map<String, Object> user = new HashMap<>();
            user.put("firebase_id", uid);
            user.put("name", name);
            user.put("email", email);

            HttpHeaders postHeaders = new HttpHeaders();
            postHeaders.set("apikey", supabaseApiKey);
            postHeaders.set("Authorization", "Bearer " + supabaseApiKey);
            postHeaders.setContentType(MediaType.APPLICATION_JSON);
            postHeaders.set("Prefer", "return=representation"); // get created record

            HttpEntity<Map<String, Object>> postRequest = new HttpEntity<>(user, postHeaders);
            ResponseEntity<String> postResponse = restTemplate.postForEntity(supabaseUrl, postRequest, String.class);

            List<Map<String, Object>> createdUsers = mapper.readValue(postResponse.getBody(), List.class);
            return createdUsers.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
