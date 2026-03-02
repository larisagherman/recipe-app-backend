package com.recipeapp.recipe_backend.service;

import com.recipeapp.recipe_backend.dto.GeminiResponseDTO;
import com.recipeapp.recipe_backend.entity.RecommendationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeminiService {
    private final WebClient webClient;
    private final String apiKey;
    private final RecommendationService recommendationService;

    public GeminiService(@Value("${GEMINI_API_KEY}") String apiKey, RecommendationService recommendationService) {
        this.apiKey = apiKey;
        this.recommendationService = recommendationService;
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    public List<RecommendationResponse> detectIngredients(String base64Image, int topK, List<String> forbiddenIngredients, boolean strict) {
        String body = """
        {
          "contents": [{
            "parts": [
              {  "text": "List all ingredients visible in this image as a comma-separated list. Only include ingredient names, nothing else—no measurements, no locations, no extra words."},
              {
                "inline_data": {
                  "mime_type": "image/jpeg",
                  "data": "%s"
                }
              }
            ]
          }]
        }
        """.formatted(base64Image);

        GeminiResponseDTO geminiResponse = webClient.post()
                .uri("/v1beta/models/gemini-3-flash-preview:generateContent?key=" + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(GeminiResponseDTO.class)
                .block();

        // Extract the detected ingredients text from Gemini response (this is the query)
        String detectedIngredients = extractIngredientsText(geminiResponse);
        System.out.println(detectedIngredients);

        List<String> parsedIngredients = parseIngredients(detectedIngredients);
        System.out.println(parsedIngredients);

        String query = String.join(" ", parsedIngredients);

        System.out.println(query);
        // Use the detected ingredients as query with parameters from frontend
        return recommendationService.getRecommendations(
                parsedIngredients, // query from Gemini
                topK, // from frontend
                forbiddenIngredients, // from frontend
                strict // from frontend
        );
    }

    private String extractIngredientsText(GeminiResponseDTO response) {
        if (response != null &&
            response.candidates() != null &&
            !response.candidates().isEmpty()) {

            GeminiResponseDTO.Candidate firstCandidate = response.candidates().get(0);
            if (firstCandidate.content() != null &&
                firstCandidate.content().parts() != null &&
                !firstCandidate.content().parts().isEmpty()) {

                return firstCandidate.content().parts().get(0).text();
            }
        }
        return ""; // Return empty string if parsing fails
    }
    private List<String> parseIngredients(String rawText) {

        List<String> ingredients = new ArrayList<>();

        if (rawText == null || rawText.isBlank()) {
            return ingredients;
        }

        String[] lines = rawText.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();

            if (line.isEmpty()) continue;

            // remove bullet points (* - etc)
            line = line.replaceAll("^[\\*\\-•\\d\\.\\s]+", "");

            // remove markdown bold **
            line = line.replace("**", "");

            // remove text in parentheses
            line = line.replaceAll("\\(.*?\\)", "");

            // lowercase + trim
            line = line.trim().toLowerCase();

            if (!line.isEmpty()) {
                ingredients.add(line);
            }
        }

        return ingredients;
    }
}
