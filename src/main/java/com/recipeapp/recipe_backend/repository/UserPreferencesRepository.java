package com.recipeapp.recipe_backend.repository;

import com.recipeapp.recipe_backend.entity.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {

    UserPreferences findByUserId(@Param("userId") Long userId);
}
