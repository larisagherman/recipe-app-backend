package com.recipeapp.recipe_backend.service;

import com.recipeapp.recipe_backend.dto.UserPreferencesRequestDto;
import com.recipeapp.recipe_backend.dto.UserPreferencesResponseDto;
import com.recipeapp.recipe_backend.entity.UserPreferences;
import com.recipeapp.recipe_backend.mapper.UserPreferencesMapper;
import com.recipeapp.recipe_backend.repository.UserPreferencesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserPreferencesService {
    private final UserPreferencesRepository userPreferencesRepository;
    private final UserPreferencesMapper userPreferencesMapper;
    public void createUserPreferences(UserPreferencesRequestDto userPreferencesRequestDto) {
        UserPreferences userPreferences = userPreferencesMapper.requestDtoToUserPreferences(userPreferencesRequestDto);
        userPreferencesRepository.save(userPreferences);
    }
    public UserPreferencesResponseDto getUserPreferences(Long userId) {
        UserPreferences userPreference=userPreferencesRepository.findByUserId(userId);
        return userPreferencesMapper.userPreferencesToUserPreferencesResponseDto(userPreference);
    }
    public void deleteUserPreferences(Long userId) {
        userPreferencesRepository.deleteById(userId);
    }
    public void updateUserPreferences(Long userId, UserPreferencesRequestDto userPreferencesRequestDto) {
        UserPreferences existingUserPreferences = userPreferencesRepository.findByUserId(userId);
        existingUserPreferences.setDietaryTypes(userPreferencesRequestDto.getDietaryTypes());
        existingUserPreferences.setAllergies(userPreferencesRequestDto.getAllergies());
        existingUserPreferences.setDislikedIngredients(userPreferencesRequestDto.getDislikedIngredients());
        existingUserPreferences.setTastePreferences(userPreferencesRequestDto.getTastePreferences());
        existingUserPreferences.setFlavourPreferences(userPreferencesRequestDto.getFlavourPreferences());
        userPreferencesRepository.save(existingUserPreferences);
    }
}
