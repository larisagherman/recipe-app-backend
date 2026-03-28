package com.recipeapp.recipe_backend.dto;

import com.recipeapp.recipe_backend.entity.GeneratedRecipe;
import com.recipeapp.recipe_backend.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
public class WeeklyRecommendationsResponseDTO {

    private Long id;
    private Long userId;
    private Date weekStart;
    private GeneratedRecipeDTO recipe;
}
