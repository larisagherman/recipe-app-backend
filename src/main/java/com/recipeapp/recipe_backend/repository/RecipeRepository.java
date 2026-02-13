package com.recipeapp.recipe_backend.repository;

import com.recipeapp.recipe_backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
