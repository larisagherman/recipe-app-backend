package com.recipeapp.recipe_backend.repository;

import com.recipeapp.recipe_backend.entity.User;
import com.recipeapp.recipe_backend.entity.WeeklyRecommendations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.Optional;

public interface WeeklyRecommendationsRepository extends JpaRepository<WeeklyRecommendations,Long> {
    WeeklyRecommendations findByUserAndWeekStart(User user, Date weekStart);
}
