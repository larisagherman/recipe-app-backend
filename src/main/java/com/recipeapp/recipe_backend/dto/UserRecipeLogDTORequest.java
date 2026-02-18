package com.recipeapp.recipe_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRecipeLogDTORequest{
    private Long userId;
    private Long recipeId;
    private String rating;
    private LocalDateTime cookedAt;
    private String notes;

}

