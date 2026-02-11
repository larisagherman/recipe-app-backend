package com.recipeapp.recipe_backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RecommendationRequest(
        String query,
        @JsonProperty("top_k")
        int topK,
        @JsonProperty("forbidden_ingredients")
        List<String> forbiddenIngredients,
        boolean strict

) {}
