package com.recipeapp.recipe_backend.controller;

import com.recipeapp.recipe_backend.dto.UserPreferencesRequestDto;
import com.recipeapp.recipe_backend.dto.UserPreferencesResponseDto;
import com.recipeapp.recipe_backend.service.UserPreferencesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-preferences")
@AllArgsConstructor
public class UserPreferencesController {
    private final UserPreferencesService userPreferencesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUserPreferences(@RequestBody UserPreferencesRequestDto userPreferencesRequestDto) {
        userPreferencesService.createUserPreferences(userPreferencesRequestDto);
    }
    @GetMapping("/{userId}")
    public UserPreferencesResponseDto getUserPreferences(@PathVariable Long userId) {
        return userPreferencesService.getUserPreferences(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserPreferences(@PathVariable Long userId) {
        userPreferencesService.deleteUserPreferences(userId);
    }
    @PutMapping("/{userId}")
    public void updateUserPreferences(@PathVariable Long userId, @RequestBody UserPreferencesRequestDto userPreferencesRequestDto) {
        userPreferencesService.updateUserPreferences(userId, userPreferencesRequestDto);
    }
}
