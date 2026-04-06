package com.recipeapp.recipe_backend.controller;

import com.recipeapp.recipe_backend.dto.GeneratedRecipeDTO;
import com.recipeapp.recipe_backend.service.GeneratedRecipeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/generated-recipes")
@AllArgsConstructor
public class GeneratedRecipe {
    private final GeneratedRecipeService generatedRecipeService;
    @GetMapping("/{id}")
    public GeneratedRecipeDTO getGeneratedRecipeById(@PathVariable Long id){
        return generatedRecipeService.getGeneratedRecipeById(id);
    }
}
