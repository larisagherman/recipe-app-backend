package com.recipeapp.recipe_backend.repository;

import com.recipeapp.recipe_backend.entity.UserRecipeLog;
import com.recipeapp.recipe_backend.entity.UserSavedRecipeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSavedRecipeLogRepository extends JpaRepository<UserSavedRecipeLog, Long> {
    Optional<Object> findByUserId(Long userId);

    List<UserSavedRecipeLog> findAllByUserId(Long userId);

    Optional<UserSavedRecipeLog> findByUserIdAndRecipeId(Long userId, Long recipeId);
}

