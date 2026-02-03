package com.recipeapp.recipe_backend.entity;

public record RecommendationRequest(
        String query,
        int topK
) {}
