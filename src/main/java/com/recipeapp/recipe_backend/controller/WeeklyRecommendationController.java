package com.recipeapp.recipe_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipeapp.recipe_backend.dto.WeeklyRecommendationsResponseDTO;
import com.recipeapp.recipe_backend.entity.WeeklyRecommendations;
import com.recipeapp.recipe_backend.service.WeeklyRecommendationsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController
@RequestMapping("/weekly-recommendations")
@AllArgsConstructor
public class WeeklyRecommendationController {
    private final WeeklyRecommendationsService weeklyRecommendationsService;
    @GetMapping("/user/{userId}")
    public WeeklyRecommendationsResponseDTO getWeeklyRecommendationsForUser(@PathVariable Long userId) throws JsonProcessingException {
        return weeklyRecommendationsService.getWeeklyRecommendations(userId);
    }
}
