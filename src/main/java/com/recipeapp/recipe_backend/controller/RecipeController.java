package com.recipeapp.recipe_backend.controller;

import com.recipeapp.recipe_backend.entity.Recipe;
import com.recipeapp.recipe_backend.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recipes")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // frontend origin
public class RecipeController {
    private final RecipeService recipeService;
    @GetMapping
    public List<Recipe> getAllRecipes(){
        return recipeService.getAllRecipes();
    }
    @GetMapping("/page")
    public Map<String, Object> getRecipesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {
        return recipeService.getRecipesPage(page, size);
    }
    @GetMapping("/{id}")
    public Recipe getRecipeById( @PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

}
