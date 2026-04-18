package com.recipeapp.recipe_backend.mapper;

import com.recipeapp.recipe_backend.dto.chatbox.ChatRecipeDTO;
import com.recipeapp.recipe_backend.entity.GeneratedRecipe;
import com.recipeapp.recipe_backend.entity.Recipe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatRecipeMapper {
    ChatRecipeDTO recipeToChatRecipeDTO(Recipe recipe);
    ChatRecipeDTO generatedRecipeToChatRecipeDTO(GeneratedRecipe recipe);

}
