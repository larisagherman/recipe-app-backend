package com.recipeapp.recipe_backend.dto;

import com.recipeapp.recipe_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferencesRequestDto {
    private Long userId;
    private List<String> dietaryTypes;
    private List<String> allergies;
    private List<String> dislikedIngredients;
    private String tastePreferences;
    private List<String> flavourPreferences;
}

