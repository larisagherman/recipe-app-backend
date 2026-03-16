package com.recipeapp.recipe_backend.controller;

import com.recipeapp.recipe_backend.dto.bakedRecipesLogs.UserRecipeLogDTORequest;
import com.recipeapp.recipe_backend.dto.bakedRecipesLogs.UserRecipeLogDTOResponse;
import com.recipeapp.recipe_backend.dto.bakedRecipesLogs.UserRecipeLogDTOUpdateRequest;
import com.recipeapp.recipe_backend.dto.savedRecipesLogs.bakedRecipesLogs.UserSavedRecipeLogDTORequest;
import com.recipeapp.recipe_backend.dto.savedRecipesLogs.bakedRecipesLogs.UserSavedRecipeLogDTOResponse;
import com.recipeapp.recipe_backend.service.UserRecipeLogService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-saved-recipe-logs")
public class UserSavedRecipeLogController {
    private final UserRecipeLogService userRecipeLogService;

    public UserSavedRecipeLogController(UserRecipeLogService userRecipeLogService) {
        this.userRecipeLogService = userRecipeLogService;
    }

    @GetMapping("/{userId}")
    public List<UserSavedRecipeLogDTOResponse> getLogByUserId(@PathVariable Long userId) {
        return userRecipeLogService.getSavedLogByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createLog(@RequestBody UserSavedRecipeLogDTORequest request) {
        userRecipeLogService.createSavedLog(request);
    }

    @PutMapping("/{logId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateLog(@PathVariable Long logId, @RequestBody UserRecipeLogDTOUpdateRequest request) {
        userRecipeLogService.updateLog(logId, request);
    }

    @DeleteMapping("/{logId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLog(@PathVariable Long logId) {
        userRecipeLogService.deleteSavedRecipeLog(logId);
    }
}

