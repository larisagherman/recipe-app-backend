package com.recipeapp.recipe_backend.repository;

import com.recipeapp.recipe_backend.entity.UserRecipeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRecipeLogRepository extends JpaRepository<UserRecipeLog, Long> {
    Optional<Object> findByUserId(Long userId);

    List<UserRecipeLog> findAllByUserId(Long userId);

    Optional<UserRecipeLog> findByUserIdAndRecipeId(Long userId, Long recipeId);
}

