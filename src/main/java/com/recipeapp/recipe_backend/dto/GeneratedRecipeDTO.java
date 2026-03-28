package com.recipeapp.recipe_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeneratedRecipeDTO {
    Long id;
    String name;
    String imgSrc;
    String prepTime;
    String cookTime;
    String servings;
    String ingredients;
    String directions;
}
