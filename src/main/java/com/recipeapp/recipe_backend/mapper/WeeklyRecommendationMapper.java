package com.recipeapp.recipe_backend.mapper;

import com.recipeapp.recipe_backend.dto.WeeklyRecommendationsResponseDTO;
import com.recipeapp.recipe_backend.entity.WeeklyRecommendations;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeeklyRecommendationMapper {
    WeeklyRecommendationsResponseDTO entityToResponseDto(WeeklyRecommendations weeklyRecommendations);
}
