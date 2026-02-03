package com.recipeapp.recipe_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponse{
    Long id;
    String name;
    String ingredients;
    String imgSrc;
}
