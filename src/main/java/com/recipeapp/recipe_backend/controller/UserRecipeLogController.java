package com.recipeapp.recipe_backend.controller;

import com.recipeapp.recipe_backend.dto.UserRecipeLogDTORequest;
import com.recipeapp.recipe_backend.dto.UserRecipeLogDTOResponse;
import com.recipeapp.recipe_backend.dto.UserRecipeLogDTOUpdateRequest;
import com.recipeapp.recipe_backend.service.UserRecipeLogService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-recipe-logs")
public class UserRecipeLogController {
    private final UserRecipeLogService userRecipeLogService;

    public UserRecipeLogController(UserRecipeLogService userRecipeLogService) {
        this.userRecipeLogService = userRecipeLogService;
    }

    @GetMapping("/{userId}")
    public List<UserRecipeLogDTOResponse> getLogByUserId(@PathVariable Long userId) {
        return userRecipeLogService.getLogByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createLog(@RequestBody UserRecipeLogDTORequest request) {
        userRecipeLogService.createLog(request);
    }

    @PutMapping("/{logId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateLog(@PathVariable Long logId, @RequestBody UserRecipeLogDTOUpdateRequest request) {
        userRecipeLogService.updateLog(logId, request);
    }

    @DeleteMapping("/{logId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLog(@PathVariable Long logId) {
        userRecipeLogService.deleteLog(logId);
    }
}

