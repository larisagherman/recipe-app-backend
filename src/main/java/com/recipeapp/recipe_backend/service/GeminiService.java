package com.recipeapp.recipe_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipeapp.recipe_backend.dto.GeneratedRecipeDTO;
import com.recipeapp.recipe_backend.dto.RecipeDTO;
import com.recipeapp.recipe_backend.entity.*;
import com.recipeapp.recipe_backend.repository.UserRecipeLogRepository;

import com.recipeapp.recipe_backend.dto.GeminiResponseDTO;
import com.recipeapp.recipe_backend.repository.UserPreferencesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebClient webClient;
    private final String apiKey;
    private final RecommendationService recommendationService;
    private final UserPreferencesRepository userPreferencesRepository;
    private final UserRecipeLogService userRecipeLogService;

    public GeminiService(@Value("${GEMINI_API_KEY}") String apiKey,
                         RecommendationService recommendationService,
                         UserPreferencesRepository userPreferencesRepository,
                         UserRecipeLogRepository userRecipeLogRepository, UserRecipeLogService userRecipeLogService) {
        this.apiKey = apiKey;
        this.recommendationService = recommendationService;
        this.userPreferencesRepository = userPreferencesRepository;
        this.userRecipeLogService = userRecipeLogService;
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
                .uri("/v1beta/models/gemini-2.5-flash-lite:generateContent?key=" + apiKey)
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


    public GeneratedRecipeDTO generateWeeklyMealPlanForUser(Long userId) throws JsonProcessingException {
        // Get user preferences
        UserPreferences userPreferences = userPreferencesRepository.findByUserId(userId);
        if (userPreferences == null) {
            throw new IllegalArgumentException("User preferences not found for user ID: " + userId);
        }

        // Get the user's baked recipes from the past week
        List<UserRecipeLog> weeklyBakedRecipes = userRecipeLogService.getLastWeekBakedRecipes(userId);

        // Build a prompt for Gemini
        String prompt = buildWeeklyMealPlanPrompt(userPreferences, weeklyBakedRecipes);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );


        String geminiResponse = webClient.post()
                .uri("/v1beta/models/gemini-2.5-flash-lite:generateContent?key=" + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode root = objectMapper.readTree(geminiResponse);
        String recipeJsonString = root
                .path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();

        // 6️⃣ Convert the recipe JSON string into RecipeDTO
        GeneratedRecipeDTO recipe = objectMapper.readValue(recipeJsonString, GeneratedRecipeDTO.class);

        return recipe;
    }

    private String buildWeeklyMealPlanPrompt(UserPreferences userPreferences, List<UserRecipeLog> weeklyBakedRecipes) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are a professional pastry chef specializing in desserts.\n\n");

        prompt.append("IMPORTANT: This application is focused ONLY on desserts.\n");
        prompt.append("You must generate ONLY dessert recipes (no savory meals, no main dishes).\n\n");

        prompt.append("Your task is to generate ONE new dessert recipe for the user, based on their preferences and recent baking history.\n\n");
        prompt.append("IMPORTANT RULES:\n");
        prompt.append("- Generate ONLY dessert recipes.\n");
        prompt.append("- RETURN ONLY RAW JSON.\n");
        prompt.append("- Do NOT include any explanations, comments, markdown, or extra text.\n");
        prompt.append("- The JSON must be valid and parseable.\n\n");

        prompt.append("OUTPUT FORMAT (strictly follow this structure):\n");
        prompt.append("{\n");
        prompt.append("  \"name\": \"string\",\n");
        prompt.append("\"imgSrc\": \"string (URL of a realistic dessert image, must be valid and publicly accessible, not a placeholder or 'image not found')\",\n");        prompt.append("  \"prepTime\": \"string, e.g. '15 minutes'\",\n");
        prompt.append("  \"cookTime\": \"string, e.g. '30 minutes'\",\n");
        prompt.append("  \"servings\": \"string, e.g. '4 servings'\",\n");
        prompt.append("  \"ingredients\": \"string, comma or line-separated list\",\n");
        prompt.append("  \"directions\": \"string, step-by-step instructions\"\n");
        prompt.append("}\n\n");
        // --- USER PREFERENCES ---
        prompt.append("USER PREFERENCES:\n");
        prompt.append("- Dietary Types: ").append(userPreferences.getDietaryTypes() != null ?
                String.join(", ", userPreferences.getDietaryTypes()) : "None").append("\n");
        prompt.append("- Allergies: ").append(userPreferences.getAllergies() != null ?
                String.join(", ", userPreferences.getAllergies()) : "None").append("\n");
        prompt.append("- Disliked Ingredients: ").append(userPreferences.getDislikedIngredients() != null ?
                String.join(", ", userPreferences.getDislikedIngredients()) : "None").append("\n");
        prompt.append("- Taste Preferences: ").append(userPreferences.getTastePreferences() != null ?
                userPreferences.getTastePreferences() : "None").append("\n");
        prompt.append("- Flavor Preferences: ").append(userPreferences.getFlavourPreferences() != null ?
                String.join(", ", userPreferences.getFlavourPreferences()) : "None").append("\n\n");

        // --- RECENT MEALS ---
        prompt.append("RECENTLY BAKED DESSERTS (AVOID SIMILAR ONES):\n");

        if (weeklyBakedRecipes.isEmpty()) {
            prompt.append("- None\n\n");
        } else {
            for (UserRecipeLog log : weeklyBakedRecipes) {
                if (log.getRecipe() != null) {
                    prompt.append("- ")
                            .append(log.getRecipe().getName())
                            .append(" (Rating: ")
                            .append(log.getRating() != null ? log.getRating() : "Not rated")
                            .append(")\n");
                }
            }
            prompt.append("\n");
        }

        // --- TASK ---
        prompt.append("TASK:\n");
        prompt.append("Generate ONE original dessert recipe that:\n");
        prompt.append("- Fully respects dietary restrictions and allergies\n");
        prompt.append("- Is clearly different from recently baked desserts\n");
        prompt.append("- Matches the user's taste and flavor preferences\n");
        prompt.append("- Uses simple, easy-to-find ingredients\n");
        prompt.append("- Is realistic and practical to bake\n\n");
        prompt.append("REMEMBER:\n");
        prompt.append("- Output ONLY JSON matching the format above.\n");
        prompt.append("- No extra text before or after.\n");
        prompt.append("- Do not wrap in code blocks or markdown.\n");
        prompt.append("- The imgSrc must be a valid image URL that actually points to a dessert photo.\n");
        prompt.append("- Do not use placeholder URLs like 'not found', 'example.com', or broken links.\n");
        prompt.append("- Prefer URLs from reliable sources like Unsplash, Pexels, or Pixabay.\n");
        prompt.append("Example of valid imgSrc:https://unsplash.com/photos/plate-of-cookies-YwKgwIiV_F8\n\n");
        return prompt.toString();
    }

    public String askGemini(String prompt) {

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        String response = webClient.post()
                .uri("/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(response);

            return root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response", e);
        }
    }

    String buildChatPrompt(Recipe recipe, String userMessage) {
        return """
        You are a helpful baking assistant.

        The user is currently baking this recipe:

        Name: %s
        Ingredients: %s
        Instructions: %s

        Answer the user's question clearly and concisely.
        If they are confused, guide them step by step.

        User question: %s
        """.formatted(
                recipe.getName(),
                recipe.getIngredients(),
                recipe.getDirections(),
                userMessage
        );
    }
}
