package com.recipeapp.recipe_backend.dto;

public record RecipeDTO(
        Long id,
        String name,
        String imgSrc,
        String prepTime,
        String cookTime,
        String servings,
        String ingredients,
        String directions
) {}
