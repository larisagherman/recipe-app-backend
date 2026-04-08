package com.recipeapp.recipe_backend.mapper;

import com.recipeapp.recipe_backend.dto.GeneratedRecipeDTO;
import com.recipeapp.recipe_backend.entity.GeneratedRecipe;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GeneratedRecipeMapper {
    public GeneratedRecipe dtoToGeneratedRecipe(GeneratedRecipeDTO generatedRecipeDTO);
    public GeneratedRecipeDTO generatedRecipeToDto(GeneratedRecipe generatedRecipe);
}
