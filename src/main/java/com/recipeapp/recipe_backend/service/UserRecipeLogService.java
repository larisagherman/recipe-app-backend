package com.recipeapp.recipe_backend.service;

import com.recipeapp.recipe_backend.dto.bakedRecipesLogs.UserRecipeLogDTORequest;
import com.recipeapp.recipe_backend.dto.bakedRecipesLogs.UserRecipeLogDTOResponse;
import com.recipeapp.recipe_backend.dto.bakedRecipesLogs.UserRecipeLogDTOUpdateRequest;
import com.recipeapp.recipe_backend.dto.savedRecipesLogs.bakedRecipesLogs.UserSavedRecipeLogDTORequest;
import com.recipeapp.recipe_backend.dto.savedRecipesLogs.bakedRecipesLogs.UserSavedRecipeLogDTOResponse;
import com.recipeapp.recipe_backend.entity.Recipe;
import com.recipeapp.recipe_backend.entity.UserRecipeLog;
import com.recipeapp.recipe_backend.entity.UserSavedRecipeLog;
import com.recipeapp.recipe_backend.entity.User;
import com.recipeapp.recipe_backend.mapper.UserRecipeLogMapper;
import com.recipeapp.recipe_backend.repository.RecipeRepository;
import com.recipeapp.recipe_backend.repository.UserRecipeLogRepository;
import com.recipeapp.recipe_backend.repository.UserSavedRecipeLogRepository;
import com.recipeapp.recipe_backend.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@RequiredArgsConstructor
@Service
public class UserRecipeLogService {
    private final UserRecipeLogRepository userRecipeLogRepository;
    private final UserSavedRecipeLogRepository userSavedRecipeLogRepository;
    private final UsersRepository usersRepository;
    private final RecipeRepository recipeRepository;
    private final UserRecipeLogMapper userRecipeLogMapper;


    public List<UserRecipeLogDTOResponse> getLogByUserId(Long userId) {
        List<UserRecipeLog> userRecipeLogs = userRecipeLogRepository.findAllByUserId(userId);
        return userRecipeLogMapper.toDtoList(userRecipeLogs);
    }
    public List<UserSavedRecipeLogDTOResponse> getSavedLogByUserId(Long userId) {
        List<UserSavedRecipeLog> userRecipeLogs = userSavedRecipeLogRepository.findAllByUserId(userId);
        return userRecipeLogMapper.toSavedDtoList(userRecipeLogs);
    }


    public void createLog(UserRecipeLogDTORequest request) {
        // Check if the user already has a log for this recipe
        if (userRecipeLogRepository.findByUserIdAndRecipeId(request.getUserId(), request.getRecipeId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already has a log for this recipe");
        }

        User user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        UserRecipeLog log = new UserRecipeLog();
        log.setUser(user);
        log.setRecipe(recipe);
        log.setCookedAt(request.getCookedAt());
        userRecipeLogRepository.save(log);
    }
    public void createSavedLog(UserSavedRecipeLogDTORequest request) {
        // Check if the user already has a log for this recipe
        if (userSavedRecipeLogRepository.findByUserIdAndRecipeId(request.getUserId(), request.getRecipeId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already has a log for this recipe");
        }

        User user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        UserSavedRecipeLog log = new UserSavedRecipeLog();
        log.setUser(user);
        log.setRecipe(recipe);
        userSavedRecipeLogRepository.save(log);
    }

    public void updateLog(Long logId, UserRecipeLogDTOUpdateRequest request) {
        UserRecipeLog log = userRecipeLogRepository.findById(logId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Log not found"));

        log.setRating(request.getRating());
        log.setNotes(request.getNotes());
        userRecipeLogRepository.save(log);
    }
    public void updateSavedRecipeLog(Long logId, UserSavedRecipeLogDTOResponse request) {
        UserSavedRecipeLog log = userSavedRecipeLogRepository.findById(logId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Log not found"));

        log.setNotes(request.getNotes());
        userSavedRecipeLogRepository.save(log);
    }

    public void deleteLog(Long logId) {
        if (!userRecipeLogRepository.existsById(logId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log not found");
        }
        userRecipeLogRepository.deleteById(logId);
    }
    public void deleteSavedRecipeLog(Long logId) {
        if (!userSavedRecipeLogRepository.existsById(logId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log not found");
        }
        userSavedRecipeLogRepository.deleteById(logId);
    }

    public List<UserRecipeLog> getLastWeekBakedRecipes(Long userId) {
        LocalDateTime startOfThisWeek = LocalDate.now()
                .with(DayOfWeek.MONDAY)
                .atStartOfDay();

        LocalDateTime startOfLastWeek = startOfThisWeek.minusWeeks(1);
        LocalDateTime endOfLastWeek = startOfThisWeek;

        return userRecipeLogRepository
                .findByUserIdAndCookedAtBetween(userId, startOfLastWeek, endOfLastWeek);
    }
}

