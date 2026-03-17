package com.recipeapp.recipe_backend.mapper;

import com.recipeapp.recipe_backend.dto.UserPreferencesRequestDto;
import com.recipeapp.recipe_backend.dto.UserPreferencesResponseDto;
import com.recipeapp.recipe_backend.entity.UserPreferences;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserPreferencesMapper {
    @Mapping(source = "userId", target = "user.id")
    public UserPreferences requestDtoToUserPreferences(UserPreferencesRequestDto userPreferencesRequestDto);
    public List<UserPreferences> requestDtoToUserPreferences(List<UserPreferencesRequestDto> userPreferencesRequestDto);
    @Mapping(source = "user.id", target = "userId")
    public UserPreferencesResponseDto userPreferencesToUserPreferencesResponseDto(UserPreferences userPreferences);
}
