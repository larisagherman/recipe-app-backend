package com.recipeapp.recipe_backend.dto.bakedRecipesLogs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRecipeLogDTOUpdateRequest {
    private String rating;
    private String notes;

}

