package com.recipeapp.recipe_backend.repository;

import com.recipeapp.recipe_backend.dto.GeneratedRecipeDTO;
import com.recipeapp.recipe_backend.entity.GeneratedRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneratedRecipeRepository extends JpaRepository<GeneratedRecipe, Long> {
    GeneratedRecipeDTO getGeneratedRecipeById(Long id);
}
