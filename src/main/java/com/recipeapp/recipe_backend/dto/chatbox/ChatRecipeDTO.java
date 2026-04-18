package com.recipeapp.recipe_backend.dto.chatbox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRecipeDTO {
    String name;
    String imgSrc;
    String prepTime;
    String cookTime;
    String servings;
    String ingredients;
    String directions;
}
