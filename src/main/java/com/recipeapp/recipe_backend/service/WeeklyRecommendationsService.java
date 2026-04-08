package com.recipeapp.recipe_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipeapp.recipe_backend.dto.WeeklyRecommendationsResponseDTO;
import com.recipeapp.recipe_backend.entity.GeneratedRecipe;
import com.recipeapp.recipe_backend.entity.User;
import com.recipeapp.recipe_backend.entity.WeeklyRecommendations;
import com.recipeapp.recipe_backend.mapper.GeneratedRecipeMapper;
import com.recipeapp.recipe_backend.mapper.WeeklyRecommendationMapper;
import com.recipeapp.recipe_backend.repository.GeneratedRecipeRepository;
import com.recipeapp.recipe_backend.repository.UsersRepository;
import com.recipeapp.recipe_backend.repository.WeeklyRecommendationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeeklyRecommendationsService {
    private final WeeklyRecommendationMapper weeklyRecommendationMapper;
    private final GeminiService geminiService;
    private final WeeklyRecommendationsRepository weeklyRecommendationsRepository;
    private final UsersRepository usersRepository;
    private final GeneratedRecipeMapper generatedRecipeMapper;
    private final GeneratedRecipeRepository generatedRecipeRepository;



    public Date getCurrentWeekStart() {
        LocalDate today = LocalDate.now(); // e.g., Wednesday 2026-03-25
        LocalDate monday = today.with(DayOfWeek.MONDAY); // goes back to Monday 2026-03-23
        return Date.valueOf(monday); // java.sql.Date for JPA
    }
    public WeeklyRecommendationsResponseDTO getWeeklyRecommendations(Long userId) throws JsonProcessingException {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Date weekStart = getCurrentWeekStart();

        WeeklyRecommendations weeklyRecommendations =weeklyRecommendationsRepository.findByUserAndWeekStart(user,weekStart);
        if(weeklyRecommendations==null){
                weeklyRecommendations=createWeeklyRecommendations(user,weekStart);
        }
        return weeklyRecommendationMapper.entityToResponseDto(weeklyRecommendations);

    }
    public WeeklyRecommendations createWeeklyRecommendations(User user, Date weekStart) throws JsonProcessingException {
        GeneratedRecipe generatedRecipe =generatedRecipeMapper.dtoToGeneratedRecipe(geminiService.generateWeeklyMealPlanForUser(user.getId()));
        generatedRecipeRepository.save(generatedRecipe);

        WeeklyRecommendations weeklyRecommendations = new WeeklyRecommendations();
        weeklyRecommendations.setUser(user);
        weeklyRecommendations.setWeekStart(weekStart);
        weeklyRecommendations.setRecipe(generatedRecipe);
        weeklyRecommendationsRepository.save(weeklyRecommendations);
        return weeklyRecommendations;
    }
    public List<WeeklyRecommendationsResponseDTO> getAllWeeklyRecommendationsForUser(Long userId){
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        List<WeeklyRecommendations> weeklyRecommendationsList = weeklyRecommendationsRepository.findAllByUser(user);
        return weeklyRecommendationMapper.entityToResponseDtoList(weeklyRecommendationsList);
    }
}
