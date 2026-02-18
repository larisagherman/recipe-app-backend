package com.recipeapp.recipe_backend.mapper;

import com.recipeapp.recipe_backend.dto.UserRecipeLogDTOResponse;
import com.recipeapp.recipe_backend.entity.UserRecipeLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRecipeLogMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "recipe.id", target = "recipeId")
    UserRecipeLogDTOResponse toDto(UserRecipeLog userRecipeLog);

    List<UserRecipeLogDTOResponse> toDtoList(List<UserRecipeLog> userRecipeLogs);
}
