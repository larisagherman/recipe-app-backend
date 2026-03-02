package com.recipeapp.recipe_backend.service;

import com.recipeapp.recipe_backend.dto.UserRecipeLogDTORequest;
import com.recipeapp.recipe_backend.dto.UserRecipeLogDTOResponse;
import com.recipeapp.recipe_backend.dto.UserRecipeLogDTOUpdateRequest;
import com.recipeapp.recipe_backend.entity.Recipe;
import com.recipeapp.recipe_backend.entity.UserRecipeLog;
import com.recipeapp.recipe_backend.entity.Users;
import com.recipeapp.recipe_backend.mapper.UserRecipeLogMapper;
import com.recipeapp.recipe_backend.repository.RecipeRepository;
import com.recipeapp.recipe_backend.repository.UserRecipeLogRepository;
import com.recipeapp.recipe_backend.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@RequiredArgsConstructor
@Service
public class UserRecipeLogService {
    private final UserRecipeLogRepository userRecipeLogRepository;
    private final UsersRepository usersRepository;
    private final RecipeRepository recipeRepository;
    private final UserRecipeLogMapper userRecipeLogMapper;


    public List<UserRecipeLogDTOResponse> getLogByUserId(Long userId) {
        List<UserRecipeLog> userRecipeLogs = userRecipeLogRepository.findAllByUserId(userId);
        return userRecipeLogMapper.toDtoList(userRecipeLogs);
    }


    public void createLog(UserRecipeLogDTORequest request) {
        // Check if the user already has a log for this recipe
        if (userRecipeLogRepository.findByUserIdAndRecipeId(request.getUserId(), request.getRecipeId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already has a log for this recipe");
        }

        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        UserRecipeLog log = new UserRecipeLog();
        log.setUser(user);
        log.setRecipe(recipe);
        log.setCookedAt(request.getCookedAt());
        userRecipeLogRepository.save(log);
    }

    public void updateLog(Long logId, UserRecipeLogDTOUpdateRequest request) {
        UserRecipeLog log = userRecipeLogRepository.findById(logId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Log not found"));

        log.setRating(request.getRating());
        log.setNotes(request.getNotes());
        userRecipeLogRepository.save(log);
    }

    public void deleteLog(Long logId) {
        if (!userRecipeLogRepository.existsById(logId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log not found");
        }
        userRecipeLogRepository.deleteById(logId);
    }
}

