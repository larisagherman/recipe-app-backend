package com.recipeapp.recipe_backend.mapper;

import com.recipeapp.recipe_backend.dto.WeeklyRecommendationsResponseDTO;
import com.recipeapp.recipe_backend.entity.WeeklyRecommendations;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WeeklyRecommendationMapper {
    @Mapping(source = "user.id", target = "userId")
    WeeklyRecommendationsResponseDTO entityToResponseDto(WeeklyRecommendations weeklyRecommendations);

    @Mapping(source = "user.id", target = "userId")
    List<WeeklyRecommendationsResponseDTO> entityToResponseDtoList(List<WeeklyRecommendations> weeklyRecommendationsList);
}
