package com.recipeapp.recipe_backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponse{
    private Long id;
    private String name;
    private String ingredients;
    @JsonProperty("img_src")
    private String imgSrc;
    @JsonProperty("missing_count")
    private Integer missingCount;
    @JsonProperty("missing_ingredients")
    private List<String> missingIngredients;

}
