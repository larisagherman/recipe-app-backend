package com.recipeapp.recipe_backend.dto;

import com.recipeapp.recipe_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserPreferencesResponseDto {
    private Long id;
    private Long userId;
    private List<String> dietaryType;
    private List<String> allergies;
    private List<String> dislikedIngredients;
    private List<String> tastePreferences;
    private List<String> flavourPreferences;
}

