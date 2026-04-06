package com.recipeapp.recipe_backend.service;

import com.recipeapp.recipe_backend.controller.GeneratedRecipe;
import com.recipeapp.recipe_backend.dto.GeneratedRecipeDTO;
import com.recipeapp.recipe_backend.mapper.GeneratedRecipeMapper;
import com.recipeapp.recipe_backend.repository.GeneratedRecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GeneratedRecipeService {
    private final GeneratedRecipeRepository generatedRecipeRepository;
    private final GeneratedRecipeMapper generatedRecipeMapper;
    public GeneratedRecipeDTO getGeneratedRecipeById(Long id){
       return generatedRecipeMapper.generatedRecipeToDto(generatedRecipeRepository.findById(id).orElseThrow());
    }
}
