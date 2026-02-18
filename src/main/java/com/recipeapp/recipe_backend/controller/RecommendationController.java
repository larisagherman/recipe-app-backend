package com.recipeapp.recipe_backend.controller;

import com.recipeapp.recipe_backend.entity.RecommendationRequest;
import com.recipeapp.recipe_backend.entity.RecommendationResponse;
import com.recipeapp.recipe_backend.service.RecommendationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommend")
@AllArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;
    @PostMapping
    public List<RecommendationResponse> recommend(@RequestBody RecommendationRequest recommendationRequest) {
        return recommendationService.getRecommendations(recommendationRequest.query(),recommendationRequest.topK(),recommendationRequest.forbiddenIngredients(),recommendationRequest.strict());
    }
}
