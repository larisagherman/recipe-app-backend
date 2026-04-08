package com.recipeapp.recipe_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipeapp.recipe_backend.dto.GeneratedRecipeDTO;
import com.recipeapp.recipe_backend.dto.WeeklyRecommendationsResponseDTO;
import com.recipeapp.recipe_backend.entity.WeeklyRecommendations;
import com.recipeapp.recipe_backend.service.WeeklyRecommendationsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@AllArgsConstructor
public class WeeklyRecommendationController {
    private final WeeklyRecommendationsService weeklyRecommendationsService;
    @GetMapping("/user/{userId}/weekly-recommendation")
    public WeeklyRecommendationsResponseDTO getWeeklyRecommendationsForUser(@PathVariable Long userId) throws JsonProcessingException {
        return weeklyRecommendationsService.getWeeklyRecommendations(userId);
    }
    @GetMapping("/user/{userId}/weekly-recommendations")
    public List<WeeklyRecommendationsResponseDTO> getAllWeeklyRecommendationsByUserId(@PathVariable Long userId){
        return weeklyRecommendationsService.getAllWeeklyRecommendationsForUser(userId);
    }
}
