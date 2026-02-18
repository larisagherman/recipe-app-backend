package com.recipeapp.recipe_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRecipeLogDTOUpdateRequest {
    private String rating;
    private String notes;

}

