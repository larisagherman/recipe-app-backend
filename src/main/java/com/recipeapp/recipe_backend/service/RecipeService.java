package com.recipeapp.recipe_backend.service;

import com.recipeapp.recipe_backend.dto.RecipeDTO;
import com.recipeapp.recipe_backend.entity.Recipe;
import com.recipeapp.recipe_backend.repository.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Map<String, Object> getRecipesPage(int page, int size) {
        Page<Recipe> recipePage = recipeRepository.findAll(PageRequest.of(page, size));

        // Map Recipe entities to RecipeDTO
        List<RecipeDTO> dtoList = recipePage.getContent()
                .stream()
                .map(r -> new RecipeDTO(
                        r.getId(),
                        r.getName(),
                        r.getImgSrc(),
                        r.getPrepTime(),
                        r.getCookTime(),
                        r.getServings(),
                        r.getIngredients(),
                        r.getDirections()
                ))
                .collect(Collectors.toList());

        // Prepare response with pagination info
        Map<String, Object> response = new HashMap<>();
        response.put("content", dtoList);
        response.put("page", recipePage.getNumber());
        response.put("size", recipePage.getSize());
        response.put("totalElements", recipePage.getTotalElements());
        response.put("totalPages", recipePage.getTotalPages());

        return response;
    }
    public Recipe getRecipeById(Long id){
        return recipeRepository.findById(id).orElseThrow(()->new RuntimeException("Recipe with that id not found") );
    }

}
