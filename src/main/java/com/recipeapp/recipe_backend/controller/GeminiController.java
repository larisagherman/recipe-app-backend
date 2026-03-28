package com.recipeapp.recipe_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipeapp.recipe_backend.dto.GeneratedRecipeDTO;
import com.recipeapp.recipe_backend.dto.RecipeDTO;
import com.recipeapp.recipe_backend.entity.RecommendationResponse;
import com.recipeapp.recipe_backend.service.GeminiService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/gemini")
@AllArgsConstructor
public class GeminiController {
    private final GeminiService geminiService;

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<RecommendationResponse>> analyze(
            @Parameter(description = "Image file", required = true)
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "topK", defaultValue = "12") int topK,
            @RequestParam(value = "forbiddenIngredients", required = false) List<String> forbiddenIngredients,
            @RequestParam(value = "strict", defaultValue = "false") boolean strict
    ) throws IOException {
        String base64 = Base64.getEncoder()
                .encodeToString(file.getBytes());

        // Handle null forbiddenIngredients
        if (forbiddenIngredients == null) {
            forbiddenIngredients = List.of();
        }

        List<RecommendationResponse> recommendations = geminiService.detectIngredients(
                base64, topK, forbiddenIngredients, strict
        );
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/weekly-meal-plan/{userId}")
    public ResponseEntity<GeneratedRecipeDTO> generateWeeklyMealPlan(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId
    ) throws JsonProcessingException {
        GeneratedRecipeDTO mealPlan = geminiService.generateWeeklyMealPlanForUser(userId);
        return ResponseEntity.ok(mealPlan);
    }
}
