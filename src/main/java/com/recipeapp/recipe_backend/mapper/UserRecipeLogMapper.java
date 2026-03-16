package com.recipeapp.recipe_backend.mapper;

import com.recipeapp.recipe_backend.dto.bakedRecipesLogs.UserRecipeLogDTOResponse;
import com.recipeapp.recipe_backend.dto.savedRecipesLogs.bakedRecipesLogs.UserSavedRecipeLogDTOResponse;
import com.recipeapp.recipe_backend.entity.UserRecipeLog;
import com.recipeapp.recipe_backend.entity.UserSavedRecipeLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRecipeLogMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "recipe.id", target = "recipeId")
    UserRecipeLogDTOResponse toDto(UserRecipeLog userRecipeLog);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "recipe.id", target = "recipeId")
    UserSavedRecipeLogDTOResponse toSavedDto(UserSavedRecipeLog userSavedRecipeLog);

    List<UserRecipeLogDTOResponse> toDtoList(List<UserRecipeLog> userRecipeLogs);
    List<UserSavedRecipeLogDTOResponse> toSavedDtoList(List<UserSavedRecipeLog> userSavedRecipeLogs);
}
