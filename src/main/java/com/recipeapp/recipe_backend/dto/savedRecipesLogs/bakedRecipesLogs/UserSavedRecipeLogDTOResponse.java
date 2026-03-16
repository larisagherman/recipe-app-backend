package com.recipeapp.recipe_backend.dto.savedRecipesLogs.bakedRecipesLogs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSavedRecipeLogDTOResponse {
    private Long id;
    private Long userId;
    private Long recipeId;
    private String notes;

}

